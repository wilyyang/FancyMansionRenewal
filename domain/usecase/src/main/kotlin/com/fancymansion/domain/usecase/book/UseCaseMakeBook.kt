package com.fancymansion.domain.usecase.book

import android.content.Context
import android.net.Uri
import com.fancymansion.core.common.const.EpisodeRef
import com.fancymansion.core.common.const.PageType
import com.fancymansion.core.common.const.getCoverFileName
import com.fancymansion.core.common.const.imageFileNamePrefix
import com.fancymansion.core.common.di.DispatcherIO
import com.fancymansion.core.common.util.getFileExtension
import com.fancymansion.domain.interfaceRepository.BookLocalRepository
import com.fancymansion.domain.model.book.BookInfoModel
import com.fancymansion.domain.model.book.ConditionModel.RouteConditionModel
import com.fancymansion.domain.model.book.ConditionModel.ShowSelectorConditionModel
import com.fancymansion.domain.model.book.LogicModel
import com.fancymansion.domain.model.book.PageLogicModel
import com.fancymansion.domain.model.book.PageModel
import com.fancymansion.domain.model.book.RouteModel
import com.fancymansion.domain.model.book.SelectorModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class UseCaseMakeBook @Inject constructor(
    @ApplicationContext private val context : Context,
    @DispatcherIO private val dispatcher: CoroutineDispatcher,
    private val bookLocalRepository: BookLocalRepository
) {
    suspend fun makeBookInfo(episodeRef: EpisodeRef, bookInfo : BookInfoModel) =
        withContext(dispatcher) {
            bookLocalRepository.makeBookInfo(userId = episodeRef.userId, mode = episodeRef.mode, bookId = episodeRef.bookId, bookInfo = bookInfo)
            bookLocalRepository.updateEditTime(episodeRef)
        }

    suspend fun updatePageContent(episodeRef: EpisodeRef, pageId: Long, page: PageModel) =
        withContext(dispatcher) {
            bookLocalRepository.deletePage(episodeRef, pageId)
            bookLocalRepository.makePage(episodeRef, pageId, page)
            bookLocalRepository.updateEditTime(episodeRef)
        }

    suspend fun makeBookLogic(episodeRef: EpisodeRef, logic: LogicModel) : Boolean =
        withContext(dispatcher) {
            bookLocalRepository.deleteLogic(episodeRef) && makeLogicAndUpdateEditTime(episodeRef, logic)
        }

    suspend fun updateBookPageLogic(episodeRef: EpisodeRef, pageLogic: PageLogicModel) : Boolean =
        withContext(dispatcher) {
            var logic = bookLocalRepository.loadLogic(episodeRef)

            // Start Page 변경
            if(pageLogic.type == PageType.START){
                logic.logics.firstOrNull{it.type == PageType.START}?.let { startPageLogic ->
                    if(pageLogic.pageId != startPageLogic.pageId){
                        val updatedPageLogic = startPageLogic.copy(type = PageType.NORMAL)
                        logic = logic.copy(logics = logic.logics.map { if (it.pageId == updatedPageLogic.pageId) updatedPageLogic else it })
                    }
                }
            }

            val modified = logic.copy(logics = logic.logics.map { if (it.pageId == pageLogic.pageId) pageLogic else it })
            makeBookLogic(episodeRef, modified)
        }

    suspend fun makePageImage(episodeRef: EpisodeRef, pageId: Long, uri: Uri) : String =
        withContext(dispatcher) {
            val nextImageNumber = (bookLocalRepository.getPageImageFiles(episodeRef, pageId)
                .maxOfOrNull {
                    val start = it.name.lastIndexOf("_") + 1
                    val end = it.name.lastIndexOf(".")
                    it.name.substring(start, end).toIntOrNull() ?: 0
                } ?: 0) + 1

            val fileExtension = context.getFileExtension(uri)?:""
            val nextImageName = "${pageId}$imageFileNamePrefix$nextImageNumber.$fileExtension"

            makePageImage(episodeRef, nextImageName, uri)
            nextImageName
        }

    suspend fun makePageImage(episodeRef: EpisodeRef, imageName: String, uri: Uri) =
        withContext(dispatcher) {
            bookLocalRepository.makePageImageFromUri(episodeRef, imageName, uri)
        }

    suspend fun deletePageImage(episodeRef: EpisodeRef, imageName: String) =
        withContext(dispatcher) {
            bookLocalRepository.deletePageImage(episodeRef, imageName)
        }

    suspend fun createBookCover(episodeRef: EpisodeRef, bookInfo: BookInfoModel, uri: Uri) =
        withContext(dispatcher) {
            val fileExtension = context.getFileExtension(uri)?:""
            getCoverFileName(episodeRef.bookId, 0, fileExtension).let { newBookCoverName ->
                bookLocalRepository.makeBookInfo(
                    episodeRef.userId, episodeRef.mode, episodeRef.bookId,
                    bookInfo.copy(introduce = bookInfo.introduce.copy(coverList = listOf(newBookCoverName)))
                ) && bookLocalRepository.makeCoverImageFromUri(
                    episodeRef.userId, episodeRef.mode, episodeRef.bookId,
                    newBookCoverName, uri
                )
            }
        }

    suspend fun saveEditedPageList(episodeRef: EpisodeRef, editedPageList: List<PageLogicModel>, deleteIds : Set<Long>) =
        withContext(dispatcher) {
            val originLogic = bookLocalRepository.loadLogic(episodeRef = episodeRef)
            val (originDeleteIds, originRemainingIds) = originLogic.logics.map { it.pageId }.toSet().partition { it in deleteIds }

            // Delete PageContent
            originDeleteIds.forEach { pageId ->
                bookLocalRepository.deletePage(episodeRef = episodeRef, pageId = pageId)
            }

            // Add PageContent
            val pagesToAdd = editedPageList.filter { it.pageId !in originRemainingIds }
            pagesToAdd.forEach { page ->
                bookLocalRepository.makePage(
                    episodeRef = episodeRef, pageId = page.pageId,
                    page = PageModel(
                        id = page.pageId,
                        title = page.title,
                        sources = listOf()
                    )
                )
            }

            // Update with the new logic
            originLogic.copy(
                logics = editedPageList
            ).let { updatedLogic ->
                makeLogicAndUpdateEditTime(episodeRef, updatedLogic)
                bookLocalRepository.updatePageCount(episodeRef, editedPageList.size)
            }
        }

    suspend fun saveEditedSelectorList(episodeRef: EpisodeRef, pageId: Long, editedSelectorList: List<SelectorModel>) =
        withContext(dispatcher) {
            val newLogic = bookLocalRepository.loadLogic(episodeRef = episodeRef).let { originLogic ->
                originLogic.logics.first { it.pageId == pageId }.let { originPageLogic ->
                    originPageLogic.copy(selectors = editedSelectorList).let { newPageLogic ->
                        originLogic.copy(
                            logics = originLogic.logics.map { if (it.pageId == pageId) newPageLogic else it }
                        )
                    }
                }
            }
            makeLogicAndUpdateEditTime(episodeRef, newLogic)
        }

    suspend fun saveEditedSelector(episodeRef: EpisodeRef, pageId: Long, selector: SelectorModel) =
        withContext(dispatcher) {
            val newLogic = bookLocalRepository.loadLogic(episodeRef = episodeRef).let { originLogic ->
                originLogic.logics.first { it.pageId == pageId }.let { originPageLogic ->
                    originPageLogic.copy(
                        selectors = originPageLogic.selectors.map { if (it.selectorId == selector.selectorId) selector else it }
                    ).let { newPageLogic ->
                        originLogic.copy(
                            logics = originLogic.logics.map { if (it.pageId == pageId) newPageLogic else it }
                        )
                    }
                }
            }
            makeLogicAndUpdateEditTime(episodeRef, newLogic)
        }

    suspend fun saveEditedRoute(episodeRef: EpisodeRef, pageId: Long, selectorId: Long, route: RouteModel) =
        withContext(dispatcher) {
            val newLogic = bookLocalRepository.loadLogic(episodeRef = episodeRef).let { originLogic ->
                originLogic.logics.first { it.pageId == pageId }.let { originPageLogic ->
                    originPageLogic.selectors.first { it.selectorId == selectorId }.let { originSelector ->
                        originSelector.copy(
                            routes = originSelector.routes.map { if (it.routeId == route.routeId) route else it }
                        ).let { newSelector ->
                            originPageLogic.copy(
                                selectors = originPageLogic.selectors.map { if (it.selectorId == newSelector.selectorId) newSelector else it }
                            ).let { newPageLogic ->
                                originLogic.copy(
                                    logics = originLogic.logics.map { if (it.pageId == newPageLogic.pageId) newPageLogic else it }
                                )
                            }
                        }
                    }
                }
            }
            makeLogicAndUpdateEditTime(episodeRef, newLogic)
        }

    suspend fun saveEditedShowSelectorCondition(episodeRef: EpisodeRef, pageId: Long, selectorId: Long, condition: ShowSelectorConditionModel) =
        withContext(dispatcher) {
            val newLogic = bookLocalRepository.loadLogic(episodeRef = episodeRef).let { originLogic ->
                originLogic.logics.first { it.pageId == pageId }.let { originPageLogic ->
                    originPageLogic.selectors.first { it.selectorId == selectorId }.let { originSelector ->
                        originSelector.copy(
                            showConditions = originSelector.showConditions.map { if (it.conditionId == condition.conditionId) condition else it }
                        ).let { newSelector ->
                            rebuildLogicWithUpdatedSelector(originLogic, originPageLogic, newSelector)
                        }
                    }
                }
            }
            makeLogicAndUpdateEditTime(episodeRef, newLogic)
        }

    suspend fun saveEditedRouteCondition(episodeRef: EpisodeRef, pageId: Long, selectorId: Long, routeId: Long, condition: RouteConditionModel) =
        withContext(dispatcher) {
            val newLogic = bookLocalRepository.loadLogic(episodeRef = episodeRef).let { originLogic ->
                originLogic.logics.first { it.pageId == pageId }.let { originPageLogic ->
                    originPageLogic.selectors.first { it.selectorId == selectorId }.let { originSelector ->
                        originSelector.routes.first { it.routeId == routeId }.let { originRoute ->
                            originRoute.copy(
                                routeConditions = originRoute.routeConditions.map { if (it.conditionId == condition.conditionId) condition else it }
                            ).let { newRoute ->
                                originSelector.copy(
                                    routes = originSelector.routes.map { if (it.routeId == newRoute.routeId) newRoute else it }
                                ).let { newSelector ->
                                    rebuildLogicWithUpdatedSelector(originLogic, originPageLogic, newSelector)
                                }
                            }
                        }
                    }
                }
            }
            makeLogicAndUpdateEditTime(episodeRef, newLogic)
        }

    private fun rebuildLogicWithUpdatedSelector(
        originLogic: LogicModel,
        originPageLogic: PageLogicModel,
        updatedSelector: SelectorModel
    ): LogicModel {
        val newPageLogic = originPageLogic.copy(
            selectors = originPageLogic.selectors.map {
                if (it.selectorId == updatedSelector.selectorId) updatedSelector else it
            }
        )
        return originLogic.copy(
            logics = originLogic.logics.map {
                if (it.pageId == newPageLogic.pageId) newPageLogic else it
            }
        )
    }

    suspend fun removeBookCover(episodeRef: EpisodeRef, bookInfo: BookInfoModel) =
        withContext(dispatcher) {
            bookInfo.introduce.coverList.getOrNull(0)?.let { coverImageName ->
                bookLocalRepository.makeBookInfo(
                    episodeRef.userId, episodeRef.mode, episodeRef.bookId,
                    bookInfo.copy(introduce = bookInfo.introduce.copy(coverList = listOf()))
                ) && bookLocalRepository.deleteCoverImage(episodeRef.userId, episodeRef.mode, episodeRef.bookId, coverImageName)
            }?: true
        }

    suspend fun bookLogicFileExists(
        episodeRef: EpisodeRef
    ) = withContext(dispatcher) {
        bookLocalRepository.bookLogicFileExists(episodeRef)
    }

    private suspend fun makeLogicAndUpdateEditTime(
        episodeRef: EpisodeRef,
        updatedLogic: LogicModel
    ): Boolean {
        return bookLocalRepository.makeLogic(
            episodeRef = episodeRef,
            logic = updatedLogic
        ) && bookLocalRepository.updateEditTime(episodeRef)
    }
}
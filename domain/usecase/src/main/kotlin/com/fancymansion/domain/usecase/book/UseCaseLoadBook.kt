package com.fancymansion.domain.usecase.book

import com.fancymansion.core.common.const.EpisodeRef
import com.fancymansion.core.common.const.ROUTE_ID_NOT_ASSIGNED
import com.fancymansion.core.common.const.ReadMode
import com.fancymansion.core.common.di.DispatcherIO
import com.fancymansion.core.common.throwable.exception.LoadPageException
import com.fancymansion.domain.interfaceRepository.BookLocalRepository
import com.fancymansion.domain.model.book.BookInfoModel
import com.fancymansion.domain.model.book.BookMetaModel
import com.fancymansion.domain.model.book.ConditionModel
import com.fancymansion.domain.model.book.EpisodeInfoModel
import com.fancymansion.domain.model.book.LogicModel
import com.fancymansion.domain.model.book.PageLogicModel
import com.fancymansion.domain.model.book.PageModel
import com.fancymansion.domain.model.book.RouteModel
import com.fancymansion.domain.model.book.SelectorModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import java.io.File
import javax.inject.Inject

class UseCaseLoadBook @Inject constructor(
    @param:DispatcherIO private val dispatcher: CoroutineDispatcher,
    private val bookLocalRepository: BookLocalRepository
) {
    suspend fun loadBookInfo(episodeRef: EpisodeRef): BookInfoModel =
        withContext(dispatcher) {
            bookLocalRepository.loadBookInfo(userId = episodeRef.userId, mode = episodeRef.mode, bookId = episodeRef.bookId)
        }

    suspend fun loadBookMetaData(userId: String, mode: ReadMode, bookId: String): BookMetaModel =
        withContext(dispatcher) {
            bookLocalRepository.loadMetaData(userId = userId, mode = mode, bookId = bookId)
        }

    suspend fun loadEpisodeInfo(episodeRef: EpisodeRef): EpisodeInfoModel =
        withContext(dispatcher) {
            bookLocalRepository.loadEpisodeInfo(episodeRef = episodeRef)
        }

    suspend fun loadLogic(episodeRef: EpisodeRef): LogicModel =
        withContext(dispatcher) {
            bookLocalRepository.loadLogic(episodeRef)
        }

    suspend fun loadPageLogic(episodeRef: EpisodeRef, pageId: Long): PageLogicModel? =
        withContext(dispatcher) {
            bookLocalRepository.loadLogic(episodeRef).logics.firstOrNull { it.pageId == pageId }
        }

    suspend fun loadPage(episodeRef: EpisodeRef, pageId: Long): PageModel =
        withContext(dispatcher) {
            try {
                bookLocalRepository.loadPage(episodeRef, pageId)
            } catch (e: Exception) {
                throw LoadPageException(
                    message = "Failed to load page because ${e.cause}. page id = $pageId, book id = ${episodeRef.bookId}",
                    episodeRef = episodeRef,
                    pageId = pageId
                )
            }
        }

    suspend fun loadPageImage(episodeRef: EpisodeRef, imageName: String): File =
        withContext(dispatcher) {
            bookLocalRepository.loadPageImage(episodeRef, imageName)
        }

    suspend fun loadCoverImage(episodeRef: EpisodeRef, imageName: String): File =
        withContext(dispatcher) {
            bookLocalRepository.loadCoverImage(episodeRef.userId, episodeRef.mode, episodeRef.bookId, imageName)
        }

    suspend fun getSelector(logic: LogicModel, pageId : Long, selectorId : Long): SelectorModel =
        withContext(dispatcher) {
            logic.logics
                .firstOrNull { it.pageId == pageId }
                ?.selectors
                ?.firstOrNull { it.selectorId == selectorId }!!
        }

    suspend fun getRoute(logic: LogicModel, pageId : Long, selectorId : Long, routeId : Long): RouteModel =
        withContext(dispatcher) {
            logic.logics
                .firstOrNull { it.pageId == pageId }
                ?.selectors
                ?.firstOrNull { it.selectorId == selectorId }
                ?.routes
                ?.firstOrNull { it.routeId == routeId }!!
        }

    suspend fun getShowSelectorCondition(logic: LogicModel, pageId : Long, selectorId : Long, conditionId: Long): ConditionModel.ShowSelectorConditionModel =
        withContext(dispatcher) {
            getSelector(logic, pageId, selectorId).showConditions.firstOrNull { it.conditionId == conditionId }!!
        }

    suspend fun getRouteCondition(logic: LogicModel, pageId : Long, selectorId : Long, routeId : Long, conditionId: Long): ConditionModel.RouteConditionModel =
        withContext(dispatcher) {
            getRoute(logic, pageId, selectorId, routeId).routeConditions.firstOrNull { it.conditionId == conditionId }!!
        }
}
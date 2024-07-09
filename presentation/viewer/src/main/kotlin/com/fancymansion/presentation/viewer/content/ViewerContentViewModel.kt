package com.fancymansion.presentation.viewer.content

import androidx.lifecycle.SavedStateHandle
import com.fancymansion.core.common.const.BookRef
import com.fancymansion.core.common.const.PageType
import com.fancymansion.core.common.const.ReadMode
import com.fancymansion.core.common.const.testBookRef
import com.fancymansion.core.common.resource.StringValue
import com.fancymansion.core.presentation.base.BaseViewModel
import com.fancymansion.core.presentation.base.LoadState
import com.fancymansion.domain.model.book.LogicModel
import com.fancymansion.domain.model.book.SourceModel
import com.fancymansion.domain.usecase.book.UseCaseBookLogic
import com.fancymansion.domain.usecase.book.UseCaseLoadBook
import com.fancymansion.domain.usecase.book.UseCaseMakeBook
import com.fancymansion.domain.usecase.book.UseCasePageSetting
import com.fancymansion.presentation.viewer.R
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ViewerContentViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val useCasePageSetting: UseCasePageSetting,
    private val useCaseLoadBook: UseCaseLoadBook,
    private val useCaseBookLogic: UseCaseBookLogic,
    private val useCaseMakeBook: UseCaseMakeBook
) : BaseViewModel<ViewerContentContract.State, ViewerContentContract.Event, ViewerContentContract.Effect>() {
    private lateinit var bookRef : BookRef
    private lateinit var logic : LogicModel

    override fun setInitialState() = ViewerContentContract.State()

    override fun handleEvents(event: ViewerContentContract.Event) {
        when (event) {
            is ViewerContentContract.Event.OnConfirmMoveSaveDialog -> {
                launchWithLoading {
                    loadPageContent(event.pageId)
                }
            }

            is ViewerContentContract.Event.OnCancelMoveSaveDialog -> {
                launchWithLoading {
                    initializeAndLoadStartPage()
                }
            }

            is ViewerContentContract.Event.OnClickSelector -> {
                launchWithLoading {
                    handleSelectorClick(pageId = event.pageId, selectorId = event.selectorId)
                }
            }

            is ViewerContentContract.Event.ChangePageBackgroundColor -> {
                launchWithException {
                    val newPageContentSetting = uiState.value.pageSetting.pageContentSetting.copy(
                        backgroundColor = event.color
                    )
                    val newPageSetting = uiState.value.pageSetting.copy(
                        pageContentSetting = newPageContentSetting
                    )

                    useCasePageSetting.savePageSetting(bookRef = bookRef, pageSetting = newPageSetting)
                }
            }

            is ViewerContentContract.Event.ChangeContentTextSize -> {
                launchWithException {
                    val newPageContentSetting = uiState.value.pageSetting.pageContentSetting.copy(
                        textSize = event.textSize
                    )
                    val newPageSetting = uiState.value.pageSetting.copy(
                        pageContentSetting = newPageContentSetting
                    )

                    useCasePageSetting.savePageSetting(bookRef = bookRef, pageSetting = newPageSetting)
                }
            }

            is ViewerContentContract.Event.ChangeImageMargin -> {
                launchWithException {
                    val newPageContentSetting = uiState.value.pageSetting.pageContentSetting.copy(
                        imageMarginHorizontal = event.margin
                    )
                    val newPageSetting = uiState.value.pageSetting.copy(
                        pageContentSetting = newPageContentSetting
                    )

                    useCasePageSetting.savePageSetting(bookRef = bookRef, pageSetting = newPageSetting)
                }
            }
        }
    }

    init {
        bookRef = testBookRef

        scope.launch {
            useCasePageSetting.getPageSettingFlow(bookRef).collectLatest {
                setState {
                    copy(
                        pageSetting = it
                    )
                }
            }
        }

        launchWithLoading(endLoadState = null) {

            useCaseMakeBook.makeSampleBook()
            logic = useCaseLoadBook.loadLogic(bookRef)

            when(bookRef.mode){
                ReadMode.EDIT -> {
                    initializeAndLoadStartPage()
                    setLoadStateIdle()
                }

                ReadMode.READ -> {
                    useCaseBookLogic.getReadingProgressPageId(bookRef).let { saveId ->
                        if (saveId.isNullOrBlank()) {
                            initializeAndLoadStartPage()
                            setLoadStateIdle()
                        } else {
                            setLoadState(LoadState.AlarmDialog(
                                message = StringValue.StringResource(R.string.alarm_question_move_save_page),
                                onConfirm = {
                                    setEvent(ViewerContentContract.Event.OnConfirmMoveSaveDialog(saveId.toLong()))
                                },
                                onDismiss = {
                                    setEvent(ViewerContentContract.Event.OnCancelMoveSaveDialog)
                                }
                            ))
                        }
                    }
                }
            }
        }
    }

    private suspend fun handleSelectorClick(pageId : Long, selectorId : Long) {
        useCaseBookLogic.incrementActionCount(bookRef, actionId = selectorId)

        val nextPageId = useCaseBookLogic.getNextRoutePageId(
            bookRef,
            routes = logic.logics
                .first { it.id == pageId }
                .selectors
                .first { it.id == selectorId }
                .routes
        )
        useCaseBookLogic.incrementActionCount(bookRef, actionId = nextPageId)
        useCaseBookLogic.updateReadingProgressPageId(bookRef, pageId)
        loadPageContent(nextPageId)
    }

    private suspend fun initializeAndLoadStartPage(){
        useCaseBookLogic.resetBookData(bookRef)
        logic.logics.first { it.type == PageType.START }.id.let { pageId ->
            useCaseBookLogic.incrementActionCount(bookRef, actionId = pageId)
            loadPageContent(pageId)
        }
    }

    private suspend fun loadPageContent(pageId : Long){
        val page = useCaseLoadBook.loadPage(bookRef, pageId = pageId)
        val selectors = useCaseBookLogic.getVisibleSelectors(bookRef, logic = logic, pageId = pageId)

        val pageWrapper = PageWrapper(
            id = page.id,
            title = page.title,
            sources = page.sources.map {
                when(it){
                    is SourceModel.TextModel -> {
                        SourceWrapper.TextWrapper(it.description)
                    }
                    is SourceModel.ImageModel -> {
                        SourceWrapper.ImageWrapper(useCaseLoadBook.loadImage(bookRef, it.imageName))
                    }
                }
            }
        )

        setState {
            copy(
                pageWrapper = pageWrapper,
                selectors = selectors
            )
        }
    }
}
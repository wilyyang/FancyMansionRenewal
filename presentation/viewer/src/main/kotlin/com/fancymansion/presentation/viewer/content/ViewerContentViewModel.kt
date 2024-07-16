package com.fancymansion.presentation.viewer.content

import androidx.lifecycle.SavedStateHandle
import com.fancymansion.core.common.const.ArgName.NAME_BOOK_ID
import com.fancymansion.core.common.const.ArgName.NAME_EPISODE_ID
import com.fancymansion.core.common.const.ArgName.NAME_USER_ID
import com.fancymansion.core.common.const.EpisodeRef
import com.fancymansion.core.common.const.PageType
import com.fancymansion.core.common.const.ReadMode
import com.fancymansion.core.common.const.testEpisodeRef
import com.fancymansion.core.common.resource.StringValue
import com.fancymansion.core.presentation.base.BaseViewModel
import com.fancymansion.core.presentation.base.LoadState
import com.fancymansion.domain.model.book.LogicModel
import com.fancymansion.domain.model.book.PageSettingModel
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
    private var episodeRef : EpisodeRef = savedStateHandle.run {
        EpisodeRef(
            get<String>(NAME_USER_ID)?.ifBlank { testEpisodeRef.userId } ?: testEpisodeRef.userId,
            testEpisodeRef.mode, //get<ReadMode>(NAME_READ_MODE)
            get<String>(NAME_BOOK_ID)?.ifBlank { testEpisodeRef.bookId } ?: testEpisodeRef.bookId,
            get<String>(NAME_EPISODE_ID)?.ifBlank { testEpisodeRef.episodeId } ?: testEpisodeRef.episodeId
        )
    }
    private lateinit var logic : LogicModel

    override fun setInitialState() = ViewerContentContract.State()

    override fun handleEvents(event: ViewerContentContract.Event) {
        when (event) {
            is ViewerContentContract.Event.OnConfirmMoveSavePageDialog -> {
                launchWithLoading {
                    loadPageContent(event.pageId)
                }
            }

            is ViewerContentContract.Event.OnCancelMoveSavePageDialog -> {
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

                    useCasePageSetting.savePageSetting(userId = episodeRef.userId, mode = episodeRef.mode.name, bookId = episodeRef.bookId, pageSetting = newPageSetting)
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

                    useCasePageSetting.savePageSetting(userId = episodeRef.userId, mode = episodeRef.mode.name, bookId = episodeRef.bookId, pageSetting = newPageSetting)
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

                    useCasePageSetting.savePageSetting(userId = episodeRef.userId, mode = episodeRef.mode.name, bookId = episodeRef.bookId, pageSetting = newPageSetting)
                }
            }
        }
    }

    init {
        scope.launch {
            useCasePageSetting.getPageSettingFlow(userId = episodeRef.userId, mode = episodeRef.mode.name, bookId = episodeRef.bookId).collectLatest {
                if(it == null){
                    useCasePageSetting.savePageSetting(userId = episodeRef.userId, mode = episodeRef.mode.name, bookId = episodeRef.bookId, pageSetting = PageSettingModel())
                }else{
                    setState {
                        copy(
                            pageSetting = it
                        )
                    }
                }
            }
        }

        launchWithLoading(endLoadState = null) {

            useCaseMakeBook.makeSampleEpisode()
            logic = useCaseLoadBook.loadLogic(episodeRef)

            when(episodeRef.mode){
                ReadMode.EDIT -> {
                    initializeAndLoadStartPage()
                    setLoadStateIdle()
                }

                ReadMode.READ -> {
                    useCaseBookLogic.getReadingProgressPageId(episodeRef).let { saveId ->
                        if (saveId == null) {
                            initializeAndLoadStartPage()
                            setLoadStateIdle()
                        } else {
                            setLoadState(LoadState.AlarmDialog(
                                message = StringValue.StringResource(R.string.alarm_question_move_save_page),
                                onConfirm = {
                                    setEvent(ViewerContentContract.Event.OnConfirmMoveSavePageDialog(saveId))
                                },
                                onDismiss = {
                                    setEvent(ViewerContentContract.Event.OnCancelMoveSavePageDialog)
                                }
                            ))
                        }
                    }
                }
            }
        }
    }

    private suspend fun initializeAndLoadStartPage(){
        useCaseBookLogic.resetEpisodeData(episodeRef)
        logic.logics.first { it.type == PageType.START }.pageId.let { pageId ->
            useCaseBookLogic.incrementActionCount(episodeRef, pageId = pageId)
            loadPageContent(pageId)
        }
    }

    private suspend fun handleSelectorClick(pageId : Long, selectorId : Long) {
        useCaseBookLogic.incrementActionCount(episodeRef, pageId = pageId, selectorId = selectorId)

        val nextPageId = useCaseBookLogic.getNextRoutePageId(
            episodeRef,
            routes = logic.logics
                .first { it.pageId == pageId }
                .selectors
                .first { it.selectorId == selectorId }
                .routes
        )
        useCaseBookLogic.incrementActionCount(episodeRef, pageId = nextPageId)
        useCaseBookLogic.updateReadingProgressPageId(episodeRef, nextPageId)
        loadPageContent(nextPageId)
    }

    private suspend fun loadPageContent(pageId : Long){
        val page = useCaseLoadBook.loadPage(episodeRef, pageId = pageId)
        val selectors = useCaseBookLogic.getVisibleSelectors(episodeRef, logic = logic, pageId = pageId)

        val pageWrapper = PageWrapper(
            id = page.id,
            title = page.title,
            sources = page.sources.map {
                when(it){
                    is SourceModel.TextModel -> {
                        SourceWrapper.TextWrapper(it.description)
                    }
                    is SourceModel.ImageModel -> {
                        SourceWrapper.ImageWrapper(useCaseLoadBook.loadPageImage(episodeRef, it.imageName))
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
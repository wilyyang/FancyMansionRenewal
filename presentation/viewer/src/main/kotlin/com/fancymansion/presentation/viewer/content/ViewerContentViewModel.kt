package com.fancymansion.presentation.viewer.content

import androidx.lifecycle.SavedStateHandle
import com.fancymansion.core.common.const.*
import com.fancymansion.core.common.const.ArgName.NAME_BOOK_ID
import com.fancymansion.core.common.const.ArgName.NAME_BOOK_TITLE
import com.fancymansion.core.common.const.ArgName.NAME_EPISODE_ID
import com.fancymansion.core.common.const.ArgName.NAME_EPISODE_TITLE
import com.fancymansion.core.common.const.ArgName.NAME_PAGE_ID
import com.fancymansion.core.common.const.ArgName.NAME_READ_MODE
import com.fancymansion.core.common.const.ArgName.NAME_USER_ID
import com.fancymansion.core.common.resource.StringValue
import com.fancymansion.core.common.throwable.exception.LoadPageException
import com.fancymansion.core.presentation.base.BaseViewModel
import com.fancymansion.core.presentation.base.LoadState
import com.fancymansion.domain.model.book.LogicModel
import com.fancymansion.domain.model.book.PageModel
import com.fancymansion.domain.model.book.PageSettingModel
import com.fancymansion.domain.model.book.SelectorModel
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
    private val useCaseBookLogic: UseCaseBookLogic
) : BaseViewModel<ViewerContentContract.State, ViewerContentContract.Event, ViewerContentContract.Effect>() {
    private var episodeRef : EpisodeRef = savedStateHandle.run {
        EpisodeRef(
            get<String>(NAME_USER_ID)!!,
            get<ReadMode>(NAME_READ_MODE)!!,
            get<String>(NAME_BOOK_ID)!!,
            get<String>(NAME_EPISODE_ID)!!
        )
    }
    private lateinit var logic : LogicModel

    override fun setInitialState() = ViewerContentContract.State()

    override fun handleEvents(event: ViewerContentContract.Event) {
        when (event) {
            is ViewerContentContract.Event.OnConfirmMoveSavePageDialog -> {
                launchWithLoading {
                    loadPageContent(event.pageId).let { (pageWrapper, selectors) ->
                        setState {
                            copy(
                                pageWrapper = pageWrapper,
                                selectors = selectors
                            )
                        }
                    }
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

            is ViewerContentContract.Event.SettingEvent -> handleSettingEvent(event)
        }
    }

    private fun handleSettingEvent(event: ViewerContentContract.Event.SettingEvent) {
        launchWithLoading {
            val updatedPageSetting = uiState.value.pageSetting.let { setting ->
                when (event) {
                    is ViewerContentContract.Event.SettingEvent.ChangeSettingPageTheme ->
                        setting.copy(pageTheme = event.pageTheme)

                    is ViewerContentContract.Event.SettingEvent.IncrementSettingContentTextSize,
                    is ViewerContentContract.Event.SettingEvent.DecrementSettingContentTextSize -> {
                        val increment = if (event is ViewerContentContract.Event.SettingEvent.IncrementSettingContentTextSize) 1 else -1
                        val targetIdx = PageTextSize.values.indexOf(setting.pageContentSetting.textSize) + increment
                        if (targetIdx in PageTextSize.values.indices) {
                            setting.copy(pageContentSetting = setting.pageContentSetting.copy(textSize = PageTextSize.values[targetIdx]))
                        } else {
                            setting
                        }
                    }

                    is ViewerContentContract.Event.SettingEvent.IncrementSettingContentLineHeight,
                    is ViewerContentContract.Event.SettingEvent.DecrementSettingContentLineHeight -> {
                        val increment = if (event is ViewerContentContract.Event.SettingEvent.IncrementSettingContentLineHeight) 1 else -1
                        val targetIdx = PageLineHeight.values.indexOf(setting.pageContentSetting.lineHeight) + increment
                        if (targetIdx in PageLineHeight.values.indices) {
                            setting.copy(pageContentSetting = setting.pageContentSetting.copy(lineHeight = PageLineHeight.values[targetIdx]))
                        } else {
                            setting
                        }
                    }

                    is ViewerContentContract.Event.SettingEvent.IncrementSettingContentTextMarginHorizontal,
                    is ViewerContentContract.Event.SettingEvent.DecrementSettingContentTextMarginHorizontal -> {
                        val increment = if (event is ViewerContentContract.Event.SettingEvent.IncrementSettingContentTextMarginHorizontal) 1 else -1
                        val targetIdx = PageMarginHorizontal.values.indexOf(setting.pageContentSetting.textMarginHorizontal) + increment
                        if (targetIdx in PageMarginHorizontal.values.indices) {
                            setting.copy(pageContentSetting = setting.pageContentSetting.copy(textMarginHorizontal = PageMarginHorizontal
                                .values[targetIdx]))
                        } else {
                            setting
                        }
                    }

                    is ViewerContentContract.Event.SettingEvent.IncrementSettingContentImageMarginHorizontal,
                    is ViewerContentContract.Event.SettingEvent.DecrementSettingContentImageMarginHorizontal -> {
                        val increment = if (event is ViewerContentContract.Event.SettingEvent.IncrementSettingContentImageMarginHorizontal) 1 else -1
                        val targetIdx = PageMarginHorizontal.values.indexOf(setting.pageContentSetting.imageMarginHorizontal) + increment
                        if (targetIdx in PageMarginHorizontal.values.indices) {
                            setting.copy(pageContentSetting = setting.pageContentSetting.copy(imageMarginHorizontal = PageMarginHorizontal
                                .values[targetIdx]))
                        } else {
                            setting
                        }
                    }

                    is ViewerContentContract.Event.SettingEvent.IncrementSettingSelectorTextSize,
                    is ViewerContentContract.Event.SettingEvent.DecrementSettingSelectorTextSize -> {
                        val increment = if (event is ViewerContentContract.Event.SettingEvent.IncrementSettingSelectorTextSize) 1 else -1
                        val targetIdx = PageTextSize.values.indexOf(setting.selectorSetting.textSize) + increment
                        if (targetIdx in PageTextSize.values.indices) {
                            setting.copy(selectorSetting = setting.selectorSetting.copy(textSize = PageTextSize
                                .values[targetIdx]))
                        } else {
                            setting
                        }
                    }

                    is ViewerContentContract.Event.SettingEvent.IncrementSettingSelectorPaddingVertical,
                    is ViewerContentContract.Event.SettingEvent.DecrementSettingSelectorPaddingVertical -> {
                        val increment = if (event is ViewerContentContract.Event.SettingEvent.IncrementSettingSelectorPaddingVertical) 1 else -1
                        val targetIdx = SelectorPaddingVertical.values.indexOf(setting.selectorSetting.paddingVertical) + increment
                        if (targetIdx in SelectorPaddingVertical.values.indices) {
                            setting.copy(selectorSetting = setting.selectorSetting.copy(paddingVertical = SelectorPaddingVertical
                                .values[targetIdx]))
                        } else {
                            setting
                        }
                    }
                }
            }

            useCasePageSetting.savePageSetting(
                userId = episodeRef.userId,
                mode = episodeRef.mode.name,
                bookId = episodeRef.bookId,
                pageSetting = updatedPageSetting
            )
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

        launchWithInit(endLoadState = null) {

            val bookTitle = savedStateHandle.get<String>(NAME_BOOK_TITLE)!!
            val episodeTitle = savedStateHandle.get<String>(NAME_EPISODE_TITLE)!!
            setState {
                copy(
                    bookTitle = bookTitle,
                    episodeTitle = episodeTitle
                )
            }

            logic = useCaseLoadBook.loadLogic(episodeRef)

            when(episodeRef.mode){
                ReadMode.EDIT -> {
                    val receivedId = savedStateHandle.get<Long>(NAME_PAGE_ID)
                    if(receivedId != null && receivedId != PAGE_ID_NOT_ASSIGNED){
                        initializeAndLoadTestPage(receivedId)
                    }else{
                        initializeAndLoadStartPage()
                    }
                    setLoadStateIdle()
                }

                ReadMode.READ -> {
                    useCaseBookLogic.getReadingProgressPageId(episodeRef).let { saveId ->
                        if (saveId == null) {
                            initializeAndLoadStartPage()
                            setLoadStateIdle()
                        } else {
                            setLoadState(LoadState.AlarmDialog(
                                title = StringValue.StringResource(R.string.alarm_title_move_save_page),
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
            loadPageContent(pageId).let { (pageWrapper, selectors) ->
                setState {
                    copy(
                        pageWrapper = pageWrapper,
                        selectors = selectors
                    )
                }
            }
        }
    }

    private suspend fun initializeAndLoadTestPage(receivedId : Long){
        useCaseBookLogic.resetEpisodeData(episodeRef)
        logic.logics.first { it.pageId == receivedId }.pageId.let { pageId ->
            useCaseBookLogic.incrementActionCount(episodeRef, pageId = pageId)
            loadPageContent(pageId).let { (pageWrapper, selectors) ->
                setState {
                    copy(
                        pageWrapper = pageWrapper,
                        selectors = selectors
                    )
                }
            }
        }
    }

    private suspend fun handleSelectorClick(pageId : Long, selectorId : Long) {
        val nextPageId = useCaseBookLogic.getNextRoutePageId(
            episodeRef,
            routes = logic.logics
                .first { it.pageId == pageId }
                .selectors
                .first { it.selectorId == selectorId }
                .routes
        )
        useCaseBookLogic.incrementActionCount(episodeRef, pageId = pageId, selectorId = selectorId)
        val (pageWrapper, selectors) = loadPageContent(nextPageId)
        useCaseBookLogic.incrementActionCount(episodeRef, pageId = nextPageId)
        useCaseBookLogic.updateReadingProgressPageId(episodeRef, nextPageId)

        setState {
            copy(
                pageWrapper = pageWrapper,
                selectors = selectors
            )
        }
    }

    private suspend fun loadPageContent(pageId : Long) : Pair<PageWrapper, List<SelectorModel>>{
        val page = useCaseLoadBook.loadPage(episodeRef, pageId = pageId)
        val selectors = useCaseBookLogic.getVisibleSelectors(episodeRef, logic = logic, pageId = pageId)

        val pageWrapper = uiState.value.pageWrapper?.let { beforePage ->
            if (page.id == beforePage.id) {
                beforePage.copy(diff = beforePage.diff + 1)
            } else {
                createPageWrapper(page)
            }
        } ?: createPageWrapper(page)

        return Pair(pageWrapper, selectors)
    }

    private suspend fun createPageWrapper(page : PageModel) : PageWrapper {
        return PageWrapper(
            id = page.id,
            title = page.title,
            sources = page.sources.map {
                when (it) {
                    is SourceModel.TextModel -> {
                        SourceWrapper.TextWrapper(it.description)
                    }

                    is SourceModel.ImageModel -> {
                        SourceWrapper.ImageWrapper(useCaseLoadBook.loadPageImage(episodeRef, it.imageName))
                    }
                }
            }
        )
    }

    override fun showErrorResult(
        throwable: Throwable,
        defaultConfirm: () -> Unit,
        defaultDismiss: () -> Unit
    ) {
        when(throwable){
            is LoadPageException -> setLoadState(LoadState.ErrorDialog(message = throwable.message, dismissText = null))
            else -> super.showErrorResult(throwable, defaultConfirm, defaultDismiss)
        }
    }
}
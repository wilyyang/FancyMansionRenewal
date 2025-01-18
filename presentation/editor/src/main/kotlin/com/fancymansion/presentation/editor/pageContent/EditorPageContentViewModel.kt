package com.fancymansion.presentation.editor.pageContent

import androidx.lifecycle.SavedStateHandle
import com.fancymansion.core.common.const.ArgName
import com.fancymansion.core.common.const.EpisodeRef
import com.fancymansion.core.common.const.ReadMode
import com.fancymansion.core.common.resource.StringValue
import com.fancymansion.core.presentation.base.BaseViewModel
import com.fancymansion.core.presentation.base.CommonEvent
import com.fancymansion.core.presentation.base.LoadState
import com.fancymansion.domain.model.book.PageModel
import com.fancymansion.domain.model.book.SelectorModel
import com.fancymansion.domain.model.book.SourceModel
import com.fancymansion.domain.usecase.book.UseCaseBookLogic
import com.fancymansion.domain.usecase.book.UseCaseLoadBook
import com.fancymansion.domain.usecase.book.UseCaseMakeBook
import com.fancymansion.domain.usecase.util.UseCaseGetResource
import com.fancymansion.presentation.editor.R
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class EditorPageContentViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val useCaseLoadBook: UseCaseLoadBook,
    private val useCaseMakeBook: UseCaseMakeBook,
    private val useCaseBookLogic: UseCaseBookLogic,
    private val useCaseGetResource: UseCaseGetResource
) : BaseViewModel<EditorPageContentContract.State, EditorPageContentContract.Event, EditorPageContentContract.Effect>() {

    private var isFirstResumeComplete = false
    private lateinit var selectors: List<SelectorModel>

    private val episodeRef: EpisodeRef = savedStateHandle.run {
        EpisodeRef(
            requireNotNull(get<String>(ArgName.NAME_USER_ID)),
            requireNotNull(get<ReadMode>(ArgName.NAME_READ_MODE)),
            requireNotNull(get<String>(ArgName.NAME_BOOK_ID)),
            requireNotNull(get<String>(ArgName.NAME_EPISODE_ID))
        )
    }

    init {
        initializeState()
    }

    override fun setInitialState() = EditorPageContentContract.State()

    override fun handleEvents(event: EditorPageContentContract.Event) {
        when (event) {
            else -> {}
        }
    }

    override fun handleCommonEvents(event: CommonEvent) {
        when(event){
            is CommonEvent.OnResume -> handleOnResume()
            is CommonEvent.CloseEvent -> {
                if(uiState.value.isInitSuccess){
                    checkPageContentEdited {
                        super.handleCommonEvents(CommonEvent.CloseEvent)
                    }
                }else{
                    super.handleCommonEvents(CommonEvent.CloseEvent)
                }
            }
            else -> super.handleCommonEvents(event)
        }
    }

    private fun initializeState() {
        launchWithInit {
            val title = requireNotNull(savedStateHandle.get<String>(ArgName.NAME_BOOK_TITLE))

            // TODO : Load Page Content
            setState {
                copy(
                    isInitSuccess = true,
                    title = title
                )
            }
        }
    }

    // CommonEvent
    private suspend fun loadPageContent(pageId : Long) : Pair<PageWrapper, List<SelectorModel>>{
        val pageWrapper = createPageWrapper(useCaseLoadBook.loadPage(episodeRef, pageId = pageId))
        val selectors = useCaseLoadBook.loadLogic(episodeRef).logics.firstOrNull { it.pageId == pageId }?.selectors

        return if(selectors != null) Pair(pageWrapper, selectors) else Pair(pageWrapper, listOf())
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

    private fun handleOnResume() {
        if (isFirstResumeComplete) {
            launchWithLoading {
                // TODO : Load Page Content
            }
        } else {
            isFirstResumeComplete = true
        }
    }

    private fun checkPageContentEdited(onCheckComplete: () -> Unit) {

        val result = false // TODO : Page Content Edit Check
        if (result) {
            setLoadState(
                LoadState.AlarmDialog(
                    title = StringValue.StringResource(com.fancymansion.core.common.R.string.book_file_edited_info_title),
                    message = StringValue.StringResource(R.string.dialog_save_edited_book_info),
                    confirmText = StringValue.StringResource(com.fancymansion.core.common.R.string.confirm),
                    onConfirm = {
                        //수정 중인 정보 파일 저장
                        launchWithLoading {
                            // TODO : Save Page Content
                            setLoadStateIdle()
                            onCheckComplete()
                        }
                    },
                    dismissText = StringValue.StringResource(com.fancymansion.core.common.R.string.cancel),
                    onDismiss = {
                        //수정 중인 정보 삭제
                        launchWithLoading {
                            // TODO : Update Page Content
                            setLoadStateIdle()
                            onCheckComplete()
                        }
                    }
                )
            )
        } else {
            onCheckComplete()
        }
    }
}
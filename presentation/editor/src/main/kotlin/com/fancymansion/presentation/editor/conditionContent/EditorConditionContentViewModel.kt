package com.fancymansion.presentation.editor.conditionContent

import androidx.lifecycle.SavedStateHandle
import com.fancymansion.core.common.const.ArgName.NAME_BOOK_ID
import com.fancymansion.core.common.const.ArgName.NAME_BOOK_TITLE
import com.fancymansion.core.common.const.ArgName.NAME_EPISODE_ID
import com.fancymansion.core.common.const.ArgName.NAME_PAGE_ID
import com.fancymansion.core.common.const.ArgName.NAME_READ_MODE
import com.fancymansion.core.common.const.ArgName.NAME_ROUTE_ID
import com.fancymansion.core.common.const.ArgName.NAME_SELECTOR_ID
import com.fancymansion.core.common.const.ArgName.NAME_USER_ID
import com.fancymansion.core.common.const.EpisodeRef
import com.fancymansion.core.common.const.ReadMode
import com.fancymansion.core.common.resource.StringValue
import com.fancymansion.core.presentation.base.BaseViewModel
import com.fancymansion.core.presentation.base.CommonEvent
import com.fancymansion.core.presentation.base.LoadState
import com.fancymansion.domain.model.book.LogicModel
import com.fancymansion.domain.usecase.book.UseCaseLoadBook
import com.fancymansion.domain.usecase.book.UseCaseMakeBook
import com.fancymansion.domain.usecase.util.UseCaseGetResource
import com.fancymansion.presentation.editor.R
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class EditorConditionContentViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val useCaseLoadBook: UseCaseLoadBook,
    private val useCaseMakeBook: UseCaseMakeBook,
    private val useCaseGetResource: UseCaseGetResource
) : BaseViewModel<EditorConditionContentContract.State, EditorConditionContentContract.Event, EditorConditionContentContract.Effect>() {

    private var isUpdateResume = false
    private val pageId: Long by lazy { requireNotNull(savedStateHandle.get<Long>(NAME_PAGE_ID)) }
    private val selectorId: Long by lazy { requireNotNull(savedStateHandle.get<Long>(NAME_SELECTOR_ID)) }
    private val routeId: Long by lazy { requireNotNull(savedStateHandle.get<Long>(NAME_ROUTE_ID)) }

    private val episodeRef: EpisodeRef = savedStateHandle.run {
        EpisodeRef(
            requireNotNull(get<String>(NAME_USER_ID)),
            requireNotNull(get<ReadMode>(NAME_READ_MODE)),
            requireNotNull(get<String>(NAME_BOOK_ID)),
            requireNotNull(get<String>(NAME_EPISODE_ID))
        )
    }
    private lateinit var logic: LogicModel

    init {
        initializeState()
    }

    override fun setInitialState() = EditorConditionContentContract.State()

    override fun handleEvents(event: EditorConditionContentContract.Event) {
        when(event) {
            else -> {}
        }
    }

    override fun handleCommonEvents(event: CommonEvent) {
        when(event){
            is CommonEvent.OnResume -> handleOnResume()
            is CommonEvent.CloseEvent -> {
                if (uiState.value.isInitSuccess) {
                    checkConditionEdited {
                        super.handleCommonEvents(CommonEvent.CloseEvent)
                    }
                } else {
                    super.handleCommonEvents(CommonEvent.CloseEvent)
                }
            }
            else -> super.handleCommonEvents(event)
        }
    }

    private fun initializeState() {
        launchWithInit {
            updateCondition(pageId, selectorId, routeId)

            val bookTitle = requireNotNull(savedStateHandle.get<String>(NAME_BOOK_TITLE))
            val pageTitle = logic.logics.firstOrNull { it.pageId == pageId }?.title.orEmpty()
            val selectorText = logic.logics.firstOrNull { it.pageId == pageId }?.selectors?.firstOrNull { it.selectorId == selectorId }?.text.orEmpty()

            setState {
                copy(
                    isInitSuccess = true,
                    bookTitle = bookTitle,
                    pageTitle = pageTitle,
                    selectorText = selectorText
                )
            }
        }
    }

    // CommonEvent
    private suspend fun updateCondition(pageId: Long, selectorId: Long, routeId: Long) {
        logic = useCaseLoadBook.loadLogic(episodeRef)
        // TODO 06.12 get condition and setState
    }

    private suspend fun saveEditedConditionAndReload() : Boolean{
        // TODO 06.12 save condition
        updateCondition(pageId, selectorId, routeId)
        return true
    }

    private fun handleOnResume() {
        if (isUpdateResume) {
            isUpdateResume = false
            launchWithLoading { updateCondition(pageId, selectorId, routeId) }
        }
    }

    private fun checkConditionEdited(onCheckComplete: () -> Unit) {
        // TODO 06.12 edited condition to model
        if (false) {
            setLoadState(
                LoadState.AlarmDialog(
                    title = StringValue.StringResource(com.fancymansion.core.common.R.string.book_file_edited_info_title),
                    message = StringValue.StringResource(R.string.dialog_save_edited_book_info),
                    confirmText = StringValue.StringResource(com.fancymansion.core.common.R.string.confirm),
                    onConfirm = {
                        //수정 중인 정보 파일 저장
                        launchWithLoading {
                            saveEditedConditionAndReload()
                            setLoadStateIdle()
                            onCheckComplete()
                        }
                    },
                    dismissText = StringValue.StringResource(com.fancymansion.core.common.R.string.cancel),
                    onDismiss = {
                        //수정 중인 정보 삭제
                        launchWithLoading {
                            updateCondition(pageId, selectorId, routeId)
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
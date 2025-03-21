package com.fancymansion.presentation.editor.selectorList

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.SavedStateHandle
import com.fancymansion.core.common.const.ArgName.NAME_BOOK_ID
import com.fancymansion.core.common.const.ArgName.NAME_BOOK_TITLE
import com.fancymansion.core.common.const.ArgName.NAME_EPISODE_ID
import com.fancymansion.core.common.const.ArgName.NAME_IS_SELECTOR_LIST_EDIT_MODE
import com.fancymansion.core.common.const.ArgName.NAME_READ_MODE
import com.fancymansion.core.common.const.ArgName.NAME_USER_ID
import com.fancymansion.core.common.const.EpisodeRef
import com.fancymansion.core.common.const.ReadMode
import com.fancymansion.core.common.resource.StringValue
import com.fancymansion.core.common.util.BookIDManager
import com.fancymansion.core.presentation.base.BaseViewModel
import com.fancymansion.core.presentation.base.CommonEvent
import com.fancymansion.core.presentation.base.LoadState
import com.fancymansion.domain.model.book.SelectorModel
import com.fancymansion.domain.usecase.book.UseCaseLoadBook
import com.fancymansion.domain.usecase.book.UseCaseMakeBook
import com.fancymansion.domain.usecase.util.UseCaseGetResource
import com.fancymansion.presentation.editor.R
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class EditorSelectorListViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val useCaseLoadBook: UseCaseLoadBook,
    private val useCaseMakeBook: UseCaseMakeBook,
    private val useCaseGetResource: UseCaseGetResource
) : BaseViewModel<EditorSelectorListContract.State, EditorSelectorListContract.Event, EditorSelectorListContract.Effect>() {

    private var isUpdateResume = false
    private lateinit var selectors: List<SelectorModel>
    val selectorStates: SnapshotStateList<SelectorState> = mutableStateListOf()
    private val totalDeleteSelectorIds: MutableSet<Long> = mutableSetOf()

    private val episodeRef: EpisodeRef = savedStateHandle.run {
        EpisodeRef(
            requireNotNull(get<String>(NAME_USER_ID)),
            requireNotNull(get<ReadMode>(NAME_READ_MODE)),
            requireNotNull(get<String>(NAME_BOOK_ID)),
            requireNotNull(get<String>(NAME_EPISODE_ID))
        )
    }

    init {
        initializeState()
    }

    override fun setInitialState() = EditorSelectorListContract.State()

    override fun handleEvents(event: EditorSelectorListContract.Event) {
        when (event) {
            EditorSelectorListContract.Event.SelectorSaveToFile -> handleSelectorListSaveToFile()
            EditorSelectorListContract.Event.SelectorListModeChangeButtonClicked -> toggleEditMode()
            // Sort Order
            EditorSelectorListContract.Event.SelectorSortOrderLastEdited -> sortSelectorsByLastEdited()
            EditorSelectorListContract.Event.SelectorSortOrderTextAscending -> sortSelectorsByTextAscending()
            // Edit Header
            EditorSelectorListContract.Event.SelectAllHolders -> selectAllHolders()
            EditorSelectorListContract.Event.DeselectAllHolders -> deselectAllHolders()
            EditorSelectorListContract.Event.AddSelectorButtonClicked -> addNewSelector()
            EditorSelectorListContract.Event.DeleteSelectedHolders -> deleteSelectedSelectors()
            // Holder Event
            is EditorSelectorListContract.Event.SelectorHolderNavigateClicked -> navigateToSelectorContent(event.selectorId)
            is EditorSelectorListContract.Event.SelectorHolderSelectClicked -> toggleSelectorSelection(event.selectorId)
            is EditorSelectorListContract.Event.MoveHolderPosition -> moveSelector(event.fromIndex, event.toIndex)
        }
    }

    override fun handleCommonEvents(event: CommonEvent) {
        when(event){
            is CommonEvent.OnResume -> handleOnResume()
            is CommonEvent.CloseEvent -> {
                if(uiState.value.isInitSuccess){
                    checkSelectorListEdited {
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
            val title = requireNotNull(savedStateHandle.get<String>(NAME_BOOK_TITLE))
            val isEditMode = requireNotNull(savedStateHandle.get<Boolean>(NAME_IS_SELECTOR_LIST_EDIT_MODE))

            updateLogicAndStateList()
            setState {
                copy(
                    isInitSuccess = true,
                    title = title,
                    isEditMode = isEditMode
                )
            }
        }
    }

    private fun handleSelectorListSaveToFile() = launchWithLoading(endLoadState = null) {
        val isComplete = saveEditedPageListAndReload()
        setLoadState(
            loadState = LoadState.AlarmDialog(
                title = StringValue.StringResource(com.fancymansion.core.common.R.string.book_file_save_result_title),
                message = StringValue.StringResource(if (isComplete) R.string.dialog_save_complete_book_info else R.string.dialog_save_fail_book_info),
                dismissText = null,
                confirmText = StringValue.StringResource(com.fancymansion.core.common.R.string.confirm),
                onConfirm = ::setLoadStateIdle
            )
        )
    }

    private fun toggleEditMode() {
        if (uiState.value.isEditMode) {
            deselectAllHolders()
        } else {
            selectorStates.sortBy { it.editIndex }
            setState { copy(selectorSortOrder = SelectorSortOrder.LAST_EDITED) }
        }
        setState { copy(isEditMode = !isEditMode) }
    }

    // Sort Order
    private fun sortSelectorsByLastEdited() = launchWithLoading {
        selectorStates.sortBy { it.editIndex }
        setState { copy(selectorSortOrder = SelectorSortOrder.LAST_EDITED) }
    }

    private fun sortSelectorsByTextAscending() = launchWithLoading {
        updatePageListEditIndex()
        selectorStates.sortBy { it.selector.text }
        setState { copy(selectorSortOrder = SelectorSortOrder.TEXT_ASCENDING) }
    }

    // Edit Header
    private fun selectAllHolders() {
        selectorStates.forEach { it.selected.value = true }
    }

    private fun deselectAllHolders() {
        selectorStates.forEach { it.selected.value = false }
    }

    private fun addNewSelector() = launchWithLoading {
        val editedIndex = selectorStates.size
        val pageId = BookIDManager.generateId(selectorStates.map { it.selector.pageId })
        // TODO EditorSelectorList
        val selectorId = 0L
        val title = useCaseGetResource.string(R.string.edit_page_list_new_page_title_default)
        selectorStates.add(SelectorState(editedIndex, SelectorModel(pageId = pageId, selectorId = selectorId, text = title), mutableStateOf(false)))
    }

    private fun deleteSelectedSelectors() {
        totalDeleteSelectorIds.addAll(selectorStates.filter { it.selected.value }.map { it.selector.pageId })
        selectorStates.removeIf { it.selected.value }
    }

    // Holder Event
    private fun navigateToSelectorContent(pageId: Long) {
        checkSelectorListEdited {
            isUpdateResume = true
            setEffect {
                EditorSelectorListContract.Effect.Navigation.NavigateEditorSelectorContentScreen(
                    episodeRef = episodeRef,
                    bookTitle = uiState.value.title,
                    pageId = pageId,
                    selectorId = 0L // TODO EditorSelectorList
                )
            }
        }
    }

    private fun toggleSelectorSelection(pageId: Long) {
        selectorStates.firstOrNull { it.selector.pageId == pageId }?.let {
            it.selected.value = !it.selected.value
        }
    }

    private fun moveSelector(fromIndex: Int, toIndex: Int) {
        selectorStates.apply {
            val item = removeAt(fromIndex)
            add(toIndex, item)
        }
    }

    // CommonEvent
    private fun handleOnResume() {
        if (isUpdateResume) {
            isUpdateResume = false
            launchWithLoading { updateLogicAndStateList() }
        }
    }

    // Common Function
    private fun updatePageListEditIndex(){
        selectorStates.forEachIndexed { index, pageState ->
            pageState.editIndex = index
        }
    }

    private suspend fun saveEditedPageListAndReload(resetSelect : Boolean = false) : Boolean{
        if (uiState.value.selectorSortOrder == SelectorSortOrder.LAST_EDITED){
            updatePageListEditIndex()
        }
        val editedPageList = selectorStates.sortedBy { it.editIndex }.map { it.selector }

        // TODO EditorSelectorList
        val result = true
        totalDeleteSelectorIds.clear()
        updateLogicAndStateList(resetSelect = resetSelect)
        return result
    }

    private suspend fun updateLogicAndStateList(resetSelect : Boolean = false){
        // TODO EditorSelectorList
        selectors = useCaseLoadBook.loadLogic(episodeRef).logics.getOrNull(0)?.selectors ?: listOf()

        // 기존 유저 선택 항목 반영
        val selectedPageIds = selectorStates.filter { it.selected.value }.map { it.selector.pageId }.toSet()
        selectorStates.clear()
        // TODO EditorSelectorList
        setState {
            copy(
                selectorSortOrder = SelectorSortOrder.LAST_EDITED
            )
        }
    }

    private fun checkSelectorListEdited(onCheckComplete: () -> Unit) {
        // 편집 중인 목록 저장
        if (uiState.value.selectorSortOrder == SelectorSortOrder.LAST_EDITED){
            updatePageListEditIndex()
        }

        // 정렬 순서와 상관 없이 편집순 목록 변환
        val editedLogics = selectorStates.sortedBy { it.editIndex }.map { it.selector }

        if (editedLogics != selectors) {
            setLoadState(
                LoadState.AlarmDialog(
                    title = StringValue.StringResource(com.fancymansion.core.common.R.string.book_file_edited_info_title),
                    message = StringValue.StringResource(R.string.dialog_save_edited_book_info),
                    confirmText = StringValue.StringResource(com.fancymansion.core.common.R.string.confirm),
                    onConfirm = {
                        //수정 중인 정보 파일 저장
                        launchWithLoading {
                            saveEditedPageListAndReload(resetSelect = true)
                            setLoadStateIdle()
                            onCheckComplete()
                        }
                    },
                    dismissText = StringValue.StringResource(com.fancymansion.core.common.R.string.cancel),
                    onDismiss = {
                        //수정 중인 정보 삭제
                        launchWithLoading {
                            updateLogicAndStateList(resetSelect = true)
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
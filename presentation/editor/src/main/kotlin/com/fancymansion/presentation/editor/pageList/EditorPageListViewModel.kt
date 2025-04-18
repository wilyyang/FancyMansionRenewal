package com.fancymansion.presentation.editor.pageList

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.SavedStateHandle
import com.fancymansion.core.common.const.ArgName.NAME_BOOK_ID
import com.fancymansion.core.common.const.ArgName.NAME_BOOK_TITLE
import com.fancymansion.core.common.const.ArgName.NAME_EPISODE_ID
import com.fancymansion.core.common.const.ArgName.NAME_IS_PAGE_LIST_EDIT_MODE
import com.fancymansion.core.common.const.ArgName.NAME_READ_MODE
import com.fancymansion.core.common.const.ArgName.NAME_USER_ID
import com.fancymansion.core.common.const.EpisodeRef
import com.fancymansion.core.common.const.ReadMode
import com.fancymansion.core.common.resource.StringValue
import com.fancymansion.core.common.util.BookIDManager
import com.fancymansion.core.presentation.base.BaseViewModel
import com.fancymansion.core.presentation.base.CommonEvent
import com.fancymansion.core.presentation.base.LoadState
import com.fancymansion.domain.model.book.PageLogicModel
import com.fancymansion.domain.usecase.book.UseCaseLoadBook
import com.fancymansion.domain.usecase.book.UseCaseMakeBook
import com.fancymansion.domain.usecase.util.UseCaseGetResource
import com.fancymansion.presentation.editor.R
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class EditorPageListViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val useCaseLoadBook: UseCaseLoadBook,
    private val useCaseMakeBook: UseCaseMakeBook,
    private val useCaseGetResource: UseCaseGetResource
) : BaseViewModel<EditorPageListContract.State, EditorPageListContract.Event, EditorPageListContract.Effect>() {

    private var isUpdateResume = false
    private lateinit var logics: List<PageLogicModel>
    val pageLogicStates: SnapshotStateList<PageLogicState> = mutableStateListOf()
    private val totalDeletePageIds: MutableSet<Long> = mutableSetOf()

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

    override fun setInitialState() = EditorPageListContract.State()

    override fun handleEvents(event: EditorPageListContract.Event) {
        when (event) {
            EditorPageListContract.Event.PageListSaveToFile -> handlePageListSaveToFile()
            EditorPageListContract.Event.PageListModeChangeButtonClicked -> toggleEditMode()
            // Sort Order
            EditorPageListContract.Event.PageSortOrderLastEdited -> sortPagesByLastEdited()
            EditorPageListContract.Event.PageSortOrderTitleAscending -> sortPagesByTitleAscending()
            // Edit Header
            EditorPageListContract.Event.SelectAllHolders -> selectAllHolders()
            EditorPageListContract.Event.DeselectAllHolders -> deselectAllHolders()
            EditorPageListContract.Event.AddPageButtonClicked -> addNewPage()
            EditorPageListContract.Event.DeleteSelectedHolders -> deleteSelectedPages()
            // Holder Event
            is EditorPageListContract.Event.PageHolderNavigateClicked -> navigateToPageContent(event.pageId)
            is EditorPageListContract.Event.PageHolderSelectClicked -> togglePageSelection(event.pageId)
            is EditorPageListContract.Event.MoveHolderPosition -> movePage(event.fromIndex, event.toIndex)
        }
    }

    override fun handleCommonEvents(event: CommonEvent) {
        when(event){
            is CommonEvent.OnResume -> handleOnResume()
            is CommonEvent.CloseEvent -> {
                if(uiState.value.isInitSuccess){
                    checkPageListEdited {
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
            val isEditMode = requireNotNull(savedStateHandle.get<Boolean>(NAME_IS_PAGE_LIST_EDIT_MODE))

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

    private fun handlePageListSaveToFile() = launchWithLoading(endLoadState = null) {
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
            pageLogicStates.sortBy { it.editIndex }
            setState { copy(pageSortOrder = PageSortOrder.LAST_EDITED) }
        }
        setState { copy(isEditMode = !isEditMode) }
    }

    // Sort Order
    private fun sortPagesByLastEdited() = launchWithLoading {
        pageLogicStates.sortBy { it.editIndex }
        setState { copy(pageSortOrder = PageSortOrder.LAST_EDITED) }
    }

    private fun sortPagesByTitleAscending() = launchWithLoading {
        updatePageListEditIndex()
        pageLogicStates.sortBy { it.pageLogic.title }
        setState { copy(pageSortOrder = PageSortOrder.TITLE_ASCENDING) }
    }

    // Edit Header
    private fun selectAllHolders() {
        pageLogicStates.forEach { it.selected.value = true }
    }

    private fun deselectAllHolders() {
        pageLogicStates.forEach { it.selected.value = false }
    }

    private fun addNewPage() = launchWithLoading {
        val editedIndex = pageLogicStates.size
        val pageId = BookIDManager.generateId(pageLogicStates.map { it.pageLogic.pageId })
        val title = useCaseGetResource.string(R.string.edit_page_list_new_page_title_default)
        pageLogicStates.add(PageLogicState(editedIndex, PageLogicModel(pageId = pageId, title = title), mutableStateOf(false)))
    }

    private fun deleteSelectedPages() {
        totalDeletePageIds.addAll(pageLogicStates.filter { it.selected.value }.map { it.pageLogic.pageId })
        pageLogicStates.removeIf { it.selected.value }
    }

    // Holder Event
    private fun navigateToPageContent(pageId: Long) {
        checkPageListEdited {
            isUpdateResume = true
            setEffect {
                EditorPageListContract.Effect.Navigation.NavigateEditorPageContentScreen(
                    episodeRef = episodeRef,
                    bookTitle = uiState.value.title,
                    pageId = pageId
                )
            }
        }
    }

    private fun togglePageSelection(pageId: Long) {
        pageLogicStates.firstOrNull { it.pageLogic.pageId == pageId }?.let {
            it.selected.value = !it.selected.value
        }
    }

    private fun movePage(fromIndex: Int, toIndex: Int) {
        pageLogicStates.apply {
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
        pageLogicStates.forEachIndexed { index, pageState ->
            pageState.editIndex = index
        }
    }

    private suspend fun saveEditedPageListAndReload(resetSelect : Boolean = false) : Boolean{
        if (uiState.value.pageSortOrder == PageSortOrder.LAST_EDITED){
            updatePageListEditIndex()
        }
        val editedPageList = pageLogicStates.sortedBy { it.editIndex }.map { it.pageLogic }
        val result = useCaseMakeBook.saveEditedPageList(
            episodeRef = episodeRef,
            editedPageList = editedPageList,
            deleteIds = totalDeletePageIds
        )
        totalDeletePageIds.clear()
        updateLogicAndStateList(resetSelect = resetSelect)
        return result
    }

    private suspend fun updateLogicAndStateList(resetSelect : Boolean = false){
        logics = useCaseLoadBook.loadLogic(episodeRef).logics

        // 기존 유저 선택 항목 반영
        val selectedPageIds = pageLogicStates.filter { it.selected.value }.map { it.pageLogic.pageId }.toSet()
        pageLogicStates.clear()
        logics.forEachIndexed { index, pageState ->
            val selected = !resetSelect && pageState.pageId in selectedPageIds
            pageLogicStates.add(
                PageLogicState(
                    editIndex = index, pageLogic = pageState,
                    selected = mutableStateOf(selected)
                )
            )
        }
        setState {
            copy(
                pageSortOrder = PageSortOrder.LAST_EDITED
            )
        }
    }

    private fun checkPageListEdited(onCheckComplete: () -> Unit) {
        // 편집 중인 목록 저장
        if (uiState.value.pageSortOrder == PageSortOrder.LAST_EDITED){
            updatePageListEditIndex()
        }

        // 정렬 순서와 상관 없이 편집순 목록 변환
        val editedLogics = pageLogicStates.sortedBy { it.editIndex }.map { it.pageLogic }

        if (editedLogics != logics) {
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
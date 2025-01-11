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
    savedStateHandle: SavedStateHandle,
    private val useCaseLoadBook: UseCaseLoadBook,
    private val useCaseMakeBook: UseCaseMakeBook,
    private val useCaseGetResource: UseCaseGetResource
) : BaseViewModel<EditorPageListContract.State, EditorPageListContract.Event, EditorPageListContract.Effect>() {

    private lateinit var logics : List<PageLogicModel>
    val pageLogicStates: SnapshotStateList<PageLogicState> = mutableStateListOf<PageLogicState>()

    private var episodeRef : EpisodeRef = savedStateHandle.run {
        EpisodeRef(
            get<String>(NAME_USER_ID)!!,
            get<ReadMode>(NAME_READ_MODE)!!,
            get<String>(NAME_BOOK_ID)!!,
            get<String>(NAME_EPISODE_ID)!!
        )
    }

    override fun setInitialState() = EditorPageListContract.State()

    override fun handleEvents(event: EditorPageListContract.Event) {
        when (event) {
            EditorPageListContract.Event.PageListSaveToFile -> {
                launchWithLoading {
                    saveEditedPageListAndReload()
                }
            }

            EditorPageListContract.Event.PageListModeChangeButtonClicked -> {
                if(uiState.value.isEditMode){
                    // 편집 완료 전 작업 : 모두 선택 해제
                    pageLogicStates.forEach {
                        it.selected.value = false
                    }
                }else{
                    // 편집 시작 전 작업 : 편집순 정렬로 할당
                    setState {
                        copy(
                            pageSortOrder = PageSortOrder.LAST_EDITED
                        )
                    }
                }

                setState {
                    copy(
                        isEditMode = !isEditMode
                    )
                }
            }

            // Sort Order
            EditorPageListContract.Event.PageSortOrderLastEdited -> {
                launchWithLoading {
                    pageLogicStates.sortBy { it.editIndex }
                    setState {
                        copy(
                            pageSortOrder = PageSortOrder.LAST_EDITED
                        )
                    }
                }
            }

            EditorPageListContract.Event.PageSortOrderTitleAscending -> {
                launchWithLoading {
                    pageLogicStates.forEachIndexed { index, pageState ->
                        pageState.editIndex = index
                    }
                    pageLogicStates.sortBy { it.pageLogic.title }
                    setState {
                        copy(
                            pageSortOrder = PageSortOrder.TITLE_ASCENDING
                        )
                    }
                }
            }

            // Select Header
            EditorPageListContract.Event.SelectAllHolders -> {
                pageLogicStates.forEach {
                    it.selected.value = true
                }
            }

            EditorPageListContract.Event.DeselectAllHolders -> {
                pageLogicStates.forEach {
                    it.selected.value = false
                }
            }

            EditorPageListContract.Event.AddPageButtonClicked -> {
                launchWithLoading {
                    val editedIndex = pageLogicStates.size
                    val pageId = BookIDManager.generateId(pageLogicStates.map { it.pageLogic.pageId })
                    val title = useCaseGetResource.string(R.string.edit_page_list_new_page_title_default)
                    pageLogicStates.add(PageLogicState(editedIndex, PageLogicModel(pageId = pageId, title = title), mutableStateOf(false)))
                }
            }

            EditorPageListContract.Event.DeleteSelectedHolders -> {
                pageLogicStates.removeIf { it.selected.value }
            }

            // Holder Event
            is EditorPageListContract.Event.PageHolderNavigateClicked -> {
                checkPageListEdited {
                    setEffect {
                        EditorPageListContract.Effect.Navigation.NavigateEditorPageContentScreen(
                            episodeRef = episodeRef,
                            bookTitle = uiState.value.title,
                            pageId = event.pageId
                        )
                    }
                }
            }

            is EditorPageListContract.Event.PageHolderSelectClicked -> {
                pageLogicStates.firstOrNull {
                    it.pageLogic.pageId == event.pageId
                }?.let {
                    it.selected.value = !it.selected.value
                }
            }

            is EditorPageListContract.Event.MoveHolderPosition -> {
                pageLogicStates.apply {
                    val item = removeAt(event.fromIndex)
                    add(event.toIndex, item)
                }
            }
        }
    }

    init {
        launchWithInit {
            val title = savedStateHandle.get<String>(NAME_BOOK_TITLE)!!
            val isEditMode = savedStateHandle.get<Boolean>(NAME_IS_PAGE_LIST_EDIT_MODE)!!

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

    private suspend fun saveEditedPageListAndReload(resetSelect : Boolean = false){
        useCaseMakeBook.saveEditedPageList(
            episodeRef = episodeRef,
            editedPageList = pageLogicStates.map { it.pageLogic })

        updateLogicAndStateList(resetSelect = resetSelect)
    }

    private suspend fun updateLogicAndStateList(resetSelect : Boolean = false){
        logics = useCaseLoadBook.loadLogic(episodeRef).logics

        // 기존 유저 선택 항목 반영
        val selectedPageIds = pageLogicStates.filter { it.selected.value }.map { it.pageLogic.pageId }.toSet()
        pageLogicStates.clear()
        logics.forEachIndexed { index, pageState ->
            val selected = if (resetSelect) false else { pageState.pageId in selectedPageIds }
            pageLogicStates.add(
                PageLogicState(
                    editIndex = index, pageLogic = pageState,
                    selected = mutableStateOf(selected)
                )
            )
        }
    }

    private fun checkPageListEdited(onCheckComplete: () -> Unit) {
        val editedLogics = pageLogicStates.map { it.pageLogic }
        if (editedLogics != logics) {
            setLoadState(
                loadState = LoadState.AlarmDialog(
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
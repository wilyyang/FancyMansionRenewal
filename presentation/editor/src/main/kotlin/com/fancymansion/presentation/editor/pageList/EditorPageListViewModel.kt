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
import com.fancymansion.core.presentation.base.BaseViewModel
import com.fancymansion.domain.usecase.book.UseCaseLoadBook
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class EditorPageListViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val useCaseLoadBook: UseCaseLoadBook
) : BaseViewModel<EditorPageListContract.State, EditorPageListContract.Event, EditorPageListContract.Effect>() {

    private var episodeRef : EpisodeRef = savedStateHandle.run {
        EpisodeRef(
            get<String>(NAME_USER_ID)!!,
            get<ReadMode>(NAME_READ_MODE)!!,
            get<String>(NAME_BOOK_ID)!!,
            get<String>(NAME_EPISODE_ID)!!
        )
    }

    val pageLogicStates: SnapshotStateList<PageLogicState> = mutableStateListOf<PageLogicState>()

    override fun setInitialState() = EditorPageListContract.State()

    override fun handleEvents(event: EditorPageListContract.Event) {
        when (event) {
            EditorPageListContract.Event.PageListModeChangeButtonClicked -> {
                setState {
                    copy(
                        isEditMode = !isEditMode
                    )
                }
            }

            is EditorPageListContract.Event.MoveHolderPosition -> {
                pageLogicStates.apply {
                    val item = removeAt(event.fromIndex)
                    add(event.toIndex, item)
                }
            }

            is EditorPageListContract.Event.PageHolderNavigateClicked -> {
                // TODO
            }
            is EditorPageListContract.Event.PageHolderSelectClicked -> {
                pageLogicStates.firstOrNull {
                    it.pageLogic.pageId == event.pageId
                }?.let {
                    it.selected.value = !it.selected.value
                }
            }
        }
    }

    init {
        launchWithInit {
            val title = savedStateHandle.get<String>(NAME_BOOK_TITLE)!!
            val isEditMode = savedStateHandle.get<Boolean>(NAME_IS_PAGE_LIST_EDIT_MODE)!!
            val logic = useCaseLoadBook.loadLogic(episodeRef)

            logic.logics.forEach { pageLogic ->
                pageLogicStates.add(PageLogicState(pageLogic = pageLogic, selected = mutableStateOf(false)))
            }

            setState {
                copy(
                    title = title,
                    logic = logic,
                    isEditMode = isEditMode
                )
            }
        }
    }
}
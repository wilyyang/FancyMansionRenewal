package com.fancymansion.presentation.editor.selectorList

import androidx.compose.runtime.MutableState
import com.fancymansion.core.common.const.EpisodeRef
import com.fancymansion.core.presentation.base.ViewEvent
import com.fancymansion.core.presentation.base.ViewSideEffect
import com.fancymansion.core.presentation.base.ViewState
import com.fancymansion.domain.model.book.SelectorModel

enum class SelectorSortOrder {
    LAST_EDITED,
    TEXT_ASCENDING
}

data class SelectorState(var editIndex : Int, val selector : SelectorModel, val selected: MutableState<Boolean>)

class EditorSelectorListContract {
    companion object {
        const val NAME = "editor_selector_list"
    }

    data class State(
        val isInitSuccess : Boolean = false,
        val bookTitle : String = "",
        val pageTitle : String = "",
        val isEditMode : Boolean = false,
        val selectorSortOrder: SelectorSortOrder = SelectorSortOrder.LAST_EDITED
    ) : ViewState

    sealed class Event : ViewEvent {
        data object SelectorSaveToFile : Event()

        data object SelectorListModeChangeButtonClicked : Event()

        // Sort Order
        data object SelectorSortOrderLastEdited : Event()
        data object SelectorSortOrderTextAscending : Event()

        // Edit Header
        data object SelectAllHolders : Event()
        data object DeselectAllHolders : Event()
        data object AddSelectorButtonClicked : Event()
        data object DeleteSelectedHolders : Event()

        // Holder Event
        data class SelectorHolderNavigateClicked(val selectorId : Long) : Event()
        data class SelectorHolderSelectClicked(val selectorId : Long) : Event()
        data class MoveHolderPosition(val fromIndex: Int, val toIndex: Int) : Event()
    }

    sealed class Effect : ViewSideEffect {
        sealed class Navigation : Effect() {
            data class NavigateEditorSelectorContentScreen(val episodeRef : EpisodeRef, val bookTitle : String, val pageId : Long, val selectorId: Long) :
                Navigation()
        }
    }
}
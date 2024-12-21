package com.fancymansion.presentation.editor.pageList

import com.fancymansion.core.common.const.EpisodeRef
import com.fancymansion.core.common.const.LOGIC_ID_NOT_ASSIGNED
import com.fancymansion.core.presentation.base.ViewEvent
import com.fancymansion.core.presentation.base.ViewSideEffect
import com.fancymansion.core.presentation.base.ViewState
import com.fancymansion.domain.model.book.LogicModel

class EditorPageListContract {
    companion object {
        const val NAME = "editor_page_list"
    }

    data class State(
        val logic : LogicModel = LogicModel(id = LOGIC_ID_NOT_ASSIGNED),
        val isEditMode : Boolean = false
    ) : ViewState

    sealed class Event : ViewEvent {
        data class EditorPageContentButtonClicked(val pageId : Long) : Event()
    }

    sealed class Effect : ViewSideEffect {
        sealed class Navigation : Effect() {
            data class NavigateEditorPageContentScreen(val episodeRef : EpisodeRef, val bookTitle : String, val pageId : Long) :
                Navigation()
        }
    }
}
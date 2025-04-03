package com.fancymansion.presentation.editor.selectorContent

import com.fancymansion.core.presentation.base.ViewEvent
import com.fancymansion.core.presentation.base.ViewSideEffect
import com.fancymansion.core.presentation.base.ViewState

class EditorSelectorContentContract {
    companion object {
        const val NAME = "editor_selector_content"
    }

    data class State(
        val isInitSuccess : Boolean = false,
        val bookTitle : String = "",
        val pageTitle : String = "",
        val selectorText : String = ""
    ) : ViewState

    sealed class Event : ViewEvent {
        data object SelectorSaveToFile : Event()

        data class EditSelectorContentText(val text : String) : Event()
    }

    sealed class Effect : ViewSideEffect {
        sealed class Navigation : Effect()
    }
}
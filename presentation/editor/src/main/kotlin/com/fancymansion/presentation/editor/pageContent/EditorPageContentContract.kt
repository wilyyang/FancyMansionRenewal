package com.fancymansion.presentation.editor.pageContent

import com.fancymansion.core.presentation.base.ViewEvent
import com.fancymansion.core.presentation.base.ViewSideEffect
import com.fancymansion.core.presentation.base.ViewState

class EditorPageContentContract {
    companion object {
        const val NAME = "editor_page_content"
    }

    data class State(
        val isInitSuccess : Boolean = false,
        val title : String = ""
    ) : ViewState

    sealed class Event : ViewEvent

    sealed class Effect : ViewSideEffect {
        sealed class Navigation : Effect()
    }
}
package com.fancymansion.presentation.editor.routeContent

import com.fancymansion.core.presentation.base.ViewEvent
import com.fancymansion.core.presentation.base.ViewSideEffect
import com.fancymansion.core.presentation.base.ViewState

class EditorRouteContentContract {
    companion object {
        const val NAME = "editor_route_content"
    }

    data class State(
        val isInitSuccess : Boolean = false,
        val bookTitle : String = "",
        val pageTitle : String = "",
        val selectorText : String = ""
    ) : ViewState

    sealed class Event : ViewEvent

    sealed class Effect : ViewSideEffect {
        sealed class Navigation : Effect()
    }
}
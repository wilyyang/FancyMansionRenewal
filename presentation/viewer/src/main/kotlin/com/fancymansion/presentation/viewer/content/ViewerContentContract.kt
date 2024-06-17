package com.fancymansion.presentation.viewer.content

import com.fancymansion.core.presentation.base.ViewEvent
import com.fancymansion.core.presentation.base.ViewSideEffect
import com.fancymansion.core.presentation.base.ViewState

class ViewerContentContract {
    data class State(val default : Boolean = true) : ViewState
    sealed class Event : ViewEvent {

    }

    sealed class Effect : ViewSideEffect {
        sealed class Navigation : Effect() {
            object TestNavigation : Navigation()

        }
    }
}

object Navigation {
    object Routes {
        const val VIEWER_CONTENT = "viewer_content"
    }
}
package com.fancymansion.presentation.editor.routeContent

import com.fancymansion.core.common.const.PAGE_ID_NOT_ASSIGNED
import com.fancymansion.core.presentation.base.ViewEvent
import com.fancymansion.core.presentation.base.ViewSideEffect
import com.fancymansion.core.presentation.base.ViewState

data class TargetPageWrapper(
    val pageId: Long = PAGE_ID_NOT_ASSIGNED,
    val pageTitle: String  = ""
)

class EditorRouteContentContract {
    companion object {
        const val NAME = "editor_route_content"
    }

    data class State(
        val isInitSuccess : Boolean = false,
        val bookTitle : String = "",
        val pageTitle : String = "",
        val selectorText : String = "",
        val targetPageList: List<TargetPageWrapper> = listOf(),
        val targetPage: TargetPageWrapper = TargetPageWrapper()
    ) : ViewState

    sealed class Event : ViewEvent

    sealed class Effect : ViewSideEffect {
        sealed class Navigation : Effect()
    }
}
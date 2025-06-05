package com.fancymansion.presentation.editor.routeContent

import com.fancymansion.core.common.const.EpisodeRef
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

    sealed class Event : ViewEvent {
        data object RouteSaveToFile : Event()

        data class SelectTargetPage(val pageId : Long) : Event()
        data object ReadPagePreviewClicked : Event()

        // Condition Holder Event
        data object AddRouteConditionClicked : Event()
        data class RouteConditionHolderNavigateClicked(val conditionId : Long) : Event()
        data class RouteConditionHolderDeleteClicked(val conditionId : Long) : Event()
        data class MoveRouteConditionHolderPosition(val fromIndex: Int, val toIndex: Int) : Event()
    }

    sealed class Effect : ViewSideEffect {
        sealed class Navigation : Effect(){
            data class NavigateViewerContentScreen(
                val episodeRef: EpisodeRef,
                val bookTitle: String,
                val episodeTitle: String,
                val pageId: Long
            ) : Navigation()

            data class NavigateEditorConditionScreen(
                val episodeRef: EpisodeRef,
                val bookTitle: String,
                val pageTitle: String,
                val pageId: Long,
                val selectorId: Long,
                val routeId: Long,
                val conditionId: Long
            ) : Navigation()
        }
    }
}
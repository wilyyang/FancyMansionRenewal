package com.fancymansion.presentation.main.content

import com.fancymansion.core.common.const.EpisodeRef
import com.fancymansion.core.presentation.base.ViewEvent
import com.fancymansion.core.presentation.base.ViewSideEffect
import com.fancymansion.core.presentation.base.ViewState

class MainContract {
    companion object {
        const val NAME = "main"
    }

    data class State(
        val isInitSuccess : Boolean = false,
    ) : ViewState

    sealed class Event : ViewEvent {
        data class EditorBookHolderClicked(val episodeRef: EpisodeRef) : Event()
    }

    sealed class Effect : ViewSideEffect {
        sealed class Navigation : Effect(){
            data class NavigateEditorBookOverviewScreen(val episodeRef: EpisodeRef) : Navigation()
        }
    }
}
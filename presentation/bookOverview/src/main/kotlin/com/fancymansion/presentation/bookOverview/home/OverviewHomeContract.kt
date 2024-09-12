package com.fancymansion.presentation.bookOverview.home

import com.fancymansion.core.common.const.EpisodeRef
import com.fancymansion.core.presentation.base.ViewEvent
import com.fancymansion.core.presentation.base.ViewSideEffect
import com.fancymansion.core.presentation.base.ViewState

class OverviewHomeContract {
    companion object {
        const val NAME = "overview_home"
    }

    data class State(
        val data: String = ""
    ) : ViewState

    sealed class Event : ViewEvent {
        data object ReadBookButtonClicked : Event()
        data object ReviewMoreButtonClicked : Event()
    }

    sealed class Effect : ViewSideEffect {
        sealed class Navigation : Effect() {
            data class NavigateViewerContentScreen(val episodeRef: EpisodeRef, val bookTitle: String, val episodeTitle: String) : Navigation()
            data class NavigateReviewListScreen(val userId: String, val bookId: String) : Navigation()
        }
    }
}
package com.fancymansion.presentation.bookOverview.home

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

    }

    sealed class Effect : ViewSideEffect {
        sealed class Navigation : Effect()
    }
}
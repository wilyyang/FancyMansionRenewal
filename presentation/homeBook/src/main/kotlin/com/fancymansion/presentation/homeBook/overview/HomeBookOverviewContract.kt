package com.fancymansion.presentation.homeBook.overview

import com.fancymansion.core.presentation.base.ViewEvent
import com.fancymansion.core.presentation.base.ViewSideEffect
import com.fancymansion.core.presentation.base.ViewState
import com.fancymansion.domain.model.homeBook.HomeBookItemModel


class HomeBookOverviewContract {
    companion object {
        const val NAME = "home_book_overview"
    }

    data class State(
        val homeBookInfo : HomeBookItemModel? = null,
        val coverUrl: String? = null
    ) : ViewState

    sealed class Event : ViewEvent {
        data object DownloadBookButtonClicked : Event()
        data object ReviewMoreButtonClicked : Event()
    }

    sealed class Effect : ViewSideEffect {
        sealed class Navigation : Effect()
    }
}
package com.fancymansion.presentation.editor.bookOverview

import com.fancymansion.core.common.const.EpisodeRef
import com.fancymansion.core.presentation.base.ViewEvent
import com.fancymansion.core.presentation.base.ViewSideEffect
import com.fancymansion.core.presentation.base.ViewState
import com.fancymansion.domain.model.book.BookInfoModel

class EditorBookOverviewContract {
    companion object {
        const val NAME = "editor_book_overview"
    }

    data class State(
        val bookInfo : BookInfoModel? = null
    ) : ViewState

    sealed class Event : ViewEvent {
        data object BookOverviewButtonClicked : Event()
    }

    sealed class Effect : ViewSideEffect {
        sealed class Navigation : Effect(){
            data class NavigateOverviewScreen(val episodeRef: EpisodeRef) : Navigation()
        }
    }
}
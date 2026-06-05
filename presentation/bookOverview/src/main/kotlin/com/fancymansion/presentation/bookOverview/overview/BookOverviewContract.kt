package com.fancymansion.presentation.bookOverview.overview

import com.fancymansion.core.common.const.EpisodeRef
import com.fancymansion.core.presentation.base.ViewEvent
import com.fancymansion.core.presentation.base.ViewSideEffect
import com.fancymansion.core.presentation.base.ViewState
import com.fancymansion.domain.model.book.BookInfoModel
import java.io.File

class BookOverviewContract {
    companion object {
        const val NAME = "book_overview"
    }

    data class State(
        val bookInfo : BookInfoModel? = null,
        val coverImageFile : File? = null
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
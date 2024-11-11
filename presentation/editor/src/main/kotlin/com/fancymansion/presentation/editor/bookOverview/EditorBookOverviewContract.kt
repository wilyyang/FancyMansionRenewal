package com.fancymansion.presentation.editor.bookOverview

import android.net.Uri
import com.fancymansion.core.common.const.EpisodeRef
import com.fancymansion.core.presentation.base.ViewEvent
import com.fancymansion.core.presentation.base.ViewSideEffect
import com.fancymansion.core.presentation.base.ViewState
import com.fancymansion.domain.model.book.BookInfoModel
import java.io.File

class EditorBookOverviewContract {
    companion object {
        const val NAME = "editor_book_overview"
    }

    data class State(
        val bookInfo : BookInfoModel? = null,
        val bookCoverFile : File? = null,
        val galleryCoverImageUri : Uri? = null
    ) : ViewState

    sealed class Event : ViewEvent {
        data object BookOverviewButtonClicked : Event()

        /**
         * Gallery
         */
        data object GalleryBookCoverPickerRequest : Event()
        class GalleryBookCoverPickerResult(val imageUri : Uri?) : Event()
    }

    sealed class Effect : ViewSideEffect {

        /**
         * Gallery
         */
        data object GalleryBookCoverPickerEffect : Effect()
        sealed class Navigation : Effect(){
            data class NavigateOverviewScreen(val episodeRef: EpisodeRef) : Navigation()
        }
    }
}
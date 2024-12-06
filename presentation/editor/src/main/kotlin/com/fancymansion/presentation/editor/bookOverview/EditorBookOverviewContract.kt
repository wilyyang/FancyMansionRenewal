package com.fancymansion.presentation.editor.bookOverview

import android.net.Uri
import com.fancymansion.core.common.const.EpisodeRef
import com.fancymansion.core.common.const.ImagePickType
import com.fancymansion.core.presentation.base.ViewEvent
import com.fancymansion.core.presentation.base.ViewSideEffect
import com.fancymansion.core.presentation.base.ViewState
import com.fancymansion.domain.model.book.BookInfoModel
import com.fancymansion.domain.model.book.KeywordModel

data class PageBrief(val id : Long, val title : String)
data class KeywordState(val keyword: KeywordModel, var selected: Boolean)

class EditorBookOverviewContract {
    companion object {
        const val NAME = "editor_book_overview"
    }

    data class State(
        val bookInfo : BookInfoModel? = null,
        val pageBriefList : List<PageBrief> = emptyList(),
        val imagePickType : ImagePickType = ImagePickType.Empty
    ) : ViewState

    sealed class Event : ViewEvent {
        data object BookOverviewButtonClicked : Event()
        data object OverviewInfoSaveToFile : Event()

        /**
         * Edit BookInfo
         */
        data class EditBookInfoTitle(val title : String) : Event()
        data class EditBookInfoDescription(val description : String) : Event()

        /**
         * Navigate Editor Page
         */
        data class EditorPageContentButtonClicked(val pageId: Long) : Event()
        data object EditorPageListClicked : Event()
        data object EditorPageListEditModeClicked : Event()

        /**
         * Gallery
         */
        data object GalleryBookCoverPickerRequest : Event()
        class GalleryBookCoverPickerResult(val imageUri : Uri?) : Event()
        data object CoverImageReset : Event()
    }

    sealed class Effect : ViewSideEffect {

        /**
         * Gallery
         */
        data object GalleryBookCoverPickerEffect : Effect()
        sealed class Navigation : Effect(){
            data class NavigateEditorPageContentScreen(val episodeRef: EpisodeRef, val bookTitle: String, val pageId: Long) : Navigation()
            data class NavigateEditorListScreen(val episodeRef: EpisodeRef, val bookTitle: String, val isEditMode : Boolean) : Navigation()

            data class NavigateOverviewScreen(val episodeRef: EpisodeRef) : Navigation()
        }
    }
}
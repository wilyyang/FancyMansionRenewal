package com.fancymansion.presentation.editor.bookOverview

import android.net.Uri
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.fancymansion.core.common.const.EpisodeRef
import com.fancymansion.core.common.const.ImagePickType
import com.fancymansion.core.common.const.PageType
import com.fancymansion.core.presentation.base.ViewEvent
import com.fancymansion.core.presentation.base.ViewSideEffect
import com.fancymansion.core.presentation.base.ViewState
import com.fancymansion.domain.model.book.BookInfoModel
import com.fancymansion.domain.model.book.EditorModel
import com.fancymansion.domain.model.book.IntroduceModel
import com.fancymansion.domain.model.book.KeywordModel

data class PageBrief(val id : Long, val title : String, val type: PageType, val selectorCount : Int)
data class KeywordState(val keyword: KeywordModel, val selected: MutableState<Boolean>)
fun createKeywordState(keyword: KeywordModel, isSelected: Boolean): KeywordState {
    return KeywordState(keyword = keyword, selected = mutableStateOf(isSelected))
}

class EditorBookOverviewContract {
    companion object {
        const val NAME = "editor_book_overview"
    }

    data class State(
        val isInitSuccess : Boolean = false,
        val isPublished : Boolean = false,
        val bookInfo : BookInfoModel = BookInfoModel(
            id = "",
            editor = EditorModel(),
            introduce = IntroduceModel()
        ),
        val pageBriefList : List<PageBrief> = emptyList(),
        val imagePickType : ImagePickType = ImagePickType.Empty
    ) : ViewState

    sealed class Event : ViewEvent {
        data object BookOverviewButtonClicked : Event()
        data object OverviewInfoSaveToFile : Event()
        data object UploadBookFile : Event()
        data object WithdrawBookFile : Event()

        /**
         * Edit BookInfo
         */
        data class EditBookInfoTitle(val title : String) : Event()
        data class EditBookInfoDescription(val description : String) : Event()
        data class EditBookInfoKeywordState(val keywordId: Long, val requestSelect: Boolean) : Event()

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
            data class NavigateEditorPageListScreen(val episodeRef: EpisodeRef, val bookTitle: String, val isEditMode : Boolean) : Navigation()

            data class NavigateOverviewScreen(val episodeRef: EpisodeRef) : Navigation()
        }
    }
}
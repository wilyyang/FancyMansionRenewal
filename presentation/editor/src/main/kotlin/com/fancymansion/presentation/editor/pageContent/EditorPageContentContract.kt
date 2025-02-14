package com.fancymansion.presentation.editor.pageContent

import android.net.Uri
import androidx.compose.runtime.MutableState
import com.fancymansion.core.common.const.EpisodeRef
import com.fancymansion.core.common.const.ImagePickType
import com.fancymansion.core.presentation.base.ViewEvent
import com.fancymansion.core.presentation.base.ViewSideEffect
import com.fancymansion.core.presentation.base.ViewState
import com.fancymansion.domain.model.book.PageLogicModel

class EditorPageContentContract {
    companion object {
        const val NAME = "editor_page_content"
    }

    data class State(
        val isInitSuccess : Boolean = false,
        val bookTitle : String = "",
        val pageTitle : String = "",
        val pageLogic: PageLogicModel = PageLogicModel(pageId = -1, title = "")
    ) : ViewState

    sealed class Event : ViewEvent {
        data object OnClickSavePageToFile : Event()
        data object ReadPagePreviewClicked : Event()

        data class EditPageContentTitle(val title : String) : Event()
        data class MoveSourcePosition(val fromIndex: Int, val toIndex: Int) : Event()

        data object OnClickAddSource : Event()

        data object AddTextSourceEvent : Event()
        data object AddImageSourceEvent : Event()

        data class OnClickSourceText(val sourceIndex: Int) : Event()
        data class OnClickSourceImage(val sourceIndex: Int) : Event()

        data class OnClickDeleteSource(val sourceIndex: Int) : Event()

        data class EditSourceText(val sourceIndex: Int, val text : String) : Event()
        data class EditSourceImage(val sourceIndex: Int) : Event()

        class GalleryPickerResult(val sourceIndex: Int, val imageUri : Uri?) : Event()
    }

    sealed class Effect : ViewSideEffect {
        sealed class Navigation : Effect() {
            data class NavigateViewerContentScreen(val episodeRef: EpisodeRef, val bookTitle: String, val episodeTitle: String, val pageId: Long) : Navigation()
        }

        data object ShowAddSourceDialogEffect : Effect()

        data class ShowSourceTextEffect(val sourceIndex: Int, val source: SourceWrapper) : Effect()
        data class ShowSourceImageEffect(val sourceIndex: Int, val source: SourceWrapper) : Effect()

        data object GallerySourceImagePickerEffect : Effect()
        data class UpdateSourceImage(val sourceIndex: Int, val source: SourceWrapper) : Effect()
    }
}

sealed class SourceWrapper {
    data class TextWrapper(val description: MutableState<String>) : SourceWrapper()
    data class ImageWrapper(val imagePickType: ImagePickType) : SourceWrapper()
}
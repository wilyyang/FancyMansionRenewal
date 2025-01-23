package com.fancymansion.presentation.editor.pageContent

import com.fancymansion.core.common.const.ImagePickType
import com.fancymansion.core.presentation.base.ViewEvent
import com.fancymansion.core.presentation.base.ViewSideEffect
import com.fancymansion.core.presentation.base.ViewState
import com.fancymansion.domain.model.book.SelectorModel

class EditorPageContentContract {
    companion object {
        const val NAME = "editor_page_content"
    }

    data class State(
        val isInitSuccess : Boolean = false,
        val bookTitle : String = "",
        val pageTitle : String = "",
        val selectors: List<SelectorModel> = listOf()
    ) : ViewState

    sealed class Event : ViewEvent {
        data class EditPageContentTitle(val title : String) : Event()
        data class MoveSourcePosition(val fromIndex: Int, val toIndex: Int) : Event()
    }

    sealed class Effect : ViewSideEffect {
        sealed class Navigation : Effect()
    }
}

sealed class SourceWrapper(open var editIndex : Int) {
    data class TextWrapper(override var editIndex : Int, val description: String) : SourceWrapper(editIndex)
    data class ImageWrapper(override var editIndex : Int, val imagePickType: ImagePickType) : SourceWrapper(editIndex)
}
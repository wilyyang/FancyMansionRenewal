package com.fancymansion.presentation.editor.pageContent

import com.fancymansion.core.presentation.base.ViewEvent
import com.fancymansion.core.presentation.base.ViewSideEffect
import com.fancymansion.core.presentation.base.ViewState
import com.fancymansion.domain.model.book.SelectorModel
import java.io.File

class EditorPageContentContract {
    companion object {
        const val NAME = "editor_page_content"
    }

    data class State(
        val isInitSuccess : Boolean = false,
        val title : String = "",
        val selectors: List<SelectorModel> = listOf()
    ) : ViewState

    sealed class Event : ViewEvent

    sealed class Effect : ViewSideEffect {
        sealed class Navigation : Effect()
    }
}

sealed class SourceWrapper(open var editIndex : Int) {
    data class TextWrapper(override var editIndex : Int, val description: String) : SourceWrapper(editIndex)
    data class ImageWrapper(override var editIndex : Int, val imageFile: File) : SourceWrapper(editIndex)
}
package com.fancymansion.presentation.editor.pageContent

import com.fancymansion.core.presentation.base.ViewEvent
import com.fancymansion.core.presentation.base.ViewSideEffect
import com.fancymansion.core.presentation.base.ViewState
import java.io.File

class EditorPageContentContract {
    companion object {
        const val NAME = "editor_page_content"
    }

    data class State(
        val isInitSuccess : Boolean = false,
        val title : String = ""
    ) : ViewState

    sealed class Event : ViewEvent

    sealed class Effect : ViewSideEffect {
        sealed class Navigation : Effect()
    }
}

data class PageWrapper(
    val id: Long,
    val title: String,
    val sources: List<SourceWrapper>
)

sealed class SourceWrapper {
    data class TextWrapper(val description: String) : SourceWrapper()
    data class ImageWrapper(val imageFile: File) : SourceWrapper()
}
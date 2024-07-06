package com.fancymansion.presentation.viewer.content

import com.fancymansion.core.presentation.base.ViewEvent
import com.fancymansion.core.presentation.base.ViewSideEffect
import com.fancymansion.core.presentation.base.ViewState
import com.fancymansion.domain.model.book.SelectorModel
import java.io.File

class ViewerContentContract {
    companion object {
        const val NAME = "viewer_content"
    }

    data class State(
        val pageWrapper: PageWrapper? = null,
        val selectors: List<SelectorModel> = listOf()
    ) : ViewState

    sealed class Event : ViewEvent {
        data class OnClickSelector(val pageId: Long, val selectorId: Long) : Event()
    }

    sealed class Effect : ViewSideEffect {
        sealed class Navigation : Effect()
    }
}

data class PageWrapper(
    val id: Long,
    val title: String,
    val sources: List<SourceWrapper>
){
    override fun equals(other: Any?): Boolean {
        return false
    }

    override fun hashCode(): Int {
        return System.identityHashCode(this)
    }
}

sealed class SourceWrapper {
    data class TextWrapper(val description: String) : SourceWrapper()
    data class ImageWrapper(val imageFile: File) : SourceWrapper()
}
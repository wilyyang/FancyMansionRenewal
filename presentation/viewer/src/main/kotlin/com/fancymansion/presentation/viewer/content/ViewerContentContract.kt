package com.fancymansion.presentation.viewer.content

import com.fancymansion.core.presentation.base.ViewEvent
import com.fancymansion.core.presentation.base.ViewSideEffect
import com.fancymansion.core.presentation.base.ViewState
import com.fancymansion.domain.model.book.PageModel
import com.fancymansion.domain.model.book.SelectorModel
import com.fancymansion.domain.model.book.SourceModel
import java.io.File

class ViewerContentContract {
    companion object {
        const val NAME = "viewer_content"
    }

    data class State(val pageState: PageState = PageState(), val imageMap : Map<String, File> = mapOf(), val selectors: List<SelectorModel> = listOf()) : ViewState
    sealed class Event : ViewEvent {
        data class OnClickSelector(val pageId: Long, val selectorId: Long) : Event()
    }

    sealed class Effect : ViewSideEffect {
        sealed class Navigation : Effect()
    }
}

data class PageState(
    val page: PageWrapper? = null
) {
    override fun equals(other: Any?): Boolean {
        return false
    }

    override fun hashCode(): Int {
        return System.identityHashCode(this)
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
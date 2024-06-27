package com.fancymansion.presentation.viewer.content

import com.fancymansion.core.presentation.base.ViewEvent
import com.fancymansion.core.presentation.base.ViewSideEffect
import com.fancymansion.core.presentation.base.ViewState
import com.fancymansion.domain.model.book.Page
import com.fancymansion.domain.model.book.Selector

class ViewerContentContract {
    companion object {
        const val NAME = "viewer_content"
    }

    data class State(val page: Page? = null, val selectors: List<Selector> = listOf()) : ViewState
    sealed class Event : ViewEvent {
        data class OnClickSelector(val selectorId: Long) : Event()
    }

    sealed class Effect : ViewSideEffect {
        sealed class Navigation : Effect()
    }
}
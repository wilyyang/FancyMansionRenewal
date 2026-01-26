package com.fancymansion.presentation.main.content

import com.fancymansion.core.presentation.base.ViewEvent
import com.fancymansion.core.presentation.base.ViewSideEffect
import com.fancymansion.core.presentation.base.ViewState
import com.fancymansion.presentation.main.common.MainScreenTab
import com.fancymansion.presentation.main.tab.editor.EditBookState

class MainContract {
    companion object {
        const val NAME = "main"
    }

    data class State(
        val isInitSuccess : Boolean = false,
        val studyBookList: List<EditBookState> = emptyList(),
        val homeBookList: List<EditBookState> = emptyList(),
        val currentTab: MainScreenTab = MainScreenTab.Editor,
    ) : ViewState

    sealed class Event : ViewEvent {
        data class TabSelected(val tab: MainScreenTab) : Event()
    }

    sealed class Effect : ViewSideEffect {
        sealed class Navigation : Effect()
    }
}
package com.fancymansion.presentation.main.content

import com.fancymansion.core.common.const.EpisodeRef
import com.fancymansion.core.common.const.ImagePickType
import com.fancymansion.core.presentation.base.ViewEvent
import com.fancymansion.core.presentation.base.ViewSideEffect
import com.fancymansion.core.presentation.base.ViewState
import com.fancymansion.domain.model.homeBook.HomeBookItemModel
import com.fancymansion.presentation.main.common.MainScreenTab
import com.fancymansion.presentation.main.tab.editor.EditBookState
import com.fancymansion.presentation.main.tab.editor.EditBookWrapper

// 임시
fun HomeBookItemModel.toWrapper(thumbnail: ImagePickType) : EditBookWrapper {
    return EditBookWrapper(
        bookId = book.publishedId,
        title = book.introduce.title,
        editTime = book.publishedAt,
        pageCount = episode.pageCount,
        thumbnail = thumbnail,
        keywords = book.introduce.keywordList
    )
}

class MainContract {
    companion object {
        const val NAME = "main"
    }

    data class State(
        val isInitSuccess : Boolean = false,
        val homeBookList: List<EditBookState> = emptyList(),
        val homeBookUrlList: List<String> = emptyList(),
        val studyBookList: List<EditBookState> = emptyList(),
        val currentTab: MainScreenTab = MainScreenTab.Editor,
    ) : ViewState

    sealed class Event : ViewEvent {
        data class TabSelected(val tab: MainScreenTab) : Event()
        data class HomeBookHolderClicked(val publishedId: String) : Event()
        data class DownloadBookHolderClicked(val bookId: String) : Event()
        data object OnClickLogout : Event()
        data object GoogleLogoutSuccess : Event()
        data class GoogleLogoutFail(val t: Throwable) : Event()
    }

    sealed class Effect : ViewSideEffect {
        sealed class Navigation : Effect(){
            data class NavigateOverviewScreen(val episodeRef: EpisodeRef) : Navigation()
            data object RequestGoogleLogout : Navigation()
            data object NavigateLaunchScreen : Navigation()
        }
    }
}
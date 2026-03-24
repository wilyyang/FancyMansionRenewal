package com.fancymansion.presentation.main.content

import com.fancymansion.core.common.const.ImagePickType
import com.fancymansion.core.presentation.base.ViewEvent
import com.fancymansion.core.presentation.base.ViewSideEffect
import com.fancymansion.core.presentation.base.ViewState
import com.fancymansion.domain.model.book.KeywordModel
import com.fancymansion.domain.model.homeBook.HomeBookItemModel
import com.fancymansion.presentation.main.common.MainScreenTab

// 임시
data class HomeBookState(val bookInfo: HomeBookWrapper)
fun HomeBookItemModel.toWrapper(thumbnail: ImagePickType) : HomeBookWrapper {
    return HomeBookWrapper(
        bookId = book.publishInfo.publishedId,
        title = book.introduce.title,
        editTime = book.publishInfo.updatedAt,
        pageCount = episode.pageCount,
        thumbnail = thumbnail,
        keywords = book.introduce.keywordList
    )
}
data class HomeBookWrapper(
    val bookId: String,
    val title: String,
    val editTime: Long,
    val pageCount: Int,
    val thumbnail: ImagePickType,
    val keywords: List<KeywordModel>
)

class MainContract {
    companion object {
        const val NAME = "main"
    }

    data class State(
        val isInitSuccess : Boolean = false,
        val homeBookList: List<HomeBookState> = emptyList(),
        val homeBookUrlList: List<String> = emptyList(),
        val currentTab: MainScreenTab = MainScreenTab.Editor,
    ) : ViewState

    sealed class Event : ViewEvent {
        data class TabSelected(val tab: MainScreenTab) : Event()
        data class HomeBookHolderClicked(val publishedId: String) : Event()
    }

    sealed class Effect : ViewSideEffect {
        sealed class Navigation : Effect()
    }
}
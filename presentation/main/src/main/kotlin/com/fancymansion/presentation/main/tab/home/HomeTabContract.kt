package com.fancymansion.presentation.main.tab.home

import com.fancymansion.core.common.const.RemoteBookSortOrder
import com.fancymansion.core.presentation.base.ViewEvent
import com.fancymansion.core.presentation.base.ViewSideEffect
import com.fancymansion.core.presentation.base.ViewState
import com.fancymansion.domain.model.book.KeywordModel
import com.fancymansion.domain.model.homeBook.HomeBookItemModel
import com.fancymansion.presentation.main.R

enum class HomeBookSortOrder(val textResId : Int) {
    LAST_UPDATE (textResId = R.string.home_book_sort_order_last_update),
    TITLE_ASCENDING (textResId = R.string.home_book_sort_order_title_ascending)
}

fun HomeBookSortOrder.toRemote(): RemoteBookSortOrder {
    return when (this) {
        HomeBookSortOrder.LAST_UPDATE -> RemoteBookSortOrder.LAST_UPDATE
        HomeBookSortOrder.TITLE_ASCENDING -> RemoteBookSortOrder.TITLE_ASCENDING
    }
}

data class HomeBookWrapper(
    val bookId: String,
    val title: String,
    val updateTime: Long,
    val thumbnailUrl: String,
    val pageCount: Int,
    val keywords: List<KeywordModel>
)

fun HomeBookItemModel.toWrapper(thumbnailUrl: String) : HomeBookWrapper {
    return HomeBookWrapper(
        bookId = book.publishInfo.publishedId,
        title = book.introduce.title,
        updateTime = book.publishInfo.updatedAt,
        thumbnailUrl = thumbnailUrl,
        pageCount = episode.pageCount,
        keywords = book.introduce.keywordList
    )
}

class HomeTabContract {
    companion object {
        const val NAME = "main_tab_home"
    }

    data class State(
        val isInitSuccess : Boolean = false,
        val searchText: String = "",
        val bookSortOrder: HomeBookSortOrder = HomeBookSortOrder.LAST_UPDATE,
        val totalPageCount: Int = 0,
        val currentPage : Int = 0,
        val visibleBookList: List<HomeBookWrapper> = emptyList()
    ) : ViewState

    sealed class Event : ViewEvent {
        data class BookPageNumberClicked(val pageNumber : Int) : Event()
        data class BookHolderClicked(val bookId : String) : Event()

        data class SearchTextInput(val searchText : String) : Event()
        data object SearchClicked : Event()
        data object SearchCancel : Event()

        data class SelectBookSortOrder(val sortOrder: HomeBookSortOrder) : Event()
    }

    sealed class Effect : ViewSideEffect {
        sealed class Navigation : Effect()
    }
}
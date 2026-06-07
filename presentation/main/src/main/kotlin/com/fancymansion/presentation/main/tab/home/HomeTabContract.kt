package com.fancymansion.presentation.main.tab.home

import com.fancymansion.core.common.const.RemoteBookSortOrder
import com.fancymansion.core.common.resource.StringValue
import com.fancymansion.core.presentation.base.ViewEvent
import com.fancymansion.core.presentation.base.ViewSideEffect
import com.fancymansion.core.presentation.base.ViewState
import com.fancymansion.domain.model.book.KeywordModel
import com.fancymansion.domain.model.homeBook.HomeBookItemModel
import com.fancymansion.presentation.main.R
import com.fancymansion.presentation.main.common.BOOKS_PER_PAGE

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

class LoadHomeBookException(
    val title: StringValue.StringResource,
    val text: StringValue.StringResource
): Exception()

data class PageChunkCursor(
    val startPage: Int,
    val bookSize: Int = 0,
    val cursorBookIds: List<String>
) {
    val pageCount: Int
        get() = if (bookSize <= 0) 0 else (bookSize - 1) / BOOKS_PER_PAGE + 1
    val endPage: Int
        get() = startPage + pageCount - 1

    override fun toString() =
        "Chunk[$startPage~$endPage] ($bookSize books / $pageCount pages) cursorIds=${cursorBookIds.size}"
}

class HomeTabContract {
    companion object {
        const val NAME = "main_tab_home"
    }

    data class State(
        val isInitSuccess : Boolean = false,
        val searchText: String = "",
        val bookSortOrder: HomeBookSortOrder = HomeBookSortOrder.LAST_UPDATE,
        val currentPage : Int = 0,
        val startPage: Int = 0,
        val endPage: Int = 0,
        val pageCount: Int = 0,
        val canPrev: Boolean = false,
        val canNext: Boolean = false,
        val visibleBookList: List<HomeBookWrapper> = emptyList()
    ) : ViewState

    sealed class Event : ViewEvent {
        data object UpdateHomeTabEvent : Event()
        data class BookPageNumberClicked(val pageNumber : Int) : Event()
        data class BookHolderClicked(val publishedId : String) : Event()

        data class SearchTextInput(val searchText : String) : Event()
        data object SearchClicked : Event()
        data object SearchCancel : Event()

        data class SelectBookSortOrder(val sortOrder: HomeBookSortOrder) : Event()
    }

    sealed class Effect : ViewSideEffect {
        sealed class Navigation : Effect(){
            data class NavigateHomeBookOverviewScreen(val bookId: String) : Navigation()
        }
    }
}
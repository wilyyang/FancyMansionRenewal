package com.fancymansion.presentation.main.tab.library

import androidx.compose.runtime.MutableState
import com.fancymansion.core.common.const.EpisodeRef
import com.fancymansion.core.common.const.ImagePickType
import com.fancymansion.core.presentation.base.ViewEvent
import com.fancymansion.core.presentation.base.ViewSideEffect
import com.fancymansion.core.presentation.base.ViewState
import com.fancymansion.domain.model.book.BookMetaModel
import com.fancymansion.domain.model.book.KeywordModel
import com.fancymansion.domain.model.book.LocalBookItemModel
import com.fancymansion.presentation.main.R

enum class LibraryBookSortOrder(val textResId : Int) {
    LAST_UPDATE (textResId = R.string.library_book_sort_order_last_update),
    TITLE_ASCENDING (textResId = R.string.library_book_sort_order_title_ascending)
}

data class LibraryBookWrapper(
    val bookId: String,
    val title: String,
    val pageCount: Int,
    val thumbnail: ImagePickType,
    val keywords: List<KeywordModel>,
    val metadata: BookMetaModel
)

data class LibraryBookState(val bookInfo: LibraryBookWrapper, val selected: MutableState<Boolean>)

fun LocalBookItemModel.toWrapper(thumbnail: ImagePickType) : LibraryBookWrapper {
    return LibraryBookWrapper(
        bookId = book.id,
        title = book.introduce.title,
        pageCount = episode.pageCount,
        thumbnail = thumbnail,
        keywords = book.introduce.keywordList,
        metadata = metaData
    )
}

class LibraryTabContract {
    companion object {
        const val NAME = "main_tab_library"
    }

    data class State(
        val isInitSuccess : Boolean = false,
        val isEditMode : Boolean = false,
        val searchText: String = "",
        val bookSortOrder: LibraryBookSortOrder = LibraryBookSortOrder.LAST_UPDATE,
        val totalPageCount: Int = 0,
        val currentPage : Int = 0,
        val visibleBookList: List<LibraryBookState> = emptyList()
    ) : ViewState

    sealed class Event : ViewEvent {

        // Mode Common
        data class BookPageNumberClicked(val pageNumber : Int) : Event()
        data class BookHolderClicked(val bookId : String) : Event()

        // Normal Mode
        data class SearchTextInput(val searchText : String) : Event()
        data object SearchClicked : Event()
        data object SearchCancel : Event()

        data class SelectBookSortOrder(val sortOrder: LibraryBookSortOrder) : Event()

        // Edit Mode
        data object BookListEnterEditMode : Event()
        data object BookListExitEditMode : Event()

        data object BookHolderSelectAll : Event()
        data object BookHolderDeselectAll : Event()

        data object BookHolderDeleteBook : Event()
    }

    sealed class Effect : ViewSideEffect {
        sealed class Navigation : Effect(){
            data class NavigateBookOverviewScreen(val episodeRef: EpisodeRef) : Navigation()
        }
    }
}
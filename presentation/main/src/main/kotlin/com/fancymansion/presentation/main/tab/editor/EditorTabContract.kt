package com.fancymansion.presentation.main.tab.editor

import androidx.compose.runtime.MutableState
import com.fancymansion.core.common.const.EpisodeRef
import com.fancymansion.core.common.const.ImagePickType
import com.fancymansion.core.presentation.base.ViewEvent
import com.fancymansion.core.presentation.base.ViewSideEffect
import com.fancymansion.core.presentation.base.ViewState
import com.fancymansion.domain.model.book.BookInfoModel
import com.fancymansion.domain.model.book.EpisodeInfoModel
import com.fancymansion.domain.model.book.KeywordModel
import com.fancymansion.presentation.main.R

enum class EditBookSortOrder(val textResId : Int) {
    LAST_EDITED (textResId = R.string.edit_book_sort_order_last_edited),
    TITLE_ASCENDING (textResId = R.string.edit_book_sort_order_title_ascending)
}

data class EditBookWrapper(
    val bookId: String,
    val title: String,
    val editTime: Long,
    val pageCount: Int,
    val thumbnail: ImagePickType,
    val keywords: List<KeywordModel>
)

data class EditBookState(val bookInfo: EditBookWrapper, val selected: MutableState<Boolean>)

fun Pair<BookInfoModel, EpisodeInfoModel>.toWrapper(thumbnail: ImagePickType) : EditBookWrapper {
    val bookInfo = first
    val episodeInfo = second

    return EditBookWrapper(
        bookId = bookInfo.id,
        title = bookInfo.introduce.title,
        editTime = episodeInfo.editTime,
        pageCount = episodeInfo.pageCount,
        thumbnail = thumbnail,
        keywords = bookInfo.introduce.keywordList
    )
}

class EditorTabContract {
    companion object {
        const val NAME = "main_tab_editor"
    }

    data class State(
        val isInitSuccess : Boolean = false,
        val searchText: String = "",
        val bookSortOrder: EditBookSortOrder = EditBookSortOrder.LAST_EDITED,
        val isEditMode : Boolean = false,
        val currentPageNumber : Int = 0,
        val totalPageCount: Int = 0,
        val pagedBookList: List<EditBookState> = emptyList()
    ) : ViewState

    sealed class Event : ViewEvent {
        // Search
        data class SearchTextInputUpdate(val searchText : String) : Event()
        data object RequestSearchText : Event()
        data object SearchTextInputCancel : Event()

        // Header
        data class SelectBookSortOrder(val sortOrder: EditBookSortOrder) : Event()

        data object BookListEnterEditMode : Event()
        data object BookListExitEditMode : Event()

        // Holder
        data object BookHolderSelectAll : Event()
        data object BookHolderDeselectAll : Event()
        data object BookHolderAddBook : Event()
        data object BookHolderDeleteBook : Event()

        data class BookHolderEnterEditBook(val bookId : String) : Event()
        data class BookHolderSelectBook(val bookId : String) : Event()

        // Page Number
        data class SelectBookPageNumber(val pageNumber : Int) : Event()
    }

    sealed class Effect : ViewSideEffect {
        sealed class Navigation : Effect(){
            data class NavigateEditorBookOverviewScreen(val episodeRef: EpisodeRef) : Navigation()
        }
    }
}
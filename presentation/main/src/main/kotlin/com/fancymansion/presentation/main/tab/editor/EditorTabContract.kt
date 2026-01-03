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
import com.fancymansion.domain.model.book.LogicModel
import com.fancymansion.domain.model.book.PageModel
import com.fancymansion.presentation.main.R

enum class ListTarget{
    ALL, SEARCH
}

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

private const val NEW_BOOK_DRAFT_INITIAL_LOGIC_ID = 0
private const val NEW_BOOK_DRAFT_START_PAGE_ID = 1
data class NewBookDraft(
    val bookInfo:BookInfoModel,
    val episodeInfo: EpisodeInfoModel,
    val logic: LogicModel,
    val startPage: PageModel
)

class EditorTabContract {
    companion object {
        const val NAME = "main_tab_editor"
    }

    data class State(
        val isInitSuccess : Boolean = false,
        val isEditMode : Boolean = false,
        val searchText: String = "",
        val bookSortOrder: EditBookSortOrder = EditBookSortOrder.LAST_EDITED,
        val totalPageCount: Int = 0,
        val currentPage : Int = 0,
        val visibleBookList: List<EditBookState> = emptyList()
    ) : ViewState

    sealed class Event : ViewEvent {

        // Mode Common
        data class BookPageNumberClicked(val pageNumber : Int) : Event()
        data class BookHolderClicked(val bookId : String) : Event()

        // Normal Mode
        data class SearchTextInput(val searchText : String) : Event()
        data object SearchClicked : Event()
        data object SearchCancel : Event()

        data class SelectBookSortOrder(val sortOrder: EditBookSortOrder) : Event()

        // Edit Mode
        data object BookListEnterEditMode : Event()
        data object BookListExitEditMode : Event()

        data object BookHolderSelectAll : Event()
        data object BookHolderDeselectAll : Event()

        data object BookHolderAddBook : Event()
        data object BookHolderDeleteBook : Event()
    }

    sealed class Effect : ViewSideEffect {
        sealed class Navigation : Effect(){
            data class NavigateEditorBookOverviewScreen(val episodeRef: EpisodeRef) : Navigation()
        }
    }
}
package com.fancymansion.presentation.main.tab.editor

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.SavedStateHandle
import com.fancymansion.core.common.const.ArgName.NAME_USER_ID
import com.fancymansion.core.common.const.EpisodeRef
import com.fancymansion.core.common.const.ImagePickType
import com.fancymansion.core.common.const.ReadMode
import com.fancymansion.core.common.const.testEpisodeRef
import com.fancymansion.core.presentation.base.BaseViewModel
import com.fancymansion.core.presentation.base.CommonEvent
import com.fancymansion.domain.usecase.book.UseCaseBookList
import com.fancymansion.domain.usecase.book.UseCaseLoadBook
import com.fancymansion.domain.usecase.book.UseCaseMakeBook
import com.fancymansion.domain.usecase.util.UseCaseGetResource
import com.fancymansion.presentation.main.tab.editor.EditorTabContract.Event.*
import dagger.hilt.android.lifecycle.HiltViewModel
import java.io.File
import javax.inject.Inject

@HiltViewModel
class EditorTabViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val useCaseLoadBook: UseCaseLoadBook,
    private val useCaseMakeBook: UseCaseMakeBook,
    private val useCaseBookList: UseCaseBookList,
    private val useCaseGetResource: UseCaseGetResource
) : BaseViewModel<EditorTabContract.State, EditorTabContract.Event, EditorTabContract.Effect>() {

    private var isUpdateResume = false
    private var userId: String = savedStateHandle.get<String>(NAME_USER_ID) ?: testEpisodeRef.userId
    private val mode: ReadMode = ReadMode.EDIT
    private lateinit var originBookInfoList: List<EditBookWrapper>
    val bookInfoStates: SnapshotStateList<EditBookState> = mutableStateListOf()

    init {
        initializeState()
    }

    override fun setInitialState() = EditorTabContract.State()

    override fun handleEvents(event: EditorTabContract.Event) {
        when(event) {
            is SearchTextInputUpdate -> handleSearchTextInput(searchText = event.searchText)
            SearchTextInputCancel -> handleSearchTextInput(searchText = "")
            is SelectBookSortOrder -> sortBooksByOrder(sortOrder = event.sortOrder)
            BookListEnterEditMode -> toggleEditMode()
            BookListExitEditMode -> toggleEditMode()

            BookHolderSelectAll -> selectAllHolders()
            BookHolderDeselectAll -> deselectAllHolders()
            BookHolderAddBook -> addNewBook()
            BookHolderDeleteBook -> deleteSelectedBooks()

            is BookHolderEnterEditBook -> navigateToEditBook(bookId = event.bookId)
            is BookHolderSelectBook -> toggleBookSelection(bookId = event.bookId)

            is SelectBookPageNumber -> showBooksForPage(pageNumber = event.pageNumber)
        }
    }

    override fun handleCommonEvents(event: CommonEvent) {
        when(event){
            is CommonEvent.OnResume -> handleOnResume()
            else -> super.handleCommonEvents(event)
        }
    }

    private fun initializeState() {
        launchWithInit {
            // TODO 07.14 init Editor Tab
            useCaseBookList.makeSampleEpisode()

            updateBookStateList()
            setState {
                copy(
                    isInitSuccess = true
                )
            }
        }
    }

    // EditorTabEvent
    private fun handleSearchTextInput(searchText : String) = launchWithLoading{
        // TODO 08.04 Search Book
    }


    private fun sortBooksByOrder(sortOrder: EditBookSortOrder) = launchWithLoading {
        // TODO 08.04 Sort Books
    }

    private fun toggleEditMode() = launchWithLoading {
        // TODO 08.04 Edit Mode
    }

    private fun selectAllHolders() = launchWithLoading {
        // TODO 08.04 Holder Select All
    }

    private fun deselectAllHolders() = launchWithLoading {
        // TODO 08.04 Holder Deselect All
    }

    private fun addNewBook() = launchWithLoading {
        // TODO 08.04 Holder Add New
    }

    private fun deleteSelectedBooks() = launchWithLoading {
        // TODO 08.04 Holder Delete Selected
    }

    private fun navigateToEditBook(bookId: String) {
        isUpdateResume = true
        setEffect {
            EditorTabContract.Effect.Navigation.NavigateEditorBookOverviewScreen(episodeRef = EpisodeRef(
                userId = userId,
                mode = mode,
                bookId = bookId,
                episodeId = "${bookId}_0"
            ))
        }
    }

    private fun toggleBookSelection(bookId: String) {
        // TODO 08.04 Holder Book Selection
    }

    private fun showBooksForPage(pageNumber: Int) = launchWithLoading {
        // TODO 08.04 Show Books For Page Number
    }

    // CommonEvent

    // CommonFunction
    private suspend fun updateBookStateList() {
        originBookInfoList = useCaseBookList.getUserEditBookInfoList(userId = userId).map {
            val bookInfo = it.first
            val episodeInfo = it.second

            val bookCoverFile: File? =
                if (bookInfo.introduce.coverList.isNotEmpty()) useCaseLoadBook.loadCoverImage(
                    EpisodeRef(userId = userId, mode = ReadMode.EDIT, bookId = bookInfo.id, episodeId = episodeInfo.id),
                    bookInfo.introduce.coverList[0]
                ) else null

            val savedPickType = if (bookCoverFile != null) ImagePickType.SavedImage(
                bookCoverFile
            ) else ImagePickType.Empty

            it.toWrapper(savedPickType)
        }.sortedBy { it.bookId }

        bookInfoStates.clear()
        originBookInfoList.forEach { bookInfo ->
            bookInfoStates.add(
                EditBookState(
                    bookInfo = bookInfo,
                    selected = mutableStateOf(false)
                )
            )
        }

        when(uiState.value.bookSortOrder){
            EditBookSortOrder.LAST_EDITED -> bookInfoStates.sortByDescending { it.bookInfo.editTime }
            EditBookSortOrder.TITLE_ASCENDING -> bookInfoStates.sortBy { it.bookInfo.title }
        }
    }

    private fun handleOnResume() {
        if (isUpdateResume) {
            isUpdateResume = false
            launchWithLoading {
                // TODO 07.14 load Editor Tab
            }
        }
    }
}
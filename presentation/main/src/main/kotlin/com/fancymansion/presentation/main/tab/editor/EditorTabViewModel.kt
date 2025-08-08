package com.fancymansion.presentation.main.tab.editor

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import com.fancymansion.core.common.const.ArgName.NAME_USER_ID
import com.fancymansion.core.common.const.EpisodeRef
import com.fancymansion.core.common.const.ImagePickType
import com.fancymansion.core.common.const.ReadMode
import com.fancymansion.core.common.const.testEpisodeRef
import com.fancymansion.core.common.log.Logger
import com.fancymansion.core.presentation.base.BaseViewModel
import com.fancymansion.core.presentation.base.CommonEvent
import com.fancymansion.domain.usecase.book.UseCaseBookList
import com.fancymansion.domain.usecase.book.UseCaseLoadBook
import com.fancymansion.domain.usecase.util.UseCaseGetResource
import com.fancymansion.presentation.main.tab.editor.EditorTabContract.Event.*
import dagger.hilt.android.lifecycle.HiltViewModel
import java.io.File
import javax.inject.Inject

const val BOOKS_PER_PAGE = 10

@HiltViewModel
class EditorTabViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val useCaseLoadBook: UseCaseLoadBook,
    private val useCaseBookList: UseCaseBookList,
    private val useCaseGetResource: UseCaseGetResource
) : BaseViewModel<EditorTabContract.State, EditorTabContract.Event, EditorTabContract.Effect>() {

    private var isUpdateResume = false
    private var userId: String = savedStateHandle.get<String>(NAME_USER_ID) ?: testEpisodeRef.userId
    private val mode: ReadMode = ReadMode.EDIT
    private lateinit var originBookInfoList: List<EditBookWrapper>

    private val allBookStates: MutableList<EditBookState> = mutableListOf()
    private val searchResultBookStates: MutableList<EditBookState> = mutableListOf()

    private val isSearchMode : Boolean
        get() = uiState.value.searchText.isNotEmpty()

    init {
        initializeState()
    }

    override fun setInitialState() = EditorTabContract.State()

    override fun handleEvents(event: EditorTabContract.Event) {
        when(event) {
            // Mode Common
            is BookPageNumberClicked -> showBooksForPage(page = event.pageNumber)
            is BookHolderClicked -> {
                if (uiState.value.isEditMode) {
                    toggleBookSelection(bookId = event.bookId)
                } else {
                    navigateEditBookScreen(bookId = event.bookId)
                }
            }

            // Normal Mode
            is SearchTextInput -> updateSearchText(searchText = event.searchText)
            SearchClicked -> handleSearch()
            SearchCancel -> handleSearchCancel()

            is SelectBookSortOrder -> handleSelectBookSortOrder(sortOrder = event.sortOrder)

            // Edit Mode
            BookListEnterEditMode -> handleEnterEditMode()
            BookListExitEditMode -> handleExitEditMode()

            BookHolderSelectAll -> selectAllHolders()
            BookHolderDeselectAll -> deselectAllHolders()

            BookHolderAddBook -> handleAddBook()
            BookHolderDeleteBook -> handleDeleteSelectedBooks()
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
            useCaseBookList.makeSampleEpisode()

            loadBookStateList()

            sortTargetBookList(allBookStates, EditBookSortOrder.LAST_EDITED)
            updatePagedList(allBookStates, 0)
            setState {
                copy(
                    isInitSuccess = true
                )
            }
        }
    }

    /**
     * EditorTabEvent
     */
    // Mode Common
    private fun showBooksForPage(page: Int) = launchWithLoading {
        val targetList = if (isSearchMode) {
            searchResultBookStates
        } else {
            allBookStates
        }
        updatePagedList(targetList, page)
    }

    // Normal Mode
    private fun updateSearchText(searchText : String) {
        setState {
            copy(
                searchText = searchText
            )
        }
    }

    private fun handleSearch() = launchWithLoading {
        if(isSearchMode){
            updateSearchResultBookStates(uiState.value.searchText)
            sortTargetBookList(searchResultBookStates, uiState.value.bookSortOrder)
            updatePagedList(searchResultBookStates, 0)
        }
    }

    private fun handleSearchCancel() = launchWithLoading{
        searchResultBookStates.clear()
        setState {
            copy(
                searchText = ""
            )
        }
        sortTargetBookList(allBookStates, uiState.value.bookSortOrder)
        updatePagedList(allBookStates, 0)
    }

    private fun handleSelectBookSortOrder(sortOrder: EditBookSortOrder) = launchWithLoading {
        val targetList = if (isSearchMode) {
            updateSearchResultBookStates(uiState.value.searchText)
            searchResultBookStates
        } else {
            searchResultBookStates.clear()
            allBookStates
        }

        sortTargetBookList(targetList, sortOrder)
        updatePagedList(targetList, 0)
    }

    private fun navigateEditBookScreen(bookId: String) {
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

    // Edit Mode
    private fun handleEnterEditMode() = launchWithLoading {
        // TODO 08.04 Edit Mode
    }

    private fun handleExitEditMode() = launchWithLoading {
        // TODO 08.04 Edit Mode
    }

    private fun selectAllHolders() = launchWithLoading {
        // TODO 08.04 Holder Select All
    }

    private fun deselectAllHolders() = launchWithLoading {
        // TODO 08.04 Holder Deselect All
    }

    private fun handleAddBook() = launchWithLoading {
        // TODO 08.04 Holder Add New
    }

    private fun handleDeleteSelectedBooks() = launchWithLoading {
        // TODO 08.04 Holder Delete Selected
    }

    private fun toggleBookSelection(bookId: String) {
        // TODO 08.04 Holder Book Selection
    }

    // CommonFunction
    private fun handleOnResume() {
        if (isUpdateResume) {
            // TODO 08.08 Scroll Position
            isUpdateResume = false
            launchWithLoading {
                loadBookStateList()

                val targetList = if (isSearchMode) {
                    updateSearchResultBookStates(uiState.value.searchText)
                    searchResultBookStates
                } else {
                    searchResultBookStates.clear()
                    allBookStates
                }

                sortTargetBookList(targetList, uiState.value.bookSortOrder)
                updatePagedList(targetList, uiState.value.currentPage)
            }
        }
    }

    /**
     * Core Function
     */
    private suspend fun loadBookStateList() {
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
        }

        allBookStates.clear()
        originBookInfoList.forEach { bookInfo ->
            allBookStates.add(
                EditBookState(
                    bookInfo = bookInfo,
                    selected = mutableStateOf(false)
                )
            )
        }
    }

    // Sort List
    private fun sortTargetBookList(
        targetList: MutableList<EditBookState>,
        order: EditBookSortOrder
    ) {
        when (order) {
            EditBookSortOrder.LAST_EDITED -> targetList.sortByDescending { it.bookInfo.editTime }
            EditBookSortOrder.TITLE_ASCENDING -> targetList.sortBy { it.bookInfo.title }
        }

        setState {
            copy(
                bookSortOrder = order
            )
        }
    }

    // Paging
    private fun updatePagedList(list: List<EditBookState>, pageNumber: Int) {
        val totalPageCount = (list.size + BOOKS_PER_PAGE - 1) / BOOKS_PER_PAGE

        val page = if(totalPageCount < 1) 0 else pageNumber.coerceIn(0, totalPageCount - 1)
        val pagedBookList = getPagedBookList(list, page)

        setState {
            copy(
                totalPageCount = totalPageCount,
                currentPage = page,
                visibleBookList = pagedBookList
            )
        }
    }

    private fun getPagedBookList(
        list: List<EditBookState>,
        page: Int,
        pageSize: Int = BOOKS_PER_PAGE
    ): List<EditBookState> {
        val from = page * pageSize
        val to = minOf(from + pageSize, list.size)
        return if (from >= list.size) emptyList() else list.subList(from, to).toList()
    }

    // Search
    private fun updateSearchResultBookStates(searchText: String) {
        searchResultBookStates.clear()
        allBookStates.filter { it.bookInfo.title.contains(searchText) }.forEach {
            searchResultBookStates.add(it)
        }
    }

}
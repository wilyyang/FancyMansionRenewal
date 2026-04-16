package com.fancymansion.presentation.main.tab.library

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import com.fancymansion.core.common.const.EpisodeRef
import com.fancymansion.core.common.const.ImagePickType
import com.fancymansion.core.common.const.ReadMode
import com.fancymansion.core.common.const.getEpisodeId
import com.fancymansion.core.presentation.base.BaseViewModel
import com.fancymansion.core.presentation.base.CommonEvent
import com.fancymansion.domain.usecase.book.UseCaseBookList
import com.fancymansion.domain.usecase.book.UseCaseLoadBook
import com.fancymansion.domain.usecase.user.UseCaseGetUserInfoLocal
import com.fancymansion.domain.usecase.util.UseCaseGetResource
import com.fancymansion.presentation.main.common.BOOKS_PER_PAGE
import com.fancymansion.presentation.main.common.ListTarget
import com.fancymansion.presentation.main.common.paged
import com.fancymansion.presentation.main.tab.library.LibraryTabContract.Event.BookHolderClicked
import com.fancymansion.presentation.main.tab.library.LibraryTabContract.Event.BookHolderDeleteBook
import com.fancymansion.presentation.main.tab.library.LibraryTabContract.Event.BookHolderDeselectAll
import com.fancymansion.presentation.main.tab.library.LibraryTabContract.Event.BookHolderSelectAll
import com.fancymansion.presentation.main.tab.library.LibraryTabContract.Event.BookListEnterEditMode
import com.fancymansion.presentation.main.tab.library.LibraryTabContract.Event.BookListExitEditMode
import com.fancymansion.presentation.main.tab.library.LibraryTabContract.Event.BookPageNumberClicked
import com.fancymansion.presentation.main.tab.library.LibraryTabContract.Event.SearchCancel
import com.fancymansion.presentation.main.tab.library.LibraryTabContract.Event.SearchClicked
import com.fancymansion.presentation.main.tab.library.LibraryTabContract.Event.SearchTextInput
import com.fancymansion.presentation.main.tab.library.LibraryTabContract.Event.SelectBookSortOrder
import dagger.hilt.android.lifecycle.HiltViewModel
import java.io.File
import javax.inject.Inject

@HiltViewModel
class LibraryTabViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val useCaseLoadBook: UseCaseLoadBook,
    private val useCaseBookList: UseCaseBookList,
    private val useCaseGetUserInfoLocal: UseCaseGetUserInfoLocal,
    private val useCaseGetResource: UseCaseGetResource
) : BaseViewModel<LibraryTabContract.State, LibraryTabContract.Event, LibraryTabContract.Effect>() {
    private var isUpdateResume = false
    private lateinit var userId: String
    private val mode: ReadMode = ReadMode.READ
    private lateinit var originBookInfoList: List<LibraryBookWrapper>
    private val allBookStates: MutableList<LibraryBookState> = mutableListOf()
    private val searchResultBookStates: MutableList<LibraryBookState> = mutableListOf()
    private var listTarget : ListTarget = ListTarget.ALL

    init {
        initializeState()
    }

    override fun setInitialState() = LibraryTabContract.State()

    override fun handleEvents(event: LibraryTabContract.Event) {
        when(event) {
            // Mode Common
            is BookPageNumberClicked -> handlePageNumberClicked(page = event.pageNumber)
            is BookHolderClicked -> {
                if (uiState.value.isEditMode) {
                    toggleBookSelected(bookId = event.bookId)
                } else {
                    navigateBookOverviewScreen(bookId = event.bookId)
                }
            }

            // Normal Mode
            is SearchTextInput -> handleUpdateSearchText(searchText = event.searchText)
            SearchClicked -> handleSearch()
            SearchCancel -> handleSearchCancel()

            is SelectBookSortOrder -> handleBookSortOrder(sortOrder = event.sortOrder)

            // Edit Mode
            BookListEnterEditMode -> handleEnterEditMode()
            BookListExitEditMode -> handleExitEditMode()

            BookHolderSelectAll -> selectVisibleHolders()
            BookHolderDeselectAll -> deselectAllHolders()

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
            val userInfo = useCaseGetUserInfoLocal() ?: error("UserInfo is null")
            userId = userInfo.userId

            loadBookStateList()
            sortBookList(ListTarget.ALL, LibraryBookSortOrder.LAST_DOWNLOAD)
            pagedBookList(ListTarget.ALL, 0)
            setState {
                copy(
                    isInitSuccess = true
                )
            }
        }
    }

    private fun handleOnResume() {
        launchWithLoading {
            loadBookStateList()

            when(listTarget){
                ListTarget.ALL -> searchBookListClear()
                ListTarget.SEARCH -> searchBookList(uiState.value.searchText)
            }
            sortBookList(listTarget, uiState.value.bookSortOrder)
            pagedBookList(listTarget, uiState.value.currentPage)
        }
    }

    /**
     * [1] 일반 이벤트 처리 함수
     */
    private fun handlePageNumberClicked(page: Int) = launchWithLoading {
        pagedBookList(listTarget, page)
    }

    private fun navigateBookOverviewScreen(bookId: String) {
        isUpdateResume = true
        setEffect {
            LibraryTabContract.Effect.Navigation.NavigateBookOverviewScreen(episodeRef = EpisodeRef(
                userId = userId,
                mode = mode,
                bookId = bookId,
                episodeId = getEpisodeId(bookId)
            ))
        }
    }

    private fun handleUpdateSearchText(searchText : String) {
        setState {
            copy(
                searchText = searchText
            )
        }
    }

    private fun handleSearch() = launchWithLoading {
        if(uiState.value.searchText.isNotBlank()){
            listTarget = ListTarget.SEARCH
            searchBookList(uiState.value.searchText)
            sortBookList(listTarget, uiState.value.bookSortOrder)
            pagedBookList(listTarget, 0)
        }
    }

    private fun handleSearchCancel() = launchWithLoading{
        exitSearchMode()
        sortBookList(listTarget, uiState.value.bookSortOrder)
        pagedBookList(listTarget, 0)
    }

    private fun handleBookSortOrder(sortOrder: LibraryBookSortOrder) = launchWithLoading {
        when(listTarget){
            ListTarget.ALL -> searchBookListClear()
            ListTarget.SEARCH -> searchBookList(uiState.value.searchText)
        }

        sortBookList(listTarget, sortOrder)
        pagedBookList(listTarget, 0)
    }

    /**
     * [2] 편집 이벤트 처리 함수
     */
    private fun handleEnterEditMode() = launchWithLoading {
        val resetFlag =
            listTarget != ListTarget.ALL || uiState.value.bookSortOrder != LibraryBookSortOrder.LAST_DOWNLOAD

        if(listTarget == ListTarget.SEARCH){
            listTarget = ListTarget.ALL
            searchBookListClear()
        }

        setState {
            copy(
                searchText = ""
            )
        }

        if(resetFlag){
            sortBookList(listTarget, LibraryBookSortOrder.LAST_DOWNLOAD)
            pagedBookList(listTarget, 0)
        }

        setState {
            copy(
                isEditMode = true
            )
        }
    }

    private fun handleExitEditMode() = launchWithLoading {
        loadBookStateList()
        sortBookList(listTarget, LibraryBookSortOrder.LAST_DOWNLOAD)
        pagedBookList(listTarget, uiState.value.currentPage)
        setState {
            copy(
                isEditMode = false
            )
        }
    }

    private fun handleDeleteSelectedBooks() = launchWithLoading {
        allBookStates
            .filter { it.selected.value }
            .forEach {
                useCaseBookList.deleteUserLibraryBook(userId, it.bookInfo.bookId)
            }
        loadBookStateList()
        sortBookList(listTarget, uiState.value.bookSortOrder)
        pagedBookList(listTarget, 0)
    }

    /**
     * [3] 처리 함수
     */
    private suspend fun loadBookStateList() {
        originBookInfoList = useCaseBookList.getLocalBookInfoList(userId = userId, readMode = mode).map {
            val bookInfo = it.book
            val episodeInfo = it.episode

            val bookCoverFile: File? =
                if (bookInfo.introduce.coverList.isNotEmpty()) useCaseLoadBook.loadCoverImage(
                    EpisodeRef(userId = userId, mode = mode, bookId = bookInfo.id, episodeId = episodeInfo.id),
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
                LibraryBookState(
                    bookInfo = bookInfo,
                    selected = mutableStateOf(false)
                )
            )
        }
    }

    private fun searchBookList(searchText: String) {
        searchResultBookStates.clear()
        allBookStates.filter { it.bookInfo.title.contains(searchText) }.forEach {
            searchResultBookStates.add(it)
        }
    }

    private fun searchBookListClear() {
        searchResultBookStates.clear()
    }

    private fun sortBookList(
        listTarget: ListTarget,
        order: LibraryBookSortOrder
    ) {
        val target = when(listTarget){
            ListTarget.ALL -> allBookStates
            ListTarget.SEARCH -> searchResultBookStates
        }

        when (order) {
            LibraryBookSortOrder.LAST_DOWNLOAD -> target.sortByDescending { it.bookInfo.metadata.downloadAt }
            LibraryBookSortOrder.TITLE_ASCENDING -> target.sortBy { it.bookInfo.title }
        }

        setState {
            copy(
                bookSortOrder = order
            )
        }
    }

    private fun pagedBookList(
        listTarget: ListTarget,
        pageNumber: Int
    ) {
        val target = when(listTarget){
            ListTarget.ALL -> allBookStates
            ListTarget.SEARCH -> searchResultBookStates
        }

        val totalPage = (target.size + BOOKS_PER_PAGE - 1) / BOOKS_PER_PAGE
        val currentPage = if(totalPage < 1) 0 else pageNumber.coerceIn(0, totalPage - 1)

        val pagedBookList = target.paged(currentPage, BOOKS_PER_PAGE)

        setState {
            copy(
                totalPageCount = totalPage,
                currentPage = currentPage,
                visibleBookList = pagedBookList
            )
        }
    }

    private fun toggleBookSelected(bookId: String) {
        allBookStates.find { it.bookInfo.bookId == bookId }?.let {
            it.selected.value = !it.selected.value
        }
    }

    private fun selectVisibleHolders() = launchWithLoading {
        val visibleIds = uiState.value.visibleBookList
            .map { it.bookInfo.bookId }
            .toSet()

        allBookStates
            .filter { it.bookInfo.bookId in visibleIds }
            .forEach {
                it.selected.value = true
            }
    }

    private fun deselectAllHolders() = launchWithLoading {
        allBookStates.forEach { it.selected.value = false }
    }

    private fun exitSearchMode(){
        listTarget = ListTarget.ALL
        searchBookListClear()
        setState {
            copy(
                searchText = ""
            )
        }
    }
}
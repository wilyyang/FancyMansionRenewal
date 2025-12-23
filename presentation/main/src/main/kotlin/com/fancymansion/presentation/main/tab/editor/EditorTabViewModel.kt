package com.fancymansion.presentation.main.tab.editor

import androidx.compose.runtime.mutableStateOf
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
import com.fancymansion.domain.usecase.util.UseCaseGetResource
import com.fancymansion.presentation.main.common.paged
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

    private var listTarget : ListTarget = ListTarget.ALL

    init {
        initializeState()
    }

    override fun setInitialState() = EditorTabContract.State()

    override fun handleEvents(event: EditorTabContract.Event) {
        when(event) {
            // Mode Common
            is BookPageNumberClicked -> handlePageNumberClicked(page = event.pageNumber)
            is BookHolderClicked -> {
                if (uiState.value.isEditMode) {
                    toggleBookSelection(bookId = event.bookId)
                } else {
                    navigateEditBookScreen(bookId = event.bookId)
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

            sortBookList(ListTarget.ALL, EditBookSortOrder.LAST_EDITED)
            pagedBookList(ListTarget.ALL, 0)
            setState {
                copy(
                    isInitSuccess = true
                )
            }
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

                when(listTarget){
                    ListTarget.ALL -> searchBookListClear()
                    ListTarget.SEARCH -> searchBookList(uiState.value.searchText)
                }

                sortBookList(listTarget, uiState.value.bookSortOrder)
                pagedBookList(listTarget, uiState.value.currentPage)
            }
        }
    }

    /**
     * [1] 이벤트 처리 함수
     */
    private fun handlePageNumberClicked(page: Int) = launchWithLoading {
        pagedBookList(listTarget, page)
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
        listTarget = ListTarget.ALL
        searchBookListClear()
        sortBookList(listTarget, uiState.value.bookSortOrder)
        pagedBookList(listTarget, 0)

        setState {
            copy(
                searchText = ""
            )
        }
    }
    private fun handleBookSortOrder(sortOrder: EditBookSortOrder) = launchWithLoading {
        when(listTarget){
            ListTarget.ALL -> searchBookListClear()
            ListTarget.SEARCH -> searchBookList(uiState.value.searchText)
        }

        sortBookList(listTarget, sortOrder)
        pagedBookList(listTarget, 0)
    }
    /**
     * [2] 처리 함수
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
        order: EditBookSortOrder
    ) {
        val target = when(listTarget){
            ListTarget.ALL -> allBookStates
            ListTarget.SEARCH -> searchResultBookStates
        }

        when (order) {
            EditBookSortOrder.LAST_EDITED -> target.sortByDescending { it.bookInfo.editTime }
            EditBookSortOrder.TITLE_ASCENDING -> target.sortBy { it.bookInfo.title }
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
}
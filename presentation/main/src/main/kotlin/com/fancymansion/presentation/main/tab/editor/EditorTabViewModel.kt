package com.fancymansion.presentation.main.tab.editor

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import com.fancymansion.core.common.const.EpisodeRef
import com.fancymansion.core.common.const.INIT_PUBLISHED_AT
import com.fancymansion.core.common.const.INIT_VERSION
import com.fancymansion.core.common.const.ImagePickType
import com.fancymansion.core.common.const.PageType
import com.fancymansion.core.common.const.PublishStatus
import com.fancymansion.core.common.const.ReadMode
import com.fancymansion.core.common.const.getBookId
import com.fancymansion.core.common.const.getEpisodeId
import com.fancymansion.core.common.const.nextBookNumber
import com.fancymansion.core.presentation.base.BaseViewModel
import com.fancymansion.core.presentation.base.CommonEvent
import com.fancymansion.core.presentation.base.LoadState
import com.fancymansion.domain.model.book.BookInfoModel
import com.fancymansion.domain.model.book.BookMetaModel
import com.fancymansion.domain.model.book.EditorModel
import com.fancymansion.domain.model.book.EpisodeInfoModel
import com.fancymansion.domain.model.book.IntroduceModel
import com.fancymansion.domain.model.book.LogicModel
import com.fancymansion.domain.model.book.PageLogicModel
import com.fancymansion.domain.model.book.PageModel
import com.fancymansion.domain.usecase.book.UseCaseBookList
import com.fancymansion.domain.usecase.book.UseCaseLoadBook
import com.fancymansion.domain.usecase.user.UseCaseGetUserInfoLocal
import com.fancymansion.domain.usecase.util.UseCaseGetResource
import com.fancymansion.presentation.main.R
import com.fancymansion.presentation.main.common.BOOKS_PER_PAGE
import com.fancymansion.presentation.main.common.EDIT_BOOKS_LIMIT
import com.fancymansion.presentation.main.common.paged
import com.fancymansion.presentation.main.tab.editor.EditorTabContract.Event.*
import dagger.hilt.android.lifecycle.HiltViewModel
import java.io.File
import javax.inject.Inject

@HiltViewModel
class EditorTabViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val useCaseLoadBook: UseCaseLoadBook,
    private val useCaseBookList: UseCaseBookList,
    private val useCaseGetUserInfoLocal: UseCaseGetUserInfoLocal,
    private val useCaseGetResource: UseCaseGetResource
) : BaseViewModel<EditorTabContract.State, EditorTabContract.Event, EditorTabContract.Effect>() {

    private var isUpdateResume = false
    private var lastOpenedBookId: String? = null
    private lateinit var userId: String
    private val mode: ReadMode = ReadMode.EDIT
    private lateinit var originBookInfoList: List<EditBookWrapper>

    private val allBookStates: MutableList<EditBookState> = mutableListOf()
    private val searchResultBookStates: MutableList<EditBookState> = mutableListOf()

    private var listTarget : ListTarget = ListTarget.ALL

    private lateinit var editorInfo: EditorModel

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
                    toggleBookSelected(bookId = event.bookId)
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

            BookHolderSelectAll -> selectVisibleHolders()
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
            val userInfo = useCaseGetUserInfoLocal() ?: error("UserInfo is null")
            userId = userInfo.userId
            editorInfo = EditorModel(
                editorId = userInfo.userId,
                editorName = userInfo.nickname,
                editorEmail = userInfo.email
            )

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

    private fun handleOnResume() {
        if (isUpdateResume) {
            isUpdateResume = false
            launchWithLoading {
                loadBookStateList()

                when(listTarget){
                    ListTarget.ALL -> searchBookListClear()
                    ListTarget.SEARCH -> searchBookList(uiState.value.searchText)
                }

                // 이전에 검색 목록인 경우, 제목 변경으로 요소를 찾을 수 없으면 검색 취소
                if (listTarget == ListTarget.SEARCH) {
                    lastOpenedBookId?.let { bookId ->
                        if (!containsBookId(listTarget, bookId)) {
                            exitSearchMode()
                        }
                    }
                }

                sortBookList(listTarget, uiState.value.bookSortOrder)

                val updatePage = lastOpenedBookId?.let { id ->
                    lastOpenedBookId = null
                    getPageWithBookId(id)
                } ?: uiState.value.currentPage
                pagedBookList(listTarget, updatePage)
            }
        }
    }

    /**
     * [1] 일반 이벤트 처리 함수
     */
    private fun handlePageNumberClicked(page: Int) = launchWithLoading {
        pagedBookList(listTarget, page)
    }

    private fun navigateEditBookScreen(bookId: String) {
        isUpdateResume = true
        lastOpenedBookId = bookId
        setEffect {
            EditorTabContract.Effect.Navigation.NavigateEditorBookOverviewScreen(episodeRef = EpisodeRef(
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
    private fun handleBookSortOrder(sortOrder: EditBookSortOrder) = launchWithLoading {
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
            listTarget != ListTarget.ALL || uiState.value.bookSortOrder != EditBookSortOrder.LAST_EDITED

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
            sortBookList(listTarget, EditBookSortOrder.LAST_EDITED)
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
        sortBookList(listTarget, EditBookSortOrder.LAST_EDITED)
        pagedBookList(listTarget, uiState.value.currentPage)
        setState {
            copy(
                isEditMode = false
            )
        }
    }

    private fun handleAddBook() = launchWithLoading(endLoadState = null) {
        if (allBookStates.size < EDIT_BOOKS_LIMIT) {

            // 선택된 책 ID 저장
            val selectedIdSet = allBookStates.filter { it.selected.value }.map { it.bookInfo.bookId }.toSet()

            val newNumber = nextBookNumber(originBookInfoList.map { it.bookId }, userId)
            val episodeRef = EpisodeRef(
                userId = userId,
                mode = mode,
                bookId = getBookId(userId, newNumber),
                episodeId = getEpisodeId(userId, newNumber)
            )
            val (bookInfo, episodeInfo, logic, startPage) = createNewBookDraft(episodeRef, newNumber, editorInfo)
            val metaData = BookMetaModel(
                status = PublishStatus.UNPUBLISHED,
                publishedAt = INIT_PUBLISHED_AT,
                version = INIT_VERSION
            )

            useCaseBookList.addUserEditBook(
                episodeRef = episodeRef,
                bookInfo = bookInfo,
                metaData = metaData,
                episodeInfo = episodeInfo,
                logic = logic,
                startPage = startPage
            )
            loadBookStateList()
            sortBookList(listTarget, uiState.value.bookSortOrder)
            pagedBookList(listTarget, 0)

            // 선택된 책 ID 복원
            allBookStates.forEach { state ->
                state.selected.value = state.bookInfo.bookId in selectedIdSet
            }

            setLoadStateIdle()
        } else {
            setLoadState(
                loadState = LoadState.AlarmDialog(
                    title = useCaseGetResource.string(com.fancymansion.core.common.R.string.alarm),
                    message = useCaseGetResource.string(R.string.edit_book_message_book_limit_exceeded, EDIT_BOOKS_LIMIT),
                    dismissText = null,
                    confirmText = useCaseGetResource.string(com.fancymansion.core.common.R.string.confirm),
                    onConfirm = ::setLoadStateIdle
                )
            )
        }
    }

    private fun handleDeleteSelectedBooks() = launchWithLoading {
        allBookStates
            .filter { it.selected.value }
            .forEach {
                useCaseBookList.deleteUserEditBook(userId, it.bookInfo.bookId)
            }
        loadBookStateList()
        sortBookList(listTarget, uiState.value.bookSortOrder)
        pagedBookList(listTarget, 0)
    }

    /**
     * [3] 처리 함수
     */
    private suspend fun loadBookStateList() {
        originBookInfoList = useCaseBookList.getLocalBookInfoList(userId = userId, readMode = ReadMode.EDIT).map {
            val bookInfo = it.book
            val episodeInfo = it.episode

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

    private fun createNewBookDraft(
        episodeRef: EpisodeRef,
        newNumber: Int,
        editor: EditorModel
    ): NewBookDraft {
        val currentTime = System.currentTimeMillis()
        val bookInfo = BookInfoModel(
            id = episodeRef.bookId,
            introduce = IntroduceModel(
                title = useCaseGetResource.string(R.string.edit_book_new_book_title, newNumber),
                coverList = listOf(),
                keywordList = listOf(),
                description = ""
            ),
            editor = editor
        )

        val episodeInfo = EpisodeInfoModel(
            bookId = episodeRef.bookId,
            createTime = currentTime,
            editTime = currentTime,
            id = episodeRef.episodeId,
            title = "",
            pageCount = 1
        )

        val logic = LogicModel(
            id = NEW_BOOK_DRAFT_INITIAL_LOGIC_ID,
            logics = listOf(
                PageLogicModel(
                    NEW_BOOK_DRAFT_START_PAGE_ID,
                    PageType.START,
                    useCaseGetResource.string(R.string.edit_book_new_book_start_page_title)
                )
            )
        )

        val startPage = PageModel(
            NEW_BOOK_DRAFT_START_PAGE_ID,
            useCaseGetResource.string(R.string.edit_book_new_book_start_page_title),
            listOf()
        )
        return NewBookDraft(bookInfo, episodeInfo, logic, startPage)
    }

    private fun getPageWithBookId(bookId: String):Int {
        val target = when(listTarget){
            ListTarget.ALL -> allBookStates
            ListTarget.SEARCH -> searchResultBookStates
        }
        val bookIndex = target.indexOfFirst { it.bookInfo.bookId == bookId }
        val currentPage = bookIndex / BOOKS_PER_PAGE
        return currentPage
    }

    private fun containsBookId(target: ListTarget, bookId: String): Boolean = when (target) {
        ListTarget.ALL -> allBookStates
        ListTarget.SEARCH -> searchResultBookStates
    }.any { it.bookInfo.bookId == bookId }

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
package com.fancymansion.presentation.main.tab.editor

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import com.fancymansion.core.common.const.ArgName.NAME_USER_ID
import com.fancymansion.core.common.const.BOOKS_PER_PAGE
import com.fancymansion.core.common.const.EDIT_BOOKS_LIMIT
import com.fancymansion.core.common.const.EpisodeRef
import com.fancymansion.core.common.const.ImagePickType
import com.fancymansion.core.common.const.ReadMode
import com.fancymansion.core.common.const.getBookId
import com.fancymansion.core.common.const.getEpisodeId
import com.fancymansion.core.common.const.sampleEpisodeRef
import com.fancymansion.core.presentation.base.BaseViewModel
import com.fancymansion.core.presentation.base.CommonEvent
import com.fancymansion.core.presentation.base.LoadState
import com.fancymansion.domain.model.book.BookInfoModel
import com.fancymansion.domain.model.book.EditorModel
import com.fancymansion.domain.model.book.EpisodeInfoModel
import com.fancymansion.domain.model.book.IntroduceModel
import com.fancymansion.domain.model.book.LogicModel
import com.fancymansion.domain.usecase.book.UseCaseBookList
import com.fancymansion.domain.usecase.book.UseCaseLoadBook
import com.fancymansion.domain.usecase.util.UseCaseGetResource
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
    private val useCaseGetResource: UseCaseGetResource
) : BaseViewModel<EditorTabContract.State, EditorTabContract.Event, EditorTabContract.Effect>() {

    private var isUpdateResume = false
    private var userId: String = savedStateHandle.get<String>(NAME_USER_ID) ?: sampleEpisodeRef.userId
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
            BookHolderDeselectAll -> deselectVisibleHolders()

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
     * [1] 일반 이벤트 처리 함수
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

    private fun handleAddBook() = launchWithLoading(endLoadState = null){
        if(allBookStates.size <= EDIT_BOOKS_LIMIT){
            val currentTime = System.currentTimeMillis()
            val newNumber = 0//BookIDManager.generateId(originBookInfoList.map { it.bookId })
            val bookId = getBookId(userId, ReadMode.EDIT, newNumber)
            val episodeId = getEpisodeId(userId, ReadMode.EDIT, newNumber)
            val episodeRef = EpisodeRef(
                userId = userId,
                mode = mode,
                bookId = bookId,
                episodeId = episodeId
            )
            val bookInfo = BookInfoModel(
                id = bookId,
                introduce = IntroduceModel(
                    title = "새로운 작품 $newNumber",
                    coverList = listOf(),
                    keywordList = listOf(),
                    description = ""
                ),
                editor = EditorModel(
                    editorId = "editor",
                    editorName = "테스터",
                    editorEmail = "tester@example.com"
                )
            )

            val episodeInfo = EpisodeInfoModel(
                bookId = bookId,
                createTime = currentTime,
                editTime = currentTime,
                id = episodeRef.episodeId,
                readMode = ReadMode.EDIT,
                title = "",
                pageCount = 0,
                version = 0
            )

            val logic = LogicModel(
                id = 0,
                logics = listOf()
            )

            useCaseBookList.addUserEditBook(
                episodeRef = episodeRef,
                bookInfo = bookInfo,
                episodeInfo = episodeInfo,
                logic = logic
            )
            loadBookStateList()
            sortBookList(listTarget, uiState.value.bookSortOrder)
            pagedBookList(listTarget, 0)
            setLoadStateIdle()
        }else{
            setLoadState(
                loadState = LoadState.AlarmDialog(
                    title = "알림",
                    message = "편집 작품의 개수를 초과했습니다. \n작품 한도는 ${EDIT_BOOKS_LIMIT}개 입니다. \n작품을 삭제해주세요.",
                    dismissText = null,
                    confirmText = "확인",
                    onConfirm = ::setLoadStateIdle
                )
            )
        }
    }

    private fun handleDeleteSelectedBooks() = launchWithLoading {
        // TODO 08.04 Holder Delete Selected
    }

    /**
     * [3] 처리 함수
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

    private fun deselectVisibleHolders() = launchWithLoading {
        val visibleIds = uiState.value.visibleBookList
            .map { it.bookInfo.bookId }
            .toSet()

        allBookStates
            .filter { it.bookInfo.bookId in visibleIds }
            .forEach {
                it.selected.value = false
            }
    }
}
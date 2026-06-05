package com.fancymansion.presentation.main.tab.home

import androidx.lifecycle.SavedStateHandle
import com.fancymansion.core.common.const.RemoteBookSortOrder
import com.fancymansion.core.common.resource.StringValue
import com.fancymansion.core.presentation.base.BaseViewModel
import com.fancymansion.core.presentation.base.LoadState
import com.fancymansion.domain.model.homeBook.result.BookQueryResult
import com.fancymansion.domain.usecase.remoteBook.UseCaseGetBookCoverImageUrl
import com.fancymansion.domain.usecase.remoteBook.UseCaseGetHomeBookListWithQuery
import com.fancymansion.domain.usecase.user.UseCaseGetUserInfoLocal
import com.fancymansion.domain.usecase.util.UseCaseGetResource
import com.fancymansion.presentation.main.R
import com.fancymansion.presentation.main.common.BOOKS_PER_PAGE
import com.fancymansion.presentation.main.common.QUERY_BOOKS_LIMIT
import com.fancymansion.presentation.main.tab.home.HomeTabContract.Event.BookHolderClicked
import com.fancymansion.presentation.main.tab.home.HomeTabContract.Event.BookPageNumberClicked
import com.fancymansion.presentation.main.tab.home.HomeTabContract.Event.SearchCancel
import com.fancymansion.presentation.main.tab.home.HomeTabContract.Event.SearchClicked
import com.fancymansion.presentation.main.tab.home.HomeTabContract.Event.SearchTextInput
import com.fancymansion.presentation.main.tab.home.HomeTabContract.Event.SelectBookSortOrder
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeTabViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val useCaseGetUserInfoLocal: UseCaseGetUserInfoLocal,
    private val useCaseGetHomeBookListWithQuery: UseCaseGetHomeBookListWithQuery,
    private val useCaseGetBookCoverImageUrl: UseCaseGetBookCoverImageUrl,
    private val useCaseGetResource: UseCaseGetResource
) : BaseViewModel<HomeTabContract.State, HomeTabContract.Event, HomeTabContract.Effect>() {
    private lateinit var userId: String

    private var currentChunkBooks = listOf<HomeBookWrapper>()
    private val prevPageCursors = mutableListOf<PageChunkCursor>()
    private var currentPageCursor: PageChunkCursor? = null
    private var nextPageCursor: PageChunkCursor? = null

    init {
        initializeState()
    }

    override fun setInitialState() = HomeTabContract.State()

    override fun handleEvents(event: HomeTabContract.Event) {
        when (event) {
            is BookPageNumberClicked -> handlePageNumberClicked(page = event.pageNumber)
            is BookHolderClicked -> handleBookHolderClicked(publishedId = event.publishedId)

            is SearchTextInput -> handleUpdateSearchText(searchText = event.searchText)
            SearchClicked -> handleSearch()
            SearchCancel -> handleSearchCancel()

            is SelectBookSortOrder -> handleBookSortOrder(sortOrder = event.sortOrder)
        }
    }

    private fun initializeState() {
        launchWithInit {
            val userInfo = useCaseGetUserInfoLocal() ?: error("UserInfo is null")
            userId = userInfo.userId

            initHomeBookList()
            setState {
                copy(
                    isInitSuccess = true
                )
            }
        }
    }

    /**
     * [1] 일반 이벤트 처리 함수
     */
    private fun handlePageNumberClicked(page: Int) = launchWithLoading {
        uiState.value.run {
            // 현재 청크 내부 이동
            if (page in startPage..endPage) {
                showCurrentChunkPage(page)
                return@launchWithLoading
            }


            val isNextChunk = page > endPage
            val cursor =
                if (isNextChunk) nextPageCursor
                else prevPageCursors.lastOrNull()

            cursor?.let {
                val (books, nextIds) = getHomeBookListWithQuery(
                    searchText,
                    bookSortOrder.toRemote(),
                    it.cursorBookIds
                )
                if (isNextChunk) prevPageCursors.add(currentPageCursor!!)
                else prevPageCursors.removeLast()

                applyChunkResult(
                    startPage = it.startPage,
                    books = books,
                    nextIds = nextIds
                )

                showCurrentChunkPage(page)
            }
        }
    }

    private fun handleBookHolderClicked(publishedId: String) {
        setEffect { HomeTabContract.Effect.Navigation.NavigateHomeBookOverviewScreen(publishedId) }
    }

    private fun handleUpdateSearchText(searchText: String) {
        setState {
            copy(
                searchText = searchText
            )
        }
    }

    private fun handleSearch() = launchWithLoading {
        if (uiState.value.searchText.isNotBlank()) {
            val (books, nextIds) = getHomeBookListWithQuery(
                uiState.value.searchText,
                HomeBookSortOrder.TITLE_ASCENDING.toRemote(),
                emptyList()
            )
            applyPageResetQueryResult(
                books = books,
                nextIds = nextIds,
                searchText = uiState.value.searchText,
                sortOrder = HomeBookSortOrder.TITLE_ASCENDING
            )
        }
    }

    private fun handleSearchCancel() = launchWithLoading {
        val (books, nextIds) = getHomeBookListWithQuery(
            "",
            uiState.value.bookSortOrder.toRemote(),
            emptyList()
        )
        applyPageResetQueryResult(
            books = books,
            nextIds = nextIds,
            sortOrder = uiState.value.bookSortOrder
        )
    }

    private fun handleBookSortOrder(sortOrder: HomeBookSortOrder) = launchWithLoading {
        val (books, nextIds) = getHomeBookListWithQuery(
            "",
            sortOrder.toRemote(),
            emptyList()
        )
        applyPageResetQueryResult(books = books, nextIds = nextIds, sortOrder = sortOrder)
    }

    /**
     * [3] 처리 함수
     */
    private suspend fun initHomeBookList(){
        val (books, nextIds) = getHomeBookListWithQuery(
            "",
            RemoteBookSortOrder.LAST_UPDATE,
            emptyList()
        )
        applyPageResetQueryResult(books, nextIds)
    }

    private suspend fun getHomeBookListWithQuery(
        searchText: String,
        sortOrder: RemoteBookSortOrder,
        cursorBookIds: List<String>
    ) = when (val result = useCaseGetHomeBookListWithQuery(
        searchText = searchText,
        sortOrder = sortOrder,
        cursorBookIds = cursorBookIds,
        limit = QUERY_BOOKS_LIMIT
    )) {
        is BookQueryResult.Success -> {
            val wrappers = result.model.bookList.map { item ->
                item.toWrapper(
                    useCaseGetBookCoverImageUrl(
                        item.book.publishInfo.publishedId,
                        item.book.introduce.coverList[0]
                    )
                )
            }
            Pair(wrappers, result.model.nextBookIds)
        }
        is BookQueryResult.Error -> {
            val (titleRes, textRes) = when(result){
                BookQueryResult.Error.CursorNotExist -> Pair(
                    R.string.home_book_load_error_cursor_not_exist_title,
                    R.string.home_book_load_error_cursor_not_exist_message
                )
                BookQueryResult.Error.InvalidSearch -> Pair(
                    R.string.home_book_load_error_invalid_search_title,
                    R.string.home_book_load_error_invalid_search_message
                )
                BookQueryResult.Error.NextBookIdError -> Pair(
                    R.string.home_book_load_error_next_book_id_title,
                    R.string.home_book_load_error_next_book_id_message
                )
                BookQueryResult.Error.NotFoundBook -> Pair(
                    R.string.home_book_load_error_not_found_book_title,
                    R.string.home_book_load_error_not_found_book_message
                )
            }
            throw LoadHomeBookException(
                StringValue.StringResource(titleRes),
                StringValue.StringResource(textRes)
            )
        }
    }

    private fun applyChunkResult(
        startPage: Int,
        books: List<HomeBookWrapper>,
        nextIds: List<String>
    ) {
        currentPageCursor = PageChunkCursor(
            startPage = startPage,
            bookSize = books.size,
            cursorBookIds = books.take(10).map { it.bookId }
        )

        nextPageCursor =
            if (nextIds.isEmpty()) {
                null
            } else {
                PageChunkCursor(
                    startPage = currentPageCursor!!.endPage + 1,
                    cursorBookIds = nextIds
                )
            }

        currentChunkBooks = books

        setState {
            copy(
                startPage = currentPageCursor!!.startPage,
                endPage = currentPageCursor!!.endPage,
                pageCount = currentPageCursor!!.pageCount,
                canPrev = prevPageCursors.isNotEmpty(),
                canNext = nextPageCursor != null
            )
        }
    }

    private fun showCurrentChunkPage(page: Int) {

        val localPage = page - uiState.value.startPage
        val startIndex = localPage * BOOKS_PER_PAGE

        setState {
            copy(
                currentPage = page,
                visibleBookList = currentChunkBooks
                    .drop(startIndex)
                    .take(BOOKS_PER_PAGE)
            )
        }
    }

    private fun applyPageResetQueryResult(
        books: List<HomeBookWrapper>,
        nextIds: List<String>,
        searchText: String = "",
        sortOrder: HomeBookSortOrder = HomeBookSortOrder.LAST_UPDATE
    ) {
        prevPageCursors.clear()
        applyChunkResult(
            startPage = 0,
            books = books,
            nextIds = nextIds
        )

        setState {
            copy(
                searchText = searchText,
                bookSortOrder = sortOrder,
            )
        }
        showCurrentChunkPage(0)
    }

    override fun showExceptionResult(
        throwable: Throwable,
        defaultConfirm: () -> Unit,
        defaultDismiss: () -> Unit
    ) {
        when (throwable) {
            is LoadHomeBookException -> setLoadState(
                LoadState.ErrorDialog(
                    title = throwable.title,
                    message = throwable.text,
                    dismissText = null,
                    onConfirm = {
                        launchWithLoading {
                            initHomeBookList()
                        }
                    }
                )
            )

            else -> super.showExceptionResult(throwable, defaultConfirm, defaultDismiss)
        }
    }
}
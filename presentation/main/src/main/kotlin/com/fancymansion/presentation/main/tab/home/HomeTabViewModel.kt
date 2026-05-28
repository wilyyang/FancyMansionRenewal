package com.fancymansion.presentation.main.tab.home

import androidx.lifecycle.SavedStateHandle
import com.fancymansion.core.common.const.EditorPublishStatus
import com.fancymansion.core.common.const.INIT_PUBLISHED_AT
import com.fancymansion.core.common.const.INIT_UPDATED_AT
import com.fancymansion.core.common.const.ReadMode
import com.fancymansion.core.common.const.RemoteBookSortOrder
import com.fancymansion.core.common.resource.StringValue
import com.fancymansion.core.presentation.base.BaseViewModel
import com.fancymansion.core.presentation.base.CommonEvent
import com.fancymansion.core.presentation.base.LoadState
import com.fancymansion.domain.model.book.BookMetaModel
import com.fancymansion.domain.model.homeBook.result.BookQueryResult
import com.fancymansion.domain.usecase.book.UseCaseMakeBook
import com.fancymansion.domain.usecase.remoteBook.UseCaseDownloadBook
import com.fancymansion.domain.usecase.remoteBook.UseCaseGetBookCoverImageUrl
import com.fancymansion.domain.usecase.remoteBook.UseCaseGetHomeBookListWithQuery
import com.fancymansion.domain.usecase.user.UseCaseGetUserInfoLocal
import com.fancymansion.domain.usecase.util.UseCaseGetResource
import com.fancymansion.presentation.main.R
import com.fancymansion.presentation.main.common.BOOKS_PER_PAGE
import com.fancymansion.presentation.main.common.QUERY_BOOKS_LIMIT
import com.fancymansion.presentation.main.common.VISIBLE_PAGE_LIMIT
import com.fancymansion.presentation.main.tab.home.HomeTabContract.Event.BookHolderClicked
import com.fancymansion.presentation.main.tab.home.HomeTabContract.Event.BookPageNumberClicked
import com.fancymansion.presentation.main.tab.home.HomeTabContract.Event.SearchCancel
import com.fancymansion.presentation.main.tab.home.HomeTabContract.Event.SearchClicked
import com.fancymansion.presentation.main.tab.home.HomeTabContract.Event.SearchTextInput
import com.fancymansion.presentation.main.tab.home.HomeTabContract.Event.SelectBookSortOrder
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlin.String

@HiltViewModel
class HomeTabViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val useCaseGetUserInfoLocal: UseCaseGetUserInfoLocal,
    private val useCaseDownloadBook: UseCaseDownloadBook,
    private val useCaseGetHomeBookListWithQuery: UseCaseGetHomeBookListWithQuery,
    private val useCaseGetBookCoverImageUrl: UseCaseGetBookCoverImageUrl,
    private val useCaseMakeBook: UseCaseMakeBook,
    private val useCaseGetResource: UseCaseGetResource
) : BaseViewModel<HomeTabContract.State, HomeTabContract.Event, HomeTabContract.Effect>() {

    private var isUpdateResume = false
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
            is BookHolderClicked -> handleBookHolderClicked(publishedId = event.bookId)

            is SearchTextInput -> handleUpdateSearchText(searchText = event.searchText)
            SearchClicked -> handleSearch()
            SearchCancel -> handleSearchCancel()

            is SelectBookSortOrder -> handleBookSortOrder(sortOrder = event.sortOrder)
        }
    }

    override fun handleCommonEvents(event: CommonEvent) {
        when (event) {
            is CommonEvent.OnResume -> handleOnResume()
            else -> super.handleCommonEvents(event)
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

    private fun handleOnResume() {
        if (isUpdateResume) {
            isUpdateResume = false
            launchWithLoading {
                // TODO
            }
        }
    }

    /**
     * [1] 일반 이벤트 처리 함수
     */
    private fun handlePageNumberClicked(page: Int) = launchWithLoading {
        uiState.value.run {
            if(page in startPage .. endPage){
                val localPage = page - startPage
                val startIndex = localPage * BOOKS_PER_PAGE

                setState {
                    copy(
                        currentPage = page,
                        visibleBookList = currentChunkBooks
                            .drop(startIndex)
                            .take(BOOKS_PER_PAGE)
                    )
                }
            } else {
                if (page > endPage) {
                    // 다음 청크로 이동
                    nextPageCursor?.let { cursor ->
                        getHomeBookListWithQuery(
                            searchText,
                            bookSortOrder.toRemote(),
                            cursor.cursorBookIds
                        ).let { (books, nextIds) ->
                            // 커서 업데이트
                            prevPageCursors.add(currentPageCursor!!)
                            currentPageCursor = PageChunkCursor(
                                startPage = cursor.startPage,
                                bookSize = books.size,
                                cursorBookIds = books.take(10).map { it.bookId }
                            )
                            nextPageCursor = if (nextIds.isEmpty()) null else
                                PageChunkCursor(
                                    startPage = currentPageCursor!!.endPage + 1,
                                    cursorBookIds = nextIds
                                )

                            // 데이터 업데이트
                            currentChunkBooks = books

                            val localPage = page - currentPageCursor!!.startPage
                            val startIndex = localPage * BOOKS_PER_PAGE
                            setState {
                                copy(
                                    startPage = currentPageCursor!!.startPage,
                                    endPage = currentPageCursor!!.endPage,
                                    currentPage = page,
                                    visibleBookList = currentChunkBooks
                                        .drop(startIndex)
                                        .take(BOOKS_PER_PAGE)
                                )
                            }
                        }
                    }

                } else {
                    // 이전 청크로 이동
                    prevPageCursors.lastOrNull()?.let { cursor ->
                        getHomeBookListWithQuery(
                            searchText,
                            bookSortOrder.toRemote(),
                            cursor.cursorBookIds
                        ).let { (books, nextIds) ->
                            // 커서 업데이트
                            prevPageCursors.removeLast()
                            currentPageCursor = PageChunkCursor(
                                startPage = cursor.startPage,
                                bookSize = books.size,
                                cursorBookIds = books.take(10).map { it.bookId }
                            )
                            nextPageCursor = if (nextIds.isEmpty()) null else
                                PageChunkCursor(
                                    startPage = currentPageCursor!!.endPage + 1,
                                    cursorBookIds = nextIds
                                )

                            // 데이터 업데이트
                            currentChunkBooks = books

                            val localPage = page - currentPageCursor!!.startPage
                            val startIndex = localPage * BOOKS_PER_PAGE
                            setState {
                                copy(
                                    startPage = currentPageCursor!!.startPage,
                                    endPage = currentPageCursor!!.endPage,
                                    currentPage = page,
                                    visibleBookList = currentChunkBooks
                                        .drop(startIndex)
                                        .take(BOOKS_PER_PAGE)
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    private fun handleBookHolderClicked(publishedId: String) = launchWithLoading {
        // [임시] 해당 홀더 북 다운로드하도록 구현
        val mode = ReadMode.READ
        val downloadVersion = useCaseDownloadBook(userId = userId, publishedId = publishedId, readMode = mode)
        useCaseMakeBook.makeMetaData(
            userId = userId,
            mode = mode,
            bookId = publishedId,
            metaData = BookMetaModel(
                status = EditorPublishStatus.PUBLISHED,
                publishedAt = INIT_PUBLISHED_AT,
                updatedAt = INIT_UPDATED_AT,
                downloadAt = System.currentTimeMillis(),
                version = downloadVersion
            )
        )
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
                uiState.value.bookSortOrder.toRemote(),
                emptyList()
            )
            applyPageResetQueryResult(
                books = books,
                nextIds = nextIds,
                searchText = uiState.value.searchText,
                sortOrder = uiState.value.bookSortOrder
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

    private fun applyPageResetQueryResult(
        books: List<HomeBookWrapper>,
        nextIds: List<String>,
        searchText: String = "",
        sortOrder: HomeBookSortOrder = HomeBookSortOrder.LAST_UPDATE
    ) {
        currentChunkBooks = books

        prevPageCursors.clear()
        currentPageCursor = PageChunkCursor(
            startPage = 0,
            bookSize = books.size,
            cursorBookIds = books.take(10).map { it.bookId }
        )
        nextPageCursor = if(nextIds.isEmpty()) null else PageChunkCursor(
            startPage = VISIBLE_PAGE_LIMIT,
            cursorBookIds = nextIds
        )

        setState {
            copy(
                searchText = searchText,
                bookSortOrder = sortOrder,
                startPage = currentPageCursor?.startPage?:0,
                endPage = currentPageCursor?.endPage?:0,
                currentPage = 0,
                visibleBookList = currentChunkBooks.take(BOOKS_PER_PAGE)
            )
        }
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
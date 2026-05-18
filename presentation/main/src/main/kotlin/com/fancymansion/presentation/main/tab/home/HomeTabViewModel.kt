package com.fancymansion.presentation.main.tab.home

import androidx.lifecycle.SavedStateHandle
import com.fancymansion.core.common.const.EditorPublishStatus
import com.fancymansion.core.common.const.INIT_PUBLISHED_AT
import com.fancymansion.core.common.const.INIT_UPDATED_AT
import com.fancymansion.core.common.const.ReadMode
import com.fancymansion.core.presentation.base.BaseViewModel
import com.fancymansion.core.presentation.base.CommonEvent
import com.fancymansion.domain.model.book.BookMetaModel
import com.fancymansion.domain.usecase.book.UseCaseMakeBook
import com.fancymansion.domain.usecase.remoteBook.UseCaseDownloadBook
import com.fancymansion.domain.usecase.remoteBook.UseCaseGetBookCoverImageUrl
import com.fancymansion.domain.usecase.remoteBook.UseCaseGetHomeBookList
import com.fancymansion.domain.usecase.user.UseCaseGetUserInfoLocal
import com.fancymansion.domain.usecase.util.UseCaseGetResource
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
    private val useCaseDownloadBook: UseCaseDownloadBook,
    private val useCaseGetHomeBookList: UseCaseGetHomeBookList,
    private val useCaseGetBookCoverImageUrl: UseCaseGetBookCoverImageUrl,
    private val useCaseMakeBook: UseCaseMakeBook,
    private val useCaseGetResource: UseCaseGetResource
) : BaseViewModel<HomeTabContract.State, HomeTabContract.Event, HomeTabContract.Effect>() {

    private var isUpdateResume = false
    private lateinit var userId: String

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

            // [임시] 전체 목록 가져오기
            loadHomeBookList()

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
                // [임시] 임시로 전체 목록 가져오기
                loadHomeBookList()
            }
        }
    }

    /**
     * [1] 일반 이벤트 처리 함수
     */
    private fun handlePageNumberClicked(page: Int) = launchWithLoading {
        // TODO : 페이지 넘버 선택 구현 필요

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
            // TODO : 북 검색 구현 필요
        }
    }

    private fun handleSearchCancel() = launchWithLoading {
        // TODO : 북 검색 취소 구현 필요
    }

    private fun handleBookSortOrder(sortOrder: HomeBookSortOrder) = launchWithLoading {
        // TODO : 북 정렬 구현 필요
    }

    /**
     * [3] 처리 함수
     */
    // TODO : [임시] 임시로 전체 목록 가져오기
    private suspend fun loadHomeBookList() {
        val allHomeBookList = useCaseGetHomeBookList().map {
            val imageUrl = useCaseGetBookCoverImageUrl(
                it.book.publishInfo.publishedId,
                it.book.introduce.coverList[0]
            )
            it.toWrapper(imageUrl)
        }

        setState {
            copy(
                visibleBookList = allHomeBookList
            )
        }
    }
}
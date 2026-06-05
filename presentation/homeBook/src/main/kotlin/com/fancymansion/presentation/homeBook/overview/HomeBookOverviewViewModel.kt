package com.fancymansion.presentation.homeBook.overview

import androidx.lifecycle.SavedStateHandle
import com.fancymansion.core.common.const.ArgName
import com.fancymansion.core.common.const.EditorPublishStatus
import com.fancymansion.core.common.const.INIT_PUBLISHED_AT
import com.fancymansion.core.common.const.INIT_UPDATED_AT
import com.fancymansion.core.common.const.ReadMode
import com.fancymansion.core.presentation.base.BaseViewModel
import com.fancymansion.domain.model.book.BookMetaModel
import com.fancymansion.domain.model.homeBook.result.LoadBookResult
import com.fancymansion.domain.usecase.book.UseCaseMakeBook
import com.fancymansion.domain.usecase.remoteBook.UseCaseDownloadBook
import com.fancymansion.domain.usecase.remoteBook.UseCaseGetBookCoverImageUrl
import com.fancymansion.domain.usecase.remoteBook.UseCaseGetSelectedHomeBook
import com.fancymansion.domain.usecase.user.UseCaseGetUserInfoLocal
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeBookOverviewViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val useCaseGetUserInfoLocal: UseCaseGetUserInfoLocal,
    private val useCaseDownloadBook: UseCaseDownloadBook,
    private val useCaseGetSelectedHomeBook: UseCaseGetSelectedHomeBook,
    private val useCaseGetBookCoverImageUrl: UseCaseGetBookCoverImageUrl,
    private val useCaseMakeBook: UseCaseMakeBook,
) : BaseViewModel<HomeBookOverviewContract.State, HomeBookOverviewContract.Event, HomeBookOverviewContract.Effect>() {

    private lateinit var userId: String
    private val bookId = savedStateHandle.get<String>(ArgName.NAME_BOOK_ID)!!

    override fun setInitialState() = HomeBookOverviewContract.State()

    override fun handleEvents(event: HomeBookOverviewContract.Event) {
        when (event) {
            HomeBookOverviewContract.Event.DownloadBookButtonClicked -> {
                handleDownloadClicked(publishedId = bookId)
            }

            HomeBookOverviewContract.Event.ReviewMoreButtonClicked -> {
                // TODO
            }
        }
    }

    init {
        launchWithInit {
            val userInfo = useCaseGetUserInfoLocal() ?: error("UserInfo is null")
            userId = userInfo.userId

            when (val result = useCaseGetSelectedHomeBook(bookId)) {
                is LoadBookResult.Success -> {

                    val coverUrl = useCaseGetBookCoverImageUrl(
                        bookId,
                        result.model.book.introduce.coverList[0]
                    )

                    setState {
                        copy(
                            homeBookInfo = result.model,
                            coverUrl = coverUrl
                        )
                    }
                }
                else -> {}
            }
        }
    }

    private fun handleDownloadClicked(publishedId: String) = launchWithLoading {
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
}
package com.fancymansion.presentation.bookOverview.home

import androidx.lifecycle.SavedStateHandle
import com.fancymansion.core.common.const.ArgName
import com.fancymansion.core.common.const.EpisodeRef
import com.fancymansion.core.common.const.testBookTitle
import com.fancymansion.core.common.const.testEpisodeRef
import com.fancymansion.core.common.const.testEpisodeTitle
import com.fancymansion.core.presentation.base.BaseViewModel
import com.fancymansion.domain.usecase.book.UseCaseLoadBook
import com.fancymansion.domain.usecase.book.UseCaseMakeBook
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class OverviewHomeViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val useCaseLoadBook: UseCaseLoadBook,
    private val useCaseMakeBook: UseCaseMakeBook
) : BaseViewModel<OverviewHomeContract.State, OverviewHomeContract.Event, OverviewHomeContract.Effect>() {
    private var episodeRef : EpisodeRef = savedStateHandle.run {
        EpisodeRef(
            get<String>(ArgName.NAME_USER_ID)?.ifBlank { testEpisodeRef.userId } ?: testEpisodeRef.userId,
            testEpisodeRef.mode, //get<ReadMode>(NAME_READ_MODE)
            get<String>(ArgName.NAME_BOOK_ID)?.ifBlank { testEpisodeRef.bookId } ?: testEpisodeRef.bookId,
            get<String>(ArgName.NAME_EPISODE_ID)?.ifBlank { testEpisodeRef.episodeId } ?: testEpisodeRef.episodeId
        )
    }

    override fun setInitialState() = OverviewHomeContract.State()

    override fun handleEvents(event: OverviewHomeContract.Event) {
        when (event) {
            OverviewHomeContract.Event.ReadBookButtonClicked -> {
                setEffect {
                    OverviewHomeContract.Effect.Navigation.NavigateViewerContentScreen(
                        episodeRef = episodeRef,
                        bookTitle = testBookTitle,
                        episodeTitle = testEpisodeTitle
                    )
                }
            }

            OverviewHomeContract.Event.ReviewMoreButtonClicked -> {
                setEffect {
                    OverviewHomeContract.Effect.Navigation.NavigateReviewListScreen(
                        userId = episodeRef.userId,
                        bookId = episodeRef.bookId
                    )
                }
            }
        }
    }

    init {
        launchWithLoading {
            useCaseMakeBook.makeSampleEpisode()
            val bookInfo = useCaseLoadBook.loadBookInfo(episodeRef)
            val coverImageFile = if(bookInfo.introduce.coverList.isNotEmpty()){
                useCaseLoadBook.loadCoverImage(episodeRef, bookInfo.introduce.coverList[0])
            }else null

            setState {
                copy(
                    bookInfo = bookInfo,
                    coverImageFile = coverImageFile
                )
            }
        }
    }
}
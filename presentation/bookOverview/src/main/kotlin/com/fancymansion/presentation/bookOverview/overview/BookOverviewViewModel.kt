package com.fancymansion.presentation.bookOverview.overview

import androidx.lifecycle.SavedStateHandle
import com.fancymansion.core.common.const.ArgName
import com.fancymansion.core.common.const.EpisodeRef
import com.fancymansion.core.common.const.ReadMode
import com.fancymansion.core.presentation.base.BaseViewModel
import com.fancymansion.domain.usecase.book.UseCaseLoadBook
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class BookOverviewViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val useCaseLoadBook: UseCaseLoadBook
) : BaseViewModel<BookOverviewContract.State, BookOverviewContract.Event, BookOverviewContract.Effect>() {

    private var episodeRef : EpisodeRef = savedStateHandle.run {
        EpisodeRef(
            get<String>(ArgName.NAME_USER_ID)!!,
            get<ReadMode>(ArgName.NAME_READ_MODE)!!,
            get<String>(ArgName.NAME_BOOK_ID)!!,
            get<String>(ArgName.NAME_EPISODE_ID)!!
        )
    }

    override fun setInitialState() = BookOverviewContract.State()

    override fun handleEvents(event: BookOverviewContract.Event) {
        when (event) {
            BookOverviewContract.Event.ReadBookButtonClicked -> {
                setEffect {
                    BookOverviewContract.Effect.Navigation.NavigateViewerContentScreen(
                        episodeRef = episodeRef,
                        bookTitle = uiState.value.bookInfo!!.introduce.title,
                        episodeTitle = ""
                    )
                }
            }

            BookOverviewContract.Event.ReviewMoreButtonClicked -> {
                setEffect {
                    BookOverviewContract.Effect.Navigation.NavigateReviewListScreen(
                        userId = episodeRef.userId,
                        bookId = episodeRef.bookId
                    )
                }
            }
        }
    }

    init {
        launchWithInit {
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
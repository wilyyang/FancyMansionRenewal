package com.fancymansion.presentation.editor.bookOverview

import androidx.lifecycle.SavedStateHandle
import com.fancymansion.core.common.const.ArgName
import com.fancymansion.core.common.const.EpisodeRef
import com.fancymansion.core.common.const.testEpisodeRef
import com.fancymansion.core.presentation.base.BaseViewModel
import com.fancymansion.domain.usecase.book.UseCaseLoadBook
import com.fancymansion.domain.usecase.book.UseCaseMakeBook
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class EditorBookOverviewViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val useCaseLoadBook: UseCaseLoadBook,
    private val useCaseMakeBook: UseCaseMakeBook
) : BaseViewModel<EditorBookOverviewContract.State, EditorBookOverviewContract.Event, EditorBookOverviewContract.Effect>() {
    private var episodeRef : EpisodeRef = savedStateHandle.run {
        EpisodeRef(
            get<String>(ArgName.NAME_USER_ID)?.ifBlank { testEpisodeRef.userId } ?: testEpisodeRef.userId,
            testEpisodeRef.mode, //get<ReadMode>(NAME_READ_MODE)
            get<String>(ArgName.NAME_BOOK_ID)?.ifBlank { testEpisodeRef.bookId } ?: testEpisodeRef.bookId,
            get<String>(ArgName.NAME_EPISODE_ID)?.ifBlank { testEpisodeRef.episodeId } ?: testEpisodeRef.episodeId
        )
    }

    override fun setInitialState() = EditorBookOverviewContract.State()

    override fun handleEvents(event: EditorBookOverviewContract.Event) {
        when (event) {
            EditorBookOverviewContract.Event.BookOverviewButtonClicked -> {
                setEffect {
                    EditorBookOverviewContract.Effect.Navigation.NavigateOverviewScreen(
                        episodeRef = episodeRef
                    )
                }
            }
        }
    }

    init {
        launchWithInit {
            useCaseMakeBook.makeSampleEpisode()
            val bookInfo = useCaseLoadBook.loadBookInfo(episodeRef)

            setState {
                copy(
                    bookInfo = bookInfo
                )
            }
        }
    }
}
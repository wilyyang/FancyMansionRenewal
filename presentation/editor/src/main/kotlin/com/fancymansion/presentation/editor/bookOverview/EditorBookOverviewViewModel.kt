package com.fancymansion.presentation.editor.bookOverview

import androidx.lifecycle.SavedStateHandle
import com.fancymansion.core.common.const.ArgName
import com.fancymansion.core.common.const.EpisodeRef
import com.fancymansion.core.common.const.ImagePickType
import com.fancymansion.core.common.const.testEpisodeRef
import com.fancymansion.core.presentation.base.BaseViewModel
import com.fancymansion.domain.usecase.book.UseCaseLoadBook
import com.fancymansion.domain.usecase.book.UseCaseMakeBook
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import java.io.File
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

            EditorBookOverviewContract.Event.OverviewInfoSaveToFile -> {
                launchWithLoading {
                    delay(2000L)
                    uiState.value.bookInfo?.let { book ->
                        when(uiState.value.imagePickType){
                            is ImagePickType.Empty -> {
                                        val coverName = book.introduce.coverList.getOrNull(0)
                                        if(coverName != null){
                                            useCaseMakeBook.makeBookInfo(episodeRef, book.copy(
                                                introduce = book.introduce.copy(
                                                    coverList = listOf()
                                                )
                                            ))

                                            useCaseMakeBook.deleteCoverImage(episodeRef, coverName)

                                            val bookInfo = useCaseLoadBook.loadBookInfo(episodeRef)
                                    val bookCoverFile: File? =
                                        if (bookInfo.introduce.coverList.isNotEmpty()) useCaseLoadBook.loadCoverImage(
                                            episodeRef,
                                            bookInfo.introduce.coverList[0]
                                        ) else null

                                    setState {
                                        copy(
                                            bookInfo = bookInfo,
                                            imagePickType = if (bookCoverFile != null) ImagePickType.SavedImage(
                                                bookCoverFile
                                            ) else ImagePickType.Empty
                                        )
                                    }
                                }

                            }
                            is ImagePickType.GalleryUri -> {

                            }
                            is ImagePickType.SavedImage -> {

                            }
                        }
                    }
                }
            }

            /**
             * Gallery
             */
            EditorBookOverviewContract.Event.GalleryBookCoverPickerRequest -> {
                setEffect{
                    EditorBookOverviewContract.Effect.GalleryBookCoverPickerEffect
                }
            }

            is EditorBookOverviewContract.Event.GalleryBookCoverPickerResult -> {
                setState {
                    copy(
                        imagePickType =
                        if (event.imageUri != null) ImagePickType.GalleryUri(event.imageUri)
                        else ImagePickType.Empty
                    )
                }
            }

            EditorBookOverviewContract.Event.CoverImageReset -> {
                setState {
                    copy(
                        imagePickType = ImagePickType.Empty
                    )
                }
            }
        }
    }

    init {
        launchWithInit {
            useCaseMakeBook.makeSampleEpisode()
            val bookInfo = useCaseLoadBook.loadBookInfo(episodeRef)
            val bookCoverFile: File? =
                if (bookInfo.introduce.coverList.isNotEmpty()) useCaseLoadBook.loadCoverImage(
                    episodeRef,
                    bookInfo.introduce.coverList[0]
                ) else null

            setState {
                copy(
                    bookInfo = bookInfo,
                    imagePickType = if (bookCoverFile != null) ImagePickType.SavedImage(
                        bookCoverFile
                    ) else ImagePickType.Empty
                )
            }
        }
    }
}
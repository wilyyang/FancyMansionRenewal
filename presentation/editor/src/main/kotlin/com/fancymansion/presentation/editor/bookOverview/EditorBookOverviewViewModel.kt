package com.fancymansion.presentation.editor.bookOverview

import androidx.lifecycle.SavedStateHandle
import com.fancymansion.core.common.const.ArgName
import com.fancymansion.core.common.const.EpisodeRef
import com.fancymansion.core.common.const.ImagePickType
import com.fancymansion.core.common.const.testEpisodeRef
import com.fancymansion.core.common.resource.StringValue
import com.fancymansion.core.presentation.base.BaseViewModel
import com.fancymansion.core.presentation.base.LoadState
import com.fancymansion.domain.model.book.BookInfoModel
import com.fancymansion.domain.usecase.book.UseCaseLoadBook
import com.fancymansion.domain.usecase.book.UseCaseMakeBook
import com.fancymansion.presentation.editor.R
import dagger.hilt.android.lifecycle.HiltViewModel
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
                launchWithLoading(endLoadState = null) {
                    val isComplete: Boolean =
                        uiState.value.bookInfo?.let { book ->
                            updateBookInfoAndReload(book, uiState.value.imagePickType)
                        } ?: false

                    setLoadState(
                        loadState = LoadState.AlarmDialog(
                            title = StringValue.StringResource(com.fancymansion.core.common.R.string.book_file_save_result_title),
                            message = StringValue.StringResource(if (isComplete) R.string.dialog_save_complete_book_info else R.string.dialog_save_fail_book_info),
                            confirmText = StringValue.StringResource(com.fancymansion.core.common.R.string.confirm),
                            onConfirm = ::setLoadStateIdle
                        )
                    )
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
            loadBookInfoFromFile()
        }
    }

    private suspend fun updateBookInfoAndReload(book: BookInfoModel, pickType: ImagePickType) : Boolean{
        val saveResult = updateBookCoverImage(book, pickType)
        if(saveResult){
            loadBookInfoFromFile()
        }
        return saveResult
    }

    private suspend fun updateBookCoverImage(
        book: BookInfoModel,
        pickType: ImagePickType
    ): Boolean {
        return when(pickType){
            is ImagePickType.SavedImage -> true
            else -> {
                if (!useCaseMakeBook.removeBookCover(episodeRef, book)) {
                    false
                } else if (pickType is ImagePickType.GalleryUri) {
                    useCaseMakeBook.createBookCover(episodeRef, book, pickType.uri)
                }else {
                    true
                }
            }
        }
    }

    private suspend fun loadBookInfoFromFile(){
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
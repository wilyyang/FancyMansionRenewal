package com.fancymansion.presentation.editor.bookOverview

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.SavedStateHandle
import com.fancymansion.core.common.const.ArgName
import com.fancymansion.core.common.const.EpisodeRef
import com.fancymansion.core.common.const.ImagePickType
import com.fancymansion.core.common.const.testEpisodeRef
import com.fancymansion.core.common.resource.StringValue
import com.fancymansion.core.presentation.base.BaseViewModel
import com.fancymansion.core.presentation.base.CommonEvent
import com.fancymansion.core.presentation.base.LoadState
import com.fancymansion.domain.model.book.BookInfoModel
import com.fancymansion.domain.model.book.KeywordModel
import com.fancymansion.domain.usecase.book.UseCaseGetTotalKeyword
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
    private val useCaseMakeBook: UseCaseMakeBook,
    private val useCaseGetTotalKeyword: UseCaseGetTotalKeyword
) : BaseViewModel<EditorBookOverviewContract.State, EditorBookOverviewContract.Event, EditorBookOverviewContract.Effect>() {
    private var isFirstResumeComplete : Boolean = false
    private var episodeRef: EpisodeRef = savedStateHandle.run {
        EpisodeRef(
            get<String>(ArgName.NAME_USER_ID)?.ifBlank { testEpisodeRef.userId }
                ?: testEpisodeRef.userId,
            testEpisodeRef.mode, //get<ReadMode>(NAME_READ_MODE)
            get<String>(ArgName.NAME_BOOK_ID)?.ifBlank { testEpisodeRef.bookId }
                ?: testEpisodeRef.bookId,
            get<String>(ArgName.NAME_EPISODE_ID)?.ifBlank { testEpisodeRef.episodeId }
                ?: testEpisodeRef.episodeId
        )
    }

    val keywordStates: SnapshotStateList<KeywordState> = mutableStateListOf<KeywordState>()

    private lateinit var savedBookInfo: BookInfoModel
    private lateinit var savedPickType: ImagePickType

    override fun setInitialState() = EditorBookOverviewContract.State()

    override fun handleEvents(event: EditorBookOverviewContract.Event) {
        when (event) {
            EditorBookOverviewContract.Event.BookOverviewButtonClicked -> {
                checkBookInfoEdited {
                    setEffect {
                        EditorBookOverviewContract.Effect.Navigation.NavigateOverviewScreen(
                            episodeRef = episodeRef
                        )
                    }
                }
            }

            EditorBookOverviewContract.Event.OverviewInfoSaveToFile -> {
                launchWithLoading(endLoadState = null) {
                    val isComplete: Boolean =
                        uiState.value.bookInfo?.let { book ->
                            val newKeywordList =
                                keywordStates.filter { it.selected.value }.map { it.keyword }
                            updateBookInfoAndReload(
                                book,
                                uiState.value.imagePickType,
                                newKeywordList
                            )
                        } ?: false

                    setLoadState(
                        loadState = LoadState.AlarmDialog(
                            title = StringValue.StringResource(com.fancymansion.core.common.R.string.book_file_save_result_title),
                            message = StringValue.StringResource(if (isComplete) R.string.dialog_save_complete_book_info else R.string.dialog_save_fail_book_info),
                            dismissText = null,
                            confirmText = StringValue.StringResource(com.fancymansion.core.common.R.string.confirm),
                            onConfirm = ::setLoadStateIdle
                        )
                    )
                }
            }

            /**
             * Edit BookInfo
             */
            is EditorBookOverviewContract.Event.EditBookInfoTitle -> {
                uiState.value.bookInfo?.let { book ->
                    setState {
                        copy(
                            bookInfo = book.copy(
                                introduce = book.introduce.copy(
                                    title = event.title
                                )
                            )
                        )
                    }
                }
            }

            is EditorBookOverviewContract.Event.EditBookInfoDescription -> {
                uiState.value.bookInfo?.let { book ->
                    setState {
                        copy(
                            bookInfo = book.copy(
                                introduce = book.introduce.copy(
                                    description = event.description
                                )
                            )
                        )
                    }
                }
            }

            is EditorBookOverviewContract.Event.EditBookInfoKeywordState -> {
                keywordStates.firstOrNull { it.keyword.id == event.keywordId }
                    ?.let { keywordState ->
                        keywordState.selected.value = event.requestSelect
                    }
            }

            /**
             * Navigate Editor Page
             */
            is EditorBookOverviewContract.Event.EditorPageContentButtonClicked -> {
                checkBookInfoEdited {
                    setEffect {
                        EditorBookOverviewContract.Effect.Navigation.NavigateEditorPageContentScreen(
                            episodeRef = episodeRef,
                            bookTitle = uiState.value.bookInfo!!.introduce.title,
                            pageId = event.pageId
                        )
                    }
                }
            }

            EditorBookOverviewContract.Event.EditorPageListClicked -> {
                checkBookInfoEdited {
                    setEffect {
                        EditorBookOverviewContract.Effect.Navigation.NavigateEditorPageListScreen(
                            episodeRef = episodeRef,
                            bookTitle = uiState.value.bookInfo!!.introduce.title,
                            isEditMode = false
                        )
                    }
                }
            }

            EditorBookOverviewContract.Event.EditorPageListEditModeClicked -> {
                checkBookInfoEdited {
                    setEffect {
                        EditorBookOverviewContract.Effect.Navigation.NavigateEditorPageListScreen(
                            episodeRef = episodeRef,
                            bookTitle = uiState.value.bookInfo!!.introduce.title,
                            isEditMode = true
                        )
                    }
                }
            }

            /**
             * Gallery
             */
            EditorBookOverviewContract.Event.GalleryBookCoverPickerRequest -> {
                setEffect {
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

    override fun handleCommonEvents(event: CommonEvent) {
        when(event){
            is CommonEvent.OnResume -> {
                if(isFirstResumeComplete){
                    launchWithLoading {
                        useCaseGetTotalKeyword().forEach {
                            keywordStates.add(createKeywordState(it, false))
                        }
                        loadBookInfoFromFile()
                    }
                }else{
                    isFirstResumeComplete = true
                }
            }
            else -> super.handleCommonEvents(event)
        }
    }

    init {
        launchWithInit {
            useCaseMakeBook.makeSampleEpisode()
            useCaseGetTotalKeyword().forEach {
                keywordStates.add(createKeywordState(it, false))
            }
            loadBookInfoFromFile()
        }
    }

    private suspend fun updateBookInfoAndReload(
        book: BookInfoModel,
        pickType: ImagePickType,
        newKeywordList: List<KeywordModel>
    ): Boolean {
        val saveResult = updateBookInfo(book, pickType, newKeywordList)
        if (saveResult) {
            loadBookInfoFromFile()
        }
        return saveResult
    }

    private suspend fun updateBookInfo(
        book: BookInfoModel,
        pickType: ImagePickType,
        newKeywordList: List<KeywordModel>
    ): Boolean {
        return updateBookInfoDetail(book, newKeywordList) && updateBookCoverImage(book, pickType)
    }

    private suspend fun updateBookInfoDetail(
        book: BookInfoModel,
        newKeywordList: List<KeywordModel>
    ): Boolean {
        return useCaseMakeBook.makeBookInfo(
            episodeRef, book.copy(
                introduce = book.introduce.copy(
                    keywordList = newKeywordList
                )
            )
        )
    }

    private suspend fun updateBookCoverImage(
        book: BookInfoModel,
        pickType: ImagePickType
    ): Boolean {
        return when (pickType) {
            is ImagePickType.SavedImage -> true
            else -> {
                if (!useCaseMakeBook.removeBookCover(episodeRef, book)) {
                    false
                } else if (pickType is ImagePickType.GalleryUri) {
                    useCaseMakeBook.createBookCover(episodeRef, book, pickType.uri)
                } else {
                    true
                }
            }
        }
    }

    private suspend fun loadBookInfoFromFile() {
        val bookInfo = useCaseLoadBook.loadBookInfo(episodeRef)
        val bookCoverFile: File? =
            if (bookInfo.introduce.coverList.isNotEmpty()) useCaseLoadBook.loadCoverImage(
                episodeRef,
                bookInfo.introduce.coverList[0]
            ) else null

        savedBookInfo = useCaseLoadBook.loadBookInfo(episodeRef)
        savedPickType = if (bookCoverFile != null) ImagePickType.SavedImage(
            bookCoverFile
        ) else ImagePickType.Empty

        val pageBriefList = useCaseLoadBook.loadLogic(episodeRef).logics.map {
            PageBrief(
                id = it.pageId,
                title = it.title,
                type = it.type,
                selectorCount = it.selectors.size
            )
        }

        keywordStates.forEach { it.selected.value = false }
        bookInfo.introduce.keywordList.forEach { bookKeyword ->
            keywordStates.firstOrNull { keywordState ->
                bookKeyword.id == keywordState.keyword.id
            }?.let {
                it.selected.value = true
            }
        }

        setState {
            copy(
                bookInfo = bookInfo,
                pageBriefList = pageBriefList,
                imagePickType = if (bookCoverFile != null) ImagePickType.SavedImage(
                    bookCoverFile
                ) else ImagePickType.Empty
            )
        }
    }

    private fun checkBookInfoEdited(onCheckComplete: () -> Unit) {
        val newKeywordList = keywordStates.filter { it.selected.value }.map { it.keyword }
        if (savedBookInfo != uiState.value.bookInfo
            || savedPickType != uiState.value.imagePickType
            || newKeywordList != uiState.value.bookInfo!!.introduce.keywordList
        ) {
            setLoadState(
                loadState = LoadState.AlarmDialog(
                    title = StringValue.StringResource(com.fancymansion.core.common.R.string.book_file_edited_info_title),
                    message = StringValue.StringResource(R.string.dialog_save_edited_book_info),
                    confirmText = StringValue.StringResource(com.fancymansion.core.common.R.string.confirm),
                    onConfirm = {
                        //수정 중인 정보 파일 저장
                        launchWithLoading {
                            updateBookInfoAndReload(
                                uiState.value.bookInfo!!,
                                uiState.value.imagePickType,
                                newKeywordList
                            )
                            setLoadStateIdle()
                            onCheckComplete()
                        }
                    },
                    dismissText = StringValue.StringResource(com.fancymansion.core.common.R.string.cancel),
                    onDismiss = {
                        //수정 중인 정보 삭제
                        launchWithLoading {
                            loadBookInfoFromFile()
                            setLoadStateIdle()
                            onCheckComplete()
                        }
                    }
                )
            )
        } else {
            onCheckComplete()
        }
    }
}
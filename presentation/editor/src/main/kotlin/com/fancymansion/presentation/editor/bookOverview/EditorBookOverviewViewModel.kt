package com.fancymansion.presentation.editor.bookOverview

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.SavedStateHandle
import com.fancymansion.core.common.const.ArgName
import com.fancymansion.core.common.const.EpisodeRef
import com.fancymansion.core.common.const.INIT_PUBLISHED_AT
import com.fancymansion.core.common.const.INIT_UPDATED_AT
import com.fancymansion.core.common.const.INIT_VERSION
import com.fancymansion.core.common.const.ImagePickType
import com.fancymansion.core.common.const.PublishStatus
import com.fancymansion.core.common.const.ReadMode
import com.fancymansion.core.common.const.getEpisodeId
import com.fancymansion.core.common.resource.StringValue
import com.fancymansion.core.presentation.base.BaseViewModel
import com.fancymansion.core.presentation.base.CommonEvent
import com.fancymansion.core.presentation.base.LoadState
import com.fancymansion.domain.model.book.BookInfoModel
import com.fancymansion.domain.model.book.BookMetaModel
import com.fancymansion.domain.model.book.KeywordModel
import com.fancymansion.domain.usecase.book.UseCaseGetTotalKeyword
import com.fancymansion.domain.usecase.book.UseCaseLoadBook
import com.fancymansion.domain.usecase.book.UseCaseMakeBook
import com.fancymansion.domain.usecase.remoteBook.UseCaseUpdateBook
import com.fancymansion.domain.usecase.remoteBook.UseCaseUploadBook
import com.fancymansion.domain.usecase.remoteBook.UseCaseWithdrawBook
import com.fancymansion.domain.usecase.user.UseCaseAddPublishedBookId
import com.fancymansion.domain.usecase.user.UseCaseGetPublishedBookIds
import com.fancymansion.domain.usecase.user.UseCaseRemovePublishedBookId
import com.fancymansion.presentation.editor.R
import dagger.hilt.android.lifecycle.HiltViewModel
import java.io.File
import javax.inject.Inject

@HiltViewModel
class EditorBookOverviewViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val useCaseLoadBook: UseCaseLoadBook,
    private val useCaseMakeBook: UseCaseMakeBook,
    private val useCaseUploadBook: UseCaseUploadBook,
    private val useCaseWithdrawBook: UseCaseWithdrawBook,
    private val useCaseUpdateBook: UseCaseUpdateBook,
    private val useCaseGetTotalKeyword: UseCaseGetTotalKeyword,
    private val useCaseGetPublishedBookIds: UseCaseGetPublishedBookIds,
    private val useCaseAddPublishedBookId: UseCaseAddPublishedBookId,
    private val useCaseRemovePublishedBookId: UseCaseRemovePublishedBookId
) : BaseViewModel<EditorBookOverviewContract.State, EditorBookOverviewContract.Event, EditorBookOverviewContract.Effect>() {
    private var isUpdateResume : Boolean = false
    private var episodeRef: EpisodeRef = savedStateHandle.run {
        EpisodeRef(
            get<String>(ArgName.NAME_USER_ID)!!,
            get<ReadMode>(ArgName.NAME_READ_MODE)!!,
            get<String>(ArgName.NAME_BOOK_ID)!!,
            get<String>(ArgName.NAME_EPISODE_ID)!!
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
                        uiState.value.bookInfo.let { book ->
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

            EditorBookOverviewContract.Event.UploadBookFile -> handleUploadBookFile()
            EditorBookOverviewContract.Event.WithdrawBookFile -> handleWithdrawBookFile()
            EditorBookOverviewContract.Event.UpdateBookFile -> handleUpdateBookFile()

            /**
             * Edit BookInfo
             */
            is EditorBookOverviewContract.Event.EditBookInfoTitle -> {
                uiState.value.bookInfo.let { book ->
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
                uiState.value.bookInfo.let { book ->
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
                    isUpdateResume = true
                    setEffect {
                        EditorBookOverviewContract.Effect.Navigation.NavigateEditorPageContentScreen(
                            episodeRef = episodeRef,
                            bookTitle = uiState.value.bookInfo.introduce.title,
                            pageId = event.pageId
                        )
                    }
                }
            }

            EditorBookOverviewContract.Event.EditorPageListClicked -> {
                checkBookInfoEdited {
                    isUpdateResume = true
                    setEffect {
                        EditorBookOverviewContract.Effect.Navigation.NavigateEditorPageListScreen(
                            episodeRef = episodeRef,
                            bookTitle = uiState.value.bookInfo.introduce.title,
                            isEditMode = false
                        )
                    }
                }
            }

            EditorBookOverviewContract.Event.EditorPageListEditModeClicked -> {
                checkBookInfoEdited {
                    isUpdateResume = true
                    setEffect {
                        EditorBookOverviewContract.Effect.Navigation.NavigateEditorPageListScreen(
                            episodeRef = episodeRef,
                            bookTitle = uiState.value.bookInfo.introduce.title,
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
                if(isUpdateResume){
                    isUpdateResume = false
                    launchWithLoading {
                        loadBookInfoFromFile()
                    }
                }
            }
            is CommonEvent.CloseEvent -> {
                if(uiState.value.isInitSuccess){
                    checkBookInfoEdited {
                        super.handleCommonEvents(CommonEvent.CloseEvent)
                    }
                }else{
                    super.handleCommonEvents(CommonEvent.CloseEvent)
                }
            }
            else -> super.handleCommonEvents(event)
        }
    }

    init {
        launchWithInit {

            useCaseGetTotalKeyword().forEach {
                keywordStates.add(createKeywordState(it, false))
            }
            loadBookInfoFromFile()
            setState {
                copy(
                    isInitSuccess = bookInfo.id.isNotBlank()
                )
            }
        }
    }

    private fun handleUploadBookFile(){
        launchWithLoading {
            val newKeywordList = keywordStates.filter { it.selected.value }.map { it.keyword }
            updateBookInfoAndReload(
                uiState.value.bookInfo,
                uiState.value.imagePickType,
                newKeywordList
            )
            val publishedId = useCaseUploadBook(episodeRef = episodeRef)
            episodeRef = episodeRef.copy(bookId = publishedId, episodeId = getEpisodeId(publishedId))
            val currentTime = System.currentTimeMillis()
            useCaseMakeBook.makeMetaData(
                userId = episodeRef.userId,
                mode = episodeRef.mode,
                bookId = episodeRef.bookId,
                metaData = BookMetaModel(
                    status = PublishStatus.PUBLISHED,
                    publishedAt = currentTime,
                    updatedAt = currentTime,
                    version = 0
                )
            )
            useCaseAddPublishedBookId(episodeRef.userId, episodeRef.bookId)

            useCaseGetTotalKeyword().forEach {
                keywordStates.add(createKeywordState(it, false))
            }
            loadBookInfoFromFile()
        }
    }

    private fun handleWithdrawBookFile() {
        launchWithLoading {
            useCaseWithdrawBook(episodeRef.userId, episodeRef.bookId)
            val newMetaData = BookMetaModel(
                status = PublishStatus.UNPUBLISHED,
                publishedAt = INIT_PUBLISHED_AT,
                updatedAt = INIT_UPDATED_AT,
                version = INIT_VERSION
            )
            useCaseMakeBook.makeMetaData(
                userId = episodeRef.userId,
                mode = episodeRef.mode,
                bookId = episodeRef.bookId,
                metaData = newMetaData
            )
            useCaseRemovePublishedBookId(episodeRef.userId, episodeRef.bookId)
            val publishedBookIds = useCaseGetPublishedBookIds()
            setState {
                copy(
                    isPublished = bookInfo.id in publishedBookIds,
                    metadata = newMetaData
                )
            }
        }
    }

    private fun handleUpdateBookFile() {
        launchWithLoading {
            // 임시 코드
            val newKeywordList = keywordStates.filter { it.selected.value }.map { it.keyword }
            updateBookInfoAndReload(
                uiState.value.bookInfo,
                uiState.value.imagePickType,
                newKeywordList
            )
            val newVersion = useCaseUpdateBook(episodeRef)
            val newMetaData = useCaseLoadBook.loadBookMetaData(
                episodeRef.userId,
                episodeRef.mode,
                episodeRef.bookId
            ).copy(
                updatedAt = System.currentTimeMillis(),
                version = newVersion
            )
            useCaseMakeBook.makeMetaData(
                userId = episodeRef.userId,
                mode = episodeRef.mode,
                bookId = episodeRef.bookId,
                metaData = newMetaData
            )
            setState {
                copy(
                    metadata = newMetaData
                )
            }
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
        val bookWithUpdatedKeywords = book.copy(
            introduce = book.introduce.copy(
                keywordList = newKeywordList
            )
        )
        return useCaseMakeBook.makeBookInfo(
            episodeRef, bookWithUpdatedKeywords
        ) && updateBookCoverImage(bookWithUpdatedKeywords, pickType)
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

        val loadKeywords = useCaseGetTotalKeyword()
        val loadSelectedKeywords = bookInfo.introduce.keywordList
        val modifiedSelectedKeywords = keywordStates.filter { it.selected.value }.map { it.keyword }

        if(keywordStates.size != loadKeywords.size || loadSelectedKeywords != modifiedSelectedKeywords){
            keywordStates.apply {
                clear()
                addAll(loadKeywords.map { createKeywordState(it, false) })
            }

            loadSelectedKeywords.forEach { bookKeyword ->
                keywordStates.find { it.keyword.id == bookKeyword.id }?.selected?.value = true
            }
        }

        val publishedBookIds = useCaseGetPublishedBookIds()
        val metadata = useCaseLoadBook.loadBookMetaData(episodeRef.userId, episodeRef.mode, episodeRef.bookId)
        setState {
            copy(
                isPublished = bookInfo.id in publishedBookIds,
                bookInfo = bookInfo,
                metadata = metadata,
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
            || newKeywordList != uiState.value.bookInfo.introduce.keywordList
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
                                uiState.value.bookInfo,
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
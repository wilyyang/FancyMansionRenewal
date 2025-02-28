package com.fancymansion.presentation.editor.pageContent

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.SavedStateHandle
import com.fancymansion.core.common.const.ArgName
import com.fancymansion.core.common.const.EpisodeRef
import com.fancymansion.core.common.const.ImagePickType
import com.fancymansion.core.common.const.PageType
import com.fancymansion.core.common.const.ReadMode
import com.fancymansion.core.common.resource.StringValue
import com.fancymansion.core.presentation.base.BaseViewModel
import com.fancymansion.core.presentation.base.CommonEvent
import com.fancymansion.core.presentation.base.LoadState
import com.fancymansion.domain.model.book.PageModel
import com.fancymansion.domain.model.book.SourceModel
import com.fancymansion.domain.usecase.book.UseCaseBookLogic
import com.fancymansion.domain.usecase.book.UseCaseLoadBook
import com.fancymansion.domain.usecase.book.UseCaseMakeBook
import com.fancymansion.domain.usecase.util.UseCaseGetResource
import com.fancymansion.presentation.editor.R
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class EditorPageContentViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val useCaseLoadBook: UseCaseLoadBook,
    private val useCaseMakeBook: UseCaseMakeBook,
    private val useCaseBookLogic: UseCaseBookLogic,
    private val useCaseGetResource: UseCaseGetResource
) : BaseViewModel<EditorPageContentContract.State, EditorPageContentContract.Event, EditorPageContentContract.Effect>() {

    private var isUpdateResume = false
    private lateinit var originPage : PageModel
    private lateinit var originPageType : PageType
    val contentSourceStates: SnapshotStateList<SourceWrapper> = mutableStateListOf()

    private val episodeRef: EpisodeRef = savedStateHandle.run {
        EpisodeRef(
            requireNotNull(get<String>(ArgName.NAME_USER_ID)),
            requireNotNull(get<ReadMode>(ArgName.NAME_READ_MODE)),
            requireNotNull(get<String>(ArgName.NAME_BOOK_ID)),
            requireNotNull(get<String>(ArgName.NAME_EPISODE_ID))
        )
    }

    init {
        initializeState()
    }

    override fun setInitialState() = EditorPageContentContract.State()

    override fun handleEvents(event: EditorPageContentContract.Event) {
        when (event) {
            EditorPageContentContract.Event.OnClickSavePageToFile -> handlePageContentSaveToFile()

            EditorPageContentContract.Event.ReadPagePreviewClicked -> {
                checkPageContentEdited {
                    setEffect {
                        EditorPageContentContract.Effect.Navigation.NavigateViewerContentScreen(
                            episodeRef = episodeRef,
                            bookTitle = uiState.value.bookTitle,
                            episodeTitle = "",
                            pageId = originPage.id
                        )
                    }
                }
            }

            is EditorPageContentContract.Event.EditPageContentTitle -> {
                setState {
                    copy(
                        pageTitle = event.title
                    )
                }
            }

            is EditorPageContentContract.Event.OnSelectPageType -> {
                if(originPageType == PageType.START){
                    setLoadState(LoadState.AlarmDialog(
                        title = StringValue.StringResource(R.string.edit_page_content_page_type_dialog_title),
                        message = StringValue.StringResource(R.string.edit_page_content_page_type_dialog_text_start_not_edit),
                        onConfirm = {
                            setLoadStateIdle()
                        },
                        dismissText = null
                    ))

                }else if (event.pageType == PageType.START){
                    setLoadState(LoadState.AlarmDialog(
                        title = StringValue.StringResource(R.string.edit_page_content_page_type_dialog_title),
                        message = StringValue.StringResource(R.string.edit_page_content_page_type_dialog_text_change_to_start),
                        onConfirm = {
                            setState {
                                copy(
                                    pageType = event.pageType
                                )
                            }
                            setLoadStateIdle()
                        },
                        dismissText = null
                    ))
                }else{
                    setState {
                        copy(
                            pageType = event.pageType
                        )
                    }
                }
            }

            is EditorPageContentContract.Event.MoveSourcePosition -> {
                contentSourceStates.apply {
                    val item = removeAt(event.fromIndex)
                    add(event.toIndex, item)
                }
            }

            EditorPageContentContract.Event.OnClickAddSource -> {
                setEffect { EditorPageContentContract.Effect.ShowAddSourceDialogEffect }
            }

            EditorPageContentContract.Event.AddTextSourceEvent -> {
                contentSourceStates.add(SourceWrapper.TextWrapper(mutableStateOf("")))

                val lastIndex = contentSourceStates.size - 1
                setEffect {
                    EditorPageContentContract.Effect.ShowSourceTextEffect(
                        lastIndex,
                        contentSourceStates[lastIndex]
                    )
                }
            }

            EditorPageContentContract.Event.AddImageSourceEvent -> {
                contentSourceStates.add(SourceWrapper.ImageWrapper(ImagePickType.Empty))

                val lastIndex = contentSourceStates.size - 1
                setEffect {
                    EditorPageContentContract.Effect.ShowSourceImageEffect(
                        lastIndex,
                        contentSourceStates[lastIndex]
                    )
                }
            }

            is EditorPageContentContract.Event.OnClickSelectorList -> {
                setEffect {
                    EditorPageContentContract.Effect.Navigation.NavigateSelectorListScreen(
                        episodeRef = episodeRef,
                        bookTitle = uiState.value.bookTitle,
                        episodeTitle = "",
                        pageId = originPage.id
                    )
                }
            }

            is EditorPageContentContract.Event.OnClickSourceText -> {
                contentSourceStates.getOrNull(event.sourceIndex)?.let { source ->
                    if (source is SourceWrapper.TextWrapper) {
                        setEffect {
                            EditorPageContentContract.Effect.ShowSourceTextEffect(
                                event.sourceIndex,
                                source
                            )
                        }
                    }
                }
            }

            is EditorPageContentContract.Event.OnClickSourceImage -> {
                contentSourceStates.getOrNull(event.sourceIndex)?.let { source ->
                    if (source is SourceWrapper.ImageWrapper) {
                        setEffect {
                            EditorPageContentContract.Effect.ShowSourceImageEffect(
                                event.sourceIndex,
                                source
                            )
                        }
                    }
                }
            }

            is EditorPageContentContract.Event.OnClickDeleteSource -> {
                contentSourceStates.removeAt(event.sourceIndex)
            }

            is EditorPageContentContract.Event.EditSourceText -> {
                contentSourceStates.getOrNull(event.sourceIndex)?.let { source ->
                    (source as SourceWrapper.TextWrapper).description.value = event.text
                }
            }

            is EditorPageContentContract.Event.EditSourceImage -> {
                setEffect {
                    EditorPageContentContract.Effect.GallerySourceImagePickerEffect
                }
            }

            is EditorPageContentContract.Event.GalleryPickerResult -> {
                contentSourceStates.getOrNull(event.sourceIndex)?.let { source ->
                    if(source is SourceWrapper.ImageWrapper){
                        val newImageSource = SourceWrapper.ImageWrapper(
                            if (event.imageUri != null) ImagePickType.GalleryUri(event.imageUri)
                            else ImagePickType.Empty
                        )

                        contentSourceStates.add(event.sourceIndex, newImageSource)
                        contentSourceStates.removeAt(event.sourceIndex + 1)

                        setEffect {
                            EditorPageContentContract.Effect.UpdateSourceImage(event.sourceIndex, newImageSource)
                        }
                    }
                }
            }
        }
    }

    override fun handleCommonEvents(event: CommonEvent) {
        when(event){
            is CommonEvent.OnResume -> handleOnResume()
            is CommonEvent.CloseEvent -> {
                if(uiState.value.isInitSuccess){
                    checkPageContentEdited {
                        super.handleCommonEvents(CommonEvent.CloseEvent)
                    }
                }else{
                    super.handleCommonEvents(CommonEvent.CloseEvent)
                }
            }
            else -> super.handleCommonEvents(event)
        }
    }

    private fun initializeState() {
        launchWithInit {
            val pageId = requireNotNull(savedStateHandle.get<Long>(ArgName.NAME_PAGE_ID))
            val bookTitle = requireNotNull(savedStateHandle.get<String>(ArgName.NAME_BOOK_TITLE))
            loadPageContent(pageId)
            setState {
                copy(
                    bookTitle = bookTitle,
                    isInitSuccess = true
                )
            }
        }
    }

    private fun handlePageContentSaveToFile() = launchWithLoading(endLoadState = null) {
        val isComplete = saveEditedPageContentAndReload()
        setLoadState(
            loadState = LoadState.AlarmDialog(
                title = StringValue.StringResource(com.fancymansion.core.common.R.string.book_file_save_result_title),
                message = StringValue.StringResource(if (isComplete) R.string.dialog_save_complete_page else R.string.dialog_save_fail_page),
                dismissText = null,
                confirmText = StringValue.StringResource(com.fancymansion.core.common.R.string.confirm),
                onConfirm = ::setLoadStateIdle
            )
        )
    }


    // CommonEvent
    private suspend fun saveEditedPageContentAndReload() : Boolean{
        val result = saveEditedPageContent()
        loadPageContent(originPage.id)
        return result
    }

    private suspend fun saveEditedPageContent() : Boolean{
        // PageLogic 에 Type, Title 반영
        val logicResult = if (uiState.value.pageType != originPageType || uiState.value.pageTitle != originPage.title) {
            val editedPageLogic = uiState.value.pageLogic.copy(
                type = uiState.value.pageType,
                title = uiState.value.pageTitle
            )
            useCaseMakeBook.updateBookPageLogic(episodeRef, editedPageLogic)
        } else {
            true
        }

        // Uri 이미지 저장 및 List 변환
        val newSourceList = contentSourceStates.mapNotNull {
            when(it){
                is SourceWrapper.TextWrapper -> SourceModel.TextModel(it.description.value)
                is SourceWrapper.ImageWrapper -> {
                    when(it.imagePickType){
                        is ImagePickType.Empty -> null
                        is ImagePickType.GalleryUri -> {
                            val newImageName = useCaseMakeBook.makePageImage(episodeRef, originPage.id, it.imagePickType.uri)
                            SourceModel.ImageModel(imageName = newImageName)
                        }
                        is ImagePickType.SavedImage -> {
                            SourceModel.ImageModel(imageName = it.imagePickType.file.name)
                        }
                    }
                }
            }
        }
        // 안쓰는 이미지 삭제
        originPage.sources.filterIsInstance<SourceModel.ImageModel>().forEach {
            if(it !in newSourceList){
                useCaseMakeBook.deletePageImage(episodeRef, it.imageName)
            }
        }

        // Page 파일로 변환
        val newPage = originPage.copy(
            title = uiState.value.pageTitle,
            sources = newSourceList
        )
        return logicResult && useCaseMakeBook.updatePageContent(episodeRef, originPage.id, newPage)
    }

    private suspend fun loadPageContent(pageId : Long) {
        val pageLogic = useCaseLoadBook.loadPageLogic(episodeRef, pageId)!!
        originPageType = pageLogic.type
        useCaseLoadBook.loadPage(episodeRef, pageId = pageId).let { page ->
            originPage = page
            setState {
                copy(
                    pageTitle = page.title,
                    pageType = pageLogic.type,
                    pageLogic = pageLogic
                )
            }
            contentSourceStates.clear()
            convertSourcesToWrapper(page.sources).forEach { wrapper ->
                contentSourceStates.add(wrapper)
            }
        }
    }

    private suspend fun convertSourcesToWrapper(sources : List<SourceModel>) : List<SourceWrapper> {
        return sources.map { source ->
            when (source) {
                is SourceModel.TextModel -> {
                    SourceWrapper.TextWrapper(mutableStateOf(source.description))
                }

                is SourceModel.ImageModel -> {
                    SourceWrapper.ImageWrapper(
                        ImagePickType.SavedImage(
                            useCaseLoadBook.loadPageImage(
                                episodeRef,
                                source.imageName
                            )
                        )
                    )
                }
            }
        }
    }

    private fun handleOnResume() {
        if (isUpdateResume) {
            isUpdateResume = false
            launchWithLoading {
                loadPageContent(originPage.id)
            }
        }
    }

    private fun checkPageContentEdited(onCheckComplete: () -> Unit) {

        val stateNotEmpty = contentSourceStates.filterNot { it is SourceWrapper.ImageWrapper && it.imagePickType is ImagePickType.Empty }
        val isEdited = originPageType != uiState.value.pageType || originPage.title != uiState.value.pageTitle || originPage.sources.size != stateNotEmpty.size ||
                originPage.sources.withIndex().any { (index, sourceModel) ->
                    val wrapper = stateNotEmpty.getOrNull(index)
                    when {
                        sourceModel is SourceModel.TextModel && wrapper is SourceWrapper.TextWrapper ->
                            sourceModel.description != wrapper.description.value

                        sourceModel is SourceModel.ImageModel && wrapper is SourceWrapper.ImageWrapper &&
                                wrapper.imagePickType is ImagePickType.SavedImage ->
                            sourceModel.imageName != wrapper.imagePickType.file.name

                        else -> true
                    }
                }

        if (isEdited) {
            setLoadState(
                LoadState.AlarmDialog(
                    title = StringValue.StringResource(com.fancymansion.core.common.R.string.book_file_edited_info_title),
                    message = StringValue.StringResource(R.string.dialog_save_edited_book_info),
                    confirmText = StringValue.StringResource(com.fancymansion.core.common.R.string.confirm),
                    onConfirm = {
                        //수정 중인 정보 파일 저장
                        launchWithLoading {
                            saveEditedPageContentAndReload()
                            setLoadStateIdle()
                            onCheckComplete()
                        }
                    },
                    dismissText = StringValue.StringResource(com.fancymansion.core.common.R.string.cancel),
                    onDismiss = {
                        //수정 중인 정보 삭제
                        launchWithLoading {
                            loadPageContent(originPage.id)
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
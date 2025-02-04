package com.fancymansion.presentation.editor.pageContent.composables

import android.app.Activity
import android.content.Intent
import android.provider.MediaStore
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.fancymansion.core.presentation.base.CommonEvent
import com.fancymansion.core.presentation.base.LoadState
import com.fancymansion.core.presentation.base.SIDE_EFFECTS_KEY
import com.fancymansion.core.presentation.base.window.TypePane
import com.fancymansion.core.presentation.compose.component.FadeInOutSkeleton
import com.fancymansion.core.presentation.compose.frame.BaseScreen
import com.fancymansion.core.presentation.compose.frame.FancyMansionTopBar
import com.fancymansion.core.presentation.compose.modifier.clickSingle
import com.fancymansion.core.presentation.compose.shape.borderLine
import com.fancymansion.core.presentation.compose.theme.Paddings
import com.fancymansion.core.presentation.compose.theme.onSurfaceSub
import com.fancymansion.presentation.editor.R
import com.fancymansion.presentation.editor.pageContent.EditorPageContentContract
import com.fancymansion.presentation.editor.pageContent.SourceWrapper
import com.fancymansion.presentation.editor.pageContent.composables.part.CommonEditPageContentBottomDialog
import com.fancymansion.presentation.editor.pageContent.composables.part.SelectSourceDialog
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

@Composable
fun EditorPageContentScreenFrame(
    uiState: EditorPageContentContract.State,
    contentSourceStates: SnapshotStateList<SourceWrapper>,
    loadState: LoadState,
    effectFlow: SharedFlow<EditorPageContentContract.Effect>?,
    onCommonEventSent: (event: CommonEvent) -> Unit,
    onEventSent: (event: EditorPageContentContract.Event) -> Unit,
    onNavigationRequested: (EditorPageContentContract.Effect.Navigation) -> Unit
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner) {
        val lifecycle = lifecycleOwner.lifecycle
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                onCommonEventSent(CommonEvent.OnResume)
            }
        }

        lifecycle.addObserver(observer)
        onDispose {
            lifecycle.removeObserver(observer)
        }
    }

    val focusManager = LocalFocusManager.current
    val bottomDrawerState = remember { DrawerState(initialValue = DrawerValue.Closed) }
    val coroutineScope = rememberCoroutineScope()

    var bottomDialogIndex : Int by remember {
        mutableIntStateOf(-1)
    }
    var bottomDialogSource : SourceWrapper by remember {
        mutableStateOf(SourceWrapper.TextWrapper(mutableStateOf("")))
    }

    val launcherGalleryPicker = rememberLauncherForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            onEventSent(EditorPageContentContract.Event.GalleryPickerResult(sourceIndex = bottomDialogIndex, imageUri = result.data?.data))

        }
    }

    var isShowSourceDialog by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(SIDE_EFFECTS_KEY) {
        effectFlow?.onEach { effect ->
            when(effect){
                EditorPageContentContract.Effect.ShowAddSourceDialogEffect -> {
                    isShowSourceDialog = true
                }

                is EditorPageContentContract.Effect.ShowSourceTextEffect -> {
                    bottomDialogIndex = effect.sourceIndex
                    bottomDialogSource = effect.source
                    coroutineScope.launch {
                        bottomDrawerState.open()
                    }
                }
                is EditorPageContentContract.Effect.ShowSourceImageEffect -> {
                    bottomDialogIndex = effect.sourceIndex
                    bottomDialogSource = effect.source
                    coroutineScope.launch {
                        bottomDrawerState.open()
                    }
                }
                is EditorPageContentContract.Effect.Navigation -> {
                    onNavigationRequested(effect)
                }

                EditorPageContentContract.Effect.GallerySourceImagePickerEffect -> {
                    val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                    launcherGalleryPicker.launch(intent)
                }

                is EditorPageContentContract.Effect.UpdateSourceImage -> {
                    bottomDialogIndex = effect.sourceIndex
                    bottomDialogSource = effect.source
                }
            }
        }?.collect()
    }

    BaseScreen(
        loadState = loadState,
        description = EditorPageContentContract.NAME,
        statusBarColor = MaterialTheme.colorScheme.surface,
        typePane = TypePane.MOBILE,
        initContent = {
            EditorPageContentSkeletonScreen()
        },
        topBar = {
            FancyMansionTopBar(
                typePane = TypePane.MOBILE,
                topBarColor = MaterialTheme.colorScheme.surface,
                idLeftIcon = com.fancymansion.core.presentation.R.drawable.ic_back,
                onClickLeftIcon = {
                    onCommonEventSent(CommonEvent.CloseEvent)
                },
                title = uiState.bookTitle,
                subTitle = stringResource(id = R.string.topbar_editor_sub_title),
                sideRightText = if(uiState.isInitSuccess) stringResource(id = R.string.topbar_editor_side_save) else null,
                onClickRightIcon = {
                    // TODO : Save Page Content Event
                },
                shadowElevation = 1.dp
            )
        },
        bottomDrawerState = bottomDrawerState,
        bottomDrawerBackground = {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = Color.Black.copy(alpha = 0.5f))
                    .clickSingle {
                        coroutineScope.launch {
                            focusManager.clearFocus()
                            bottomDrawerState.close()
                        }
                    })
        },
        bottomDrawerContent = {
            CommonEditPageContentBottomDialog(
                source = bottomDialogSource,
                onClickDeleteSource = {
                    focusManager.clearFocus()
                    coroutineScope.launch {
                        bottomDrawerState.close()
                    }
                    onEventSent(EditorPageContentContract.Event.OnClickDeleteSource(bottomDialogIndex))
                },
                updateSourceText = {
                    onEventSent(EditorPageContentContract.Event.EditSourceText(bottomDialogIndex, it))
                },
                onClickImagePick = {
                    onEventSent(EditorPageContentContract.Event.EditSourceImage(bottomDialogIndex))

                }
            )
        }
    ) {
        EditorPageContentScreenContent(
            modifier = Modifier.fillMaxSize(),
            uiState = uiState,
            contentSourceStates = contentSourceStates,
            onEventSent = onEventSent,
            onCommonEventSent = onCommonEventSent,
            focusManager = focusManager
        )
    }

    if(isShowSourceDialog){
        SelectSourceDialog(
            onTextSelected = {
                onEventSent(EditorPageContentContract.Event.AddTextSourceEvent)
                isShowSourceDialog = false
            },
            onImageSelected = {
                onEventSent(EditorPageContentContract.Event.AddImageSourceEvent)
                isShowSourceDialog = false
            },
            onCanceled = {
                isShowSourceDialog = false
            }
        )
    }

    BackHandler {
        if(bottomDrawerState.currentValue == DrawerValue.Open){
            coroutineScope.launch {
                bottomDrawerState.close()
            }
        }else if(isShowSourceDialog){
            isShowSourceDialog = false
        }else{
            onCommonEventSent(CommonEvent.CloseEvent)
        }
    }
}

@Composable
fun EditorPageContentSkeletonScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.surface),
    ) {
        FancyMansionTopBar(
            typePane = TypePane.MOBILE,
            topBarColor = MaterialTheme.colorScheme.surface,
            title = "",
            subTitle = stringResource(id = R.string.topbar_editor_sub_title),
            shadowElevation = 1.dp
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .borderLine(density = LocalDensity.current, color = onSurfaceSub, bottom = 0.3.dp)
                .padding(
                    vertical = Paddings.Basic.vertical,
                    horizontal = Paddings.Basic.horizontal
                )
        ) {
            FadeInOutSkeleton(
                modifier = Modifier
                    .padding(vertical = Paddings.Basic.vertical)
                    .height(18.dp)
                    .width(100.dp)
            )
        }
    }
}
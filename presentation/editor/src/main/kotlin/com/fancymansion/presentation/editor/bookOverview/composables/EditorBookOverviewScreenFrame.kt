package com.fancymansion.presentation.editor.bookOverview.composables

import android.app.Activity
import android.content.Intent
import android.provider.MediaStore
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
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
import com.fancymansion.presentation.editor.R
import com.fancymansion.presentation.editor.bookOverview.EditorBookOverviewContract
import com.fancymansion.presentation.editor.bookOverview.KeywordState
import com.fancymansion.presentation.editor.common.composables.CommonEditInfoTitle
import com.fancymansion.presentation.editor.common.itemMarginHeight
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

@Composable
fun EditorBookOverviewScreenFrame(
    uiState: EditorBookOverviewContract.State,
    keywordStates : SnapshotStateList<KeywordState>,
    loadState: LoadState,
    effectFlow: SharedFlow<EditorBookOverviewContract.Effect>?,
    onCommonEventSent: (event: CommonEvent) -> Unit,
    onEventSent: (event: EditorBookOverviewContract.Event) -> Unit,
    onNavigationRequested: (EditorBookOverviewContract.Effect.Navigation) -> Unit
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

    val launcherGalleryBookCoverPicker = rememberLauncherForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            onEventSent(EditorBookOverviewContract.Event.GalleryBookCoverPickerResult(imageUri = result.data?.data))
        }
    }

    LaunchedEffect(SIDE_EFFECTS_KEY) {
        effectFlow?.onEach { effect ->
            if(effect is EditorBookOverviewContract.Effect.Navigation){
                onNavigationRequested(effect)
            }else if(effect is EditorBookOverviewContract.Effect.GalleryBookCoverPickerEffect){
                val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                launcherGalleryBookCoverPicker.launch(intent)
            }
        }?.collect()
    }
    val focusManager = LocalFocusManager.current
    val bottomDrawerState = remember { DrawerState(initialValue = DrawerValue.Closed) }
    val coroutineScope = rememberCoroutineScope()
    BaseScreen(
        loadState = loadState,
        description = EditorBookOverviewContract.NAME,
        statusBarColor = MaterialTheme.colorScheme.surface,
        typePane = TypePane.MOBILE,
        initContent = {
            EditorBookOverviewSkeletonScreen()
        },
        topBar = {
            FancyMansionTopBar(
                typePane = TypePane.MOBILE,
                topBarColor = MaterialTheme.colorScheme.surface,
                idLeftIcon = com.fancymansion.core.presentation.R.drawable.ic_back,
                onClickLeftIcon = {
                    onCommonEventSent(CommonEvent.CloseEvent)
                },
                title = stringResource(id = R.string.topbar_editor_title_overview),
                subTitle = stringResource(id = R.string.topbar_editor_sub_title),
                sideRightText = stringResource(id = R.string.topbar_editor_side_save),
                onClickRightIcon = {
                    focusManager.clearFocus()
                    onEventSent(EditorBookOverviewContract.Event.OverviewInfoSaveToFile)
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
                            bottomDrawerState.close()
                        }
                    })
        },
        bottomDrawerContent = {
            BottomKeywordsDialog(
                keywordStates = keywordStates,
                onClickChip = { id, request ->
                    onEventSent(EditorBookOverviewContract.Event.EditBookInfoKeywordState(id, request))
                }
            )
        }
    ) {
        EditorBookOverviewScreenContent(
            modifier = Modifier.fillMaxSize(),
            uiState = uiState,
            keywordStates = keywordStates,
            onEventSent = onEventSent,
            onCommonEventSent = onCommonEventSent,
            onOpenEditKeywords = {
                coroutineScope.launch {
                    bottomDrawerState.open()
                }
            },
            focusManager = focusManager
        )
    }

    BackHandler {
        if(bottomDrawerState.currentValue == DrawerValue.Open){
            coroutineScope.launch {
                bottomDrawerState.close()
            }
        }else{
            onCommonEventSent(CommonEvent.CloseEvent)
        }
    }
}

@Composable
fun EditorBookOverviewSkeletonScreen() {
    Box {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = MaterialTheme.colorScheme.surface),
        ) {
            FancyMansionTopBar(
                typePane = TypePane.MOBILE,
                topBarColor = MaterialTheme.colorScheme.surface,
                title = stringResource(id = R.string.topbar_editor_title_overview),
                subTitle = stringResource(id = R.string.topbar_editor_sub_title),
                shadowElevation = 1.dp
            )


            Column(modifier = Modifier.padding(horizontal = Paddings.Basic.horizontal)){

                Spacer(modifier = Modifier.height(itemMarginHeight))

                FadeInOutSkeleton(modifier = Modifier
                    .padding(vertical = Paddings.Basic.vertical)
                    .height(20.dp)
                    .width(80.dp))

                FadeInOutSkeleton(modifier = Modifier
                    .padding(top = 10.dp, bottom = 10.dp, end = 10.dp)
                    .size(72.dp)
                    .clip(shape = MaterialTheme.shapes.small))

                FadeInOutSkeleton(modifier = Modifier
                    .padding(vertical = Paddings.Basic.vertical)
                    .height(20.dp)
                    .width(80.dp))

                FadeInOutSkeleton(modifier = Modifier
                    .padding(vertical = Paddings.Basic.vertical)
                    .height(35.dp)
                    .fillMaxWidth())

                Spacer(modifier = Modifier.height(itemMarginHeight))

                FadeInOutSkeleton(modifier = Modifier
                    .padding(vertical = Paddings.Basic.vertical)
                    .height(20.dp)
                    .width(80.dp))

                FadeInOutSkeleton(modifier = Modifier
                    .padding(vertical = Paddings.Basic.vertical)
                    .height(80.dp)
                    .fillMaxWidth())

                Spacer(modifier = Modifier.height(itemMarginHeight))

                FadeInOutSkeleton(modifier = Modifier
                    .padding(vertical = Paddings.Basic.vertical)
                    .height(20.dp)
                    .width(80.dp))

                FadeInOutSkeleton(modifier = Modifier
                    .padding(vertical = Paddings.Basic.vertical)
                    .height(160.dp)
                    .fillMaxWidth())
            }
        }

        Box(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .fillMaxWidth()
                .height(80.dp)
                .background(MaterialTheme.colorScheme.surface)
                .borderLine(
                    density = LocalDensity.current,
                    color = MaterialTheme.colorScheme.outline,
                    top = 1.dp
                )
        ) {

            Box(
                modifier = Modifier
                    .padding(vertical = 11.dp, horizontal = 14.dp)
                    .fillMaxSize()
                    .clip(
                        shape = MaterialTheme.shapes.small
                    )
                    .background(color = MaterialTheme.colorScheme.background)
            ) {
                Text(
                    modifier = Modifier.align(Alignment.Center),
                    text = stringResource(id = R.string.edit_overview_button_overview_preview),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }
        }
    }
}

const val detailPanelCornerHeight = 12
val detailPanelShape = RoundedCornerShape(topStart = detailPanelCornerHeight.dp, topEnd = detailPanelCornerHeight.dp)

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun BottomKeywordsDialog(
    keywordStates: List<KeywordState>,
    onClickChip: (Long, Boolean) -> Unit
) {
    val categoryKeywords = keywordStates.groupBy { it.keyword.category }.toList()

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.6f)
            .clip(detailPanelShape)
            .background(MaterialTheme.colorScheme.surface)
            .padding(horizontal = Paddings.Basic.horizontal)
            .padding(top = 10.dp)
            .clickSingle { }
    ) {
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            item {
                Spacer(modifier = Modifier.height(20.dp))
            }
            items(categoryKeywords){
                Column {
                    CommonEditInfoTitle(
                        modifier = Modifier
                            .padding(vertical = Paddings.Basic.vertical)
                            .fillMaxWidth(),
                        title = it.first
                    )
                    FlowRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        it.second.forEach { keywordState ->
                            Chip(
                                keywordState = keywordState,
                                onClick = onClickChip
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(20.dp))
                }
            }

            item {
                Spacer(modifier = Modifier.height(30.dp))
            }
        }

        Box(modifier = Modifier.align(Alignment.TopCenter).fillMaxWidth().height(30.dp).background(
            brush = Brush.verticalGradient(
                colors = listOf(
                    MaterialTheme.colorScheme.surface,
                    Color.Transparent
                )
            )
        ))
    }
}

@Composable
fun Chip(keywordState : KeywordState, onClick: (Long, Boolean) -> Unit) {
    Text(
        modifier = Modifier
            .padding(end = 2.dp)
            .clip(shape = MaterialTheme.shapes.small)
            .border(
                width = 0.5.dp,
                color = MaterialTheme.colorScheme.outline,
                shape = MaterialTheme.shapes.small
            )
            .background(color = if (keywordState.selected.value) MaterialTheme.colorScheme.surface else MaterialTheme.colorScheme.background)
            .clickSingle {
                onClick(keywordState.keyword.id, !keywordState.selected.value)
            }
            .padding(horizontal = 7.dp, vertical = 5.dp),
        text = "#${keywordState.keyword.name}",
        style = MaterialTheme.typography.bodyLarge,
        color = MaterialTheme.colorScheme.onSurface
    )
}
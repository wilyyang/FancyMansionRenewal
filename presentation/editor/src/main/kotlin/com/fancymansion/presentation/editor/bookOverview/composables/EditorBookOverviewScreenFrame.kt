package com.fancymansion.presentation.editor.bookOverview.composables

import android.app.Activity
import android.content.Intent
import android.provider.MediaStore
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.fancymansion.core.presentation.base.CommonEvent
import com.fancymansion.core.presentation.base.LoadState
import com.fancymansion.core.presentation.base.SIDE_EFFECTS_KEY
import com.fancymansion.core.presentation.base.window.TypePane
import com.fancymansion.core.presentation.compose.component.FadeInOutSkeleton
import com.fancymansion.core.presentation.compose.frame.BaseScreen
import com.fancymansion.core.presentation.compose.frame.FancyMansionTopBar
import com.fancymansion.presentation.editor.R
import com.fancymansion.presentation.editor.bookOverview.EditorBookOverviewContract
import com.fancymansion.presentation.editor.bookOverview.KeywordState
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach

@OptIn(ExperimentalLayoutApi::class)
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
        bottomDrawerContent = {
            Column(modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.5f)
                .background(color = MaterialTheme.colorScheme.primaryContainer)) {

                FlowRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    keywordStates.forEach { keywordState ->
                        Chip(
                            text = keywordState.keyword.name,
                            isSelected = keywordState.selected,
                            onClick = {  }
                        )
                    }
                }
            }
        }
    ) {
        EditorBookOverviewScreenContent(
            modifier = Modifier.fillMaxSize(),
            uiState = uiState,
            onEventSent = onEventSent,
            onCommonEventSent = onCommonEventSent,
            focusManager = focusManager
        )
    }

    BackHandler {
        onCommonEventSent(CommonEvent.CloseEvent)
    }
}

@Composable
fun EditorBookOverviewSkeletonScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.surface),
    ) {
        FadeInOutSkeleton(modifier = Modifier.padding(vertical = 20.dp, horizontal = 16.dp).height(30.dp).fillMaxWidth(0.8f))

        FadeInOutSkeleton(modifier = Modifier.height(200.dp).fillMaxWidth(), shape = RectangleShape)
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun FlowRowChips(
    keywords: List<String>,
    selectedKeywords: MutableList<String>,
    onChipClick: (String) -> Unit
) {
    FlowRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        keywords.forEach { keyword ->
            Chip(
                text = keyword,
                isSelected = selectedKeywords.contains(keyword),
                onClick = { onChipClick(keyword) }
            )
        }
    }
}

@Composable
fun Chip(text: String, isSelected: Boolean, onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .padding(horizontal = 4.dp, vertical = 4.dp)
            .clip(shape = RoundedCornerShape(16.dp))
            .wrapContentSize()
            .background(color = if (isSelected) Color(0xFF6200EE) else Color.LightGray)
    ) {
        Text(
            text = text,
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .clickable { onClick() },
            style = MaterialTheme.typography.bodyLarge,
            color = if (isSelected) Color.White else Color.Black
        )
    }
}
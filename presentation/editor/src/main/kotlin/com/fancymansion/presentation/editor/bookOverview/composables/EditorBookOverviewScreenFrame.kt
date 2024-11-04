package com.fancymansion.presentation.editor.bookOverview.composables

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
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
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach

@Composable
fun EditorBookOverviewScreenFrame(
    uiState: EditorBookOverviewContract.State,
    loadState: LoadState,
    effectFlow: SharedFlow<EditorBookOverviewContract.Effect>?,
    onCommonEventSent: (event: CommonEvent) -> Unit,
    onEventSent: (event: EditorBookOverviewContract.Event) -> Unit,
    onNavigationRequested: (EditorBookOverviewContract.Effect.Navigation) -> Unit
) {
    LaunchedEffect(SIDE_EFFECTS_KEY) {
        effectFlow?.onEach { effect ->
            if(effect is EditorBookOverviewContract.Effect.Navigation){
                onNavigationRequested(effect)
            }
        }?.collect()
    }

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
                    // TODO : TEMP CODE
                    onEventSent(EditorBookOverviewContract.Event.BookOverviewButtonClicked)
                }
            )
        }
    ) {
        EditorBookOverviewScreenContent(
            modifier = Modifier.fillMaxSize(),
            uiState = uiState,
            onEventSent = onEventSent,
            onCommonEventSent = onCommonEventSent
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
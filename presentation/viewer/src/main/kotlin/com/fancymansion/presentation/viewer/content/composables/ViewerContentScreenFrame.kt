package com.fancymansion.presentation.viewer.content.composables

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.fancymansion.core.common.const.MOBILE_PREVIEW_SPEC
import com.fancymansion.core.presentation.base.CommonEvent
import com.fancymansion.core.presentation.base.LoadState
import com.fancymansion.core.presentation.base.SIDE_EFFECTS_KEY
import com.fancymansion.core.presentation.component.FadeInOutSkeleton
import com.fancymansion.core.presentation.frame.BaseScreen
import com.fancymansion.core.presentation.theme.FancyMansionTheme
import com.fancymansion.core.presentation.window.TypePane
import com.fancymansion.domain.model.book.PageSettingModel
import com.fancymansion.presentation.viewer.content.ViewerContentContract
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach

@Composable
fun ViewerContentScreenFrame(
    uiState: ViewerContentContract.State,
    loadState: LoadState,
    effectFlow: SharedFlow<ViewerContentContract.Effect>?,
    onCommonEventSent: (event: CommonEvent) -> Unit,
    onEventSent: (event: ViewerContentContract.Event) -> Unit,
    onNavigationRequested: (ViewerContentContract.Effect.Navigation) -> Unit
) {
    LaunchedEffect(SIDE_EFFECTS_KEY) {
        effectFlow?.onEach { effect ->
            if(effect is ViewerContentContract.Effect.Navigation){
                onNavigationRequested(effect)
            }
        }?.collect()
    }

    BaseScreen(
        loadState = loadState,
        description = ViewerContentContract.NAME,
        statusBarColor = Color(uiState.pageSetting.pageTheme.pageColor.colorCode),
        isStatusBarTextDark = !uiState.pageSetting.pageTheme.isDarkTheme,
        typePane = TypePane.SINGLE,
        loadingContent = {
            ViewerContentSkeletonScreen()
        },
        isLoadingHideEffect = true,
        isOverlayTopBar = true
    ) {
        ViewerContentScreenContent(
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

@Preview(device = MOBILE_PREVIEW_SPEC)
@Composable
fun ViewerContentSkeletonScreen(
    setting : PageSettingModel = PageSettingModel()
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.surface),
    ) {
        FadeInOutSkeleton(modifier = Modifier.padding(vertical = 20.dp, horizontal = 16.dp).height(30.dp).fillMaxWidth(0.8f))

        FadeInOutSkeleton(modifier = Modifier.height(200.dp).fillMaxWidth(), shape = RectangleShape)

        Spacer(modifier = Modifier.height(15.dp))

        FadeInOutSkeleton(modifier = Modifier.padding(vertical = 5.dp, horizontal = 16.dp).height(25.dp).fillMaxWidth())
        FadeInOutSkeleton(modifier = Modifier.padding(vertical = 5.dp, horizontal = 16.dp).height(25.dp).fillMaxWidth())
        FadeInOutSkeleton(modifier = Modifier.padding(vertical = 5.dp, horizontal = 16.dp).height(25.dp).fillMaxWidth())
        FadeInOutSkeleton(modifier = Modifier.padding(vertical = 5.dp, horizontal = 16.dp).height(25.dp).fillMaxWidth(0.4f))

        Spacer(modifier = Modifier.height(30.dp))

        FadeInOutSkeleton(modifier = Modifier.padding(vertical = 10.dp, horizontal = 15.dp).height(35.dp).fillMaxWidth())
        FadeInOutSkeleton(modifier = Modifier.padding(vertical = 10.dp, horizontal = 15.dp).height(35.dp).fillMaxWidth())

    }
}

@Preview(device = MOBILE_PREVIEW_SPEC)
@Composable
fun ViewerContentScreenFramePreview(
) {
    FancyMansionTheme {
        ViewerContentScreenFrame(
            uiState = ViewerContentContract.State(),
            loadState = LoadState.Idle,
            effectFlow = null,
            onCommonEventSent = {},
            onEventSent = {},
            onNavigationRequested = {}
        )
    }
}
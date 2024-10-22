package com.fancymansion.presentation.bookOverview.home.composables

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.fancymansion.core.common.const.MOBILE_PREVIEW_SPEC
import com.fancymansion.core.presentation.base.CommonEvent
import com.fancymansion.core.presentation.base.LoadState
import com.fancymansion.core.presentation.base.SIDE_EFFECTS_KEY
import com.fancymansion.core.presentation.base.window.TypePane
import com.fancymansion.core.presentation.compose.component.FadeInOutSkeleton
import com.fancymansion.core.presentation.compose.frame.BaseScreen
import com.fancymansion.core.presentation.compose.theme.FancyMansionTheme
import com.fancymansion.presentation.bookOverview.home.OverviewHomeContract
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach

const val detailPanelCornerHeight = 12
val detailPanelShape = RoundedCornerShape(topStart = detailPanelCornerHeight.dp, topEnd = detailPanelCornerHeight.dp)

@Composable
fun OverviewHomeScreenFrame(
    uiState: OverviewHomeContract.State,
    loadState: LoadState,
    effectFlow: SharedFlow<OverviewHomeContract.Effect>?,
    onCommonEventSent: (event: CommonEvent) -> Unit,
    onEventSent: (event: OverviewHomeContract.Event) -> Unit,
    onNavigationRequested: (OverviewHomeContract.Effect.Navigation) -> Unit
) {
    LaunchedEffect(SIDE_EFFECTS_KEY) {
        effectFlow?.onEach { effect ->
            if(effect is OverviewHomeContract.Effect.Navigation){
                onNavigationRequested(effect)
            }
        }?.collect()
    }

    val statusBarPaddingDp = with(LocalDensity.current) { WindowInsets.statusBars.getTop(this).toDp() }
    val screenSize = LocalConfiguration.current
    val bookCoverHeightDp = remember { screenSize.screenWidthDp * 0.72f }
    val bookBottomInfoHeightDp = remember { screenSize.screenHeightDp - bookCoverHeightDp + detailPanelCornerHeight }

    BaseScreen(
        loadState = loadState,
        description = OverviewHomeContract.NAME,
        statusBarColor = Color.Transparent,
        typePane = TypePane.MOBILE,
        initContent = {

            OverviewHomeSkeletonScreen(
                modifier = Modifier.fillMaxSize(),
                statusBarPaddingDp = statusBarPaddingDp.value,
                bookCoverHeightDp = bookCoverHeightDp
            )
        },
        isOverlayTopBar = true
    ) {
        OverviewHomeScreenContent(
            modifier = Modifier.fillMaxSize(),
            uiState = uiState,
            statusBarPaddingDp = statusBarPaddingDp.value,
            bookCoverHeightDp = bookCoverHeightDp,
            bookBottomInfoHeightDp = bookBottomInfoHeightDp,
            onEventSent = onEventSent,
            onCommonEventSent = onCommonEventSent
        )
    }

    BackHandler {
        onCommonEventSent(CommonEvent.CloseEvent)
    }
}

@Composable
fun OverviewHomeSkeletonScreen(
    modifier: Modifier = Modifier,
    statusBarPaddingDp : Float,
    bookCoverHeightDp : Float
) {

    Column(modifier = modifier.background(color = MaterialTheme.colorScheme.surface)) {
        FadeInOutSkeleton(
            modifier = Modifier
                .height((bookCoverHeightDp + statusBarPaddingDp).dp)
                .fillMaxWidth(),
            shape = RectangleShape
        )

        Column(
            modifier = Modifier
                .offset(y = -(detailPanelCornerHeight).dp)
                .fillMaxHeight()
                .fillMaxWidth()
                .clip(detailPanelShape)
                .background(color = MaterialTheme.colorScheme.surface)
                .padding(detailPanelCornerHeight.dp)
        ) {
            FadeInOutSkeleton(Modifier.padding(vertical = 10.dp).height(20.dp).width(100.dp))

            FadeInOutSkeleton(Modifier.padding(vertical = 10.dp).height(30.dp).width(400.dp))

            FadeInOutSkeleton(Modifier.padding(vertical = 10.dp).height(30.dp).width(200.dp))

            FadeInOutSkeleton(Modifier.padding(vertical = 10.dp).height(120.dp).width(300.dp))

            FadeInOutSkeleton(Modifier.padding(vertical = 10.dp).height(60.dp).width(180.dp))

            FadeInOutSkeleton(Modifier.padding(vertical = 10.dp).height(30.dp).width(220.dp))
        }
    }
}

@Preview(device = MOBILE_PREVIEW_SPEC)
@Composable
fun OverviewHomeScreenFramePreview(
) {
    FancyMansionTheme {
        OverviewHomeScreenFrame(
            uiState = OverviewHomeContract.State(),
            loadState = LoadState.Idle,
            effectFlow = null,
            onCommonEventSent = {},
            onEventSent = {},
            onNavigationRequested = {}
        )
    }
}
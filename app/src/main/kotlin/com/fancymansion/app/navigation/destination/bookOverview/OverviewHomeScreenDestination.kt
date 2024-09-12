package com.fancymansion.app.navigation.destination.bookOverview

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.fancymansion.app.navigation.HandleCommonEffect
import com.fancymansion.app.navigation.navigateViewerContentScreen
import com.fancymansion.core.presentation.base.CommonEvent
import com.fancymansion.core.presentation.base.window.TypePane
import com.fancymansion.presentation.bookOverview.home.OverviewHomeContract
import com.fancymansion.presentation.bookOverview.home.OverviewHomeViewModel
import com.fancymansion.presentation.bookOverview.home.composables.OverviewHomeScreenFrame

@Composable
fun OverviewHomeScreenDestination(
    navController: NavController,
    typePane : TypePane
) {
    val viewModel: OverviewHomeViewModel = hiltViewModel()

    val onEventSent =  remember {
        { event : OverviewHomeContract.Event ->
            viewModel.setEvent(event)
        }
    }

    val onCommonEventSent =  remember {
        { event : CommonEvent ->
            viewModel.setCommonEvent(event)
        }
    }

    val onNavigationRequested : (OverviewHomeContract.Effect.Navigation) -> Unit =  remember {
        { effect : OverviewHomeContract.Effect.Navigation ->
            handleNavigationRequest(effect, navController)
        }
    }

    HandleCommonEffect(navController = navController, commonEffectFlow = viewModel.commonEffect, onCommonEventSent = onCommonEventSent)

    OverviewHomeScreenFrame(
        uiState = viewModel.uiState.value,
        loadState = viewModel.loadState.value,
        effectFlow = viewModel.effect,
        onCommonEventSent = onCommonEventSent,
        onEventSent = onEventSent,
        onNavigationRequested = onNavigationRequested
    )
}

fun handleNavigationRequest(effect: OverviewHomeContract.Effect, navController: NavController) {
    when (effect) {
        is OverviewHomeContract.Effect.Navigation.NavigateViewerContentScreen -> {
            navController.navigateViewerContentScreen(effect.episodeRef, effect.bookTitle, effect.episodeTitle)
        }
        is OverviewHomeContract.Effect.Navigation.NavigateReviewListScreen -> {}
        else -> {}
    }
}
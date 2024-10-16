package com.fancymansion.app.navigation.destination.editor

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.fancymansion.app.navigation.HandleCommonEffect
import com.fancymansion.app.navigation.navigateOverviewScreen
import com.fancymansion.core.presentation.base.CommonEvent
import com.fancymansion.core.presentation.base.window.TypePane
import com.fancymansion.presentation.editor.bookOverview.EditorBookOverviewContract
import com.fancymansion.presentation.editor.bookOverview.EditorBookOverviewViewModel
import com.fancymansion.presentation.editor.bookOverview.composables.EditorBookOverviewScreenFrame

@Composable
fun EditorBookOverviewScreenDestination(
    navController: NavController,
    typePane : TypePane
) {
    val viewModel: EditorBookOverviewViewModel = hiltViewModel()

    val onEventSent =  remember {
        { event : EditorBookOverviewContract.Event ->
            viewModel.setEvent(event)
        }
    }

    val onCommonEventSent =  remember {
        { event : CommonEvent ->
            viewModel.setCommonEvent(event)
        }
    }

    val onNavigationRequested : (EditorBookOverviewContract.Effect.Navigation) -> Unit =  remember {
        { effect : EditorBookOverviewContract.Effect.Navigation ->
            handleNavigationRequest(effect, navController)
        }
    }

    HandleCommonEffect(navController = navController, commonEffectFlow = viewModel.commonEffect, onCommonEventSent = onCommonEventSent)

    EditorBookOverviewScreenFrame(
        uiState = viewModel.uiState.value,
        loadState = viewModel.loadState.value,
        effectFlow = viewModel.effect,
        onCommonEventSent = onCommonEventSent,
        onEventSent = onEventSent,
        onNavigationRequested = onNavigationRequested
    )
}

fun handleNavigationRequest(effect: EditorBookOverviewContract.Effect, navController: NavController) {
    when (effect) {
        is EditorBookOverviewContract.Effect.Navigation.NavigateOverviewScreen -> {
            navController.navigateOverviewScreen(effect.episodeRef)
        }
        else -> {}
    }
}
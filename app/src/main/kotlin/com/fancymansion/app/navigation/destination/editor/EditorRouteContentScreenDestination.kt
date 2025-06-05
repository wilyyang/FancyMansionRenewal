package com.fancymansion.app.navigation.destination.editor

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.fancymansion.app.navigation.HandleCommonEffect
import com.fancymansion.app.navigation.navigateViewerContentScreen
import com.fancymansion.core.presentation.base.CommonEvent
import com.fancymansion.core.presentation.base.window.TypePane
import com.fancymansion.presentation.editor.routeContent.EditorRouteContentContract
import com.fancymansion.presentation.editor.routeContent.EditorRouteContentViewModel
import com.fancymansion.presentation.editor.routeContent.composables.EditorRouteContentScreenFrame

@Composable
fun EditorRouteContentScreenDestination(
    navController: NavController,
    typePane : TypePane
){
    val viewModel: EditorRouteContentViewModel = hiltViewModel()

    val onEventSent =  remember {
        { event : EditorRouteContentContract.Event ->
            viewModel.setEvent(event)
        }
    }

    val onCommonEventSent =  remember {
        { event : CommonEvent ->
            viewModel.setCommonEvent(event)
        }
    }

    val onNavigationRequested : (EditorRouteContentContract.Effect.Navigation) -> Unit =  remember {
        { effect : EditorRouteContentContract.Effect.Navigation ->
            handleNavigationRequest(effect, navController)
        }
    }

    HandleCommonEffect(navController = navController, commonEffectFlow = viewModel.commonEffect, onCommonEventSent = onCommonEventSent)

    EditorRouteContentScreenFrame (
        uiState = viewModel.uiState.value,
        routeConditionStates = viewModel.routeConditionStates,
        loadState = viewModel.loadState.value,
        effectFlow = viewModel.effect,
        onCommonEventSent = onCommonEventSent,
        onEventSent = onEventSent,
        onNavigationRequested = onNavigationRequested
    )
}

fun handleNavigationRequest(effect: EditorRouteContentContract.Effect, navController: NavController) {
    when (effect) {
        is EditorRouteContentContract.Effect.Navigation.NavigateViewerContentScreen -> {
            navController.navigateViewerContentScreen(effect.episodeRef, effect.bookTitle, effect.episodeTitle, effect.pageId)
        }
        else -> {}
    }
}
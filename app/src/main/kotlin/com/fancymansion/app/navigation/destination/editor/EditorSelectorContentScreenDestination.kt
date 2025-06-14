package com.fancymansion.app.navigation.destination.editor

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.fancymansion.app.navigation.HandleCommonEffect
import com.fancymansion.app.navigation.navigateEditorConditionContentScreen
import com.fancymansion.app.navigation.navigateEditorRouteContentScreen
import com.fancymansion.app.navigation.navigateViewerContentScreen
import com.fancymansion.core.common.const.ROUTE_ID_NOT_ASSIGNED
import com.fancymansion.core.presentation.base.CommonEvent
import com.fancymansion.core.presentation.base.window.TypePane
import com.fancymansion.presentation.editor.selectorContent.EditorSelectorContentContract
import com.fancymansion.presentation.editor.selectorContent.EditorSelectorContentViewModel
import com.fancymansion.presentation.editor.selectorContent.composables.EditorSelectorContentScreenFrame

@Composable
fun EditorSelectorContentScreenDestination(
    navController: NavController,
    typePane : TypePane
) {
    val viewModel: EditorSelectorContentViewModel = hiltViewModel()

    val onEventSent =  remember {
        { event : EditorSelectorContentContract.Event ->
            viewModel.setEvent(event)
        }
    }

    val onCommonEventSent =  remember {
        { event : CommonEvent ->
            viewModel.setCommonEvent(event)
        }
    }

    val onNavigationRequested : (EditorSelectorContentContract.Effect.Navigation) -> Unit =  remember {
        { effect : EditorSelectorContentContract.Effect.Navigation ->
            handleNavigationRequest(effect, navController)
        }
    }

    HandleCommonEffect(navController = navController, commonEffectFlow = viewModel.commonEffect, onCommonEventSent = onCommonEventSent)

    EditorSelectorContentScreenFrame (
        uiState = viewModel.uiState.value,
        showConditionStates = viewModel.showConditionStates,
        routeStates = viewModel.routeStates,
        loadState = viewModel.loadState.value,
        effectFlow = viewModel.effect,
        onCommonEventSent = onCommonEventSent,
        onEventSent = onEventSent,
        onNavigationRequested = onNavigationRequested
    )
}

fun handleNavigationRequest(effect: EditorSelectorContentContract.Effect, navController: NavController) {
    when (effect) {
        is EditorSelectorContentContract.Effect.Navigation.NavigateViewerContentScreen -> {
            navController.navigateViewerContentScreen(effect.episodeRef, effect.bookTitle, effect.episodeTitle, effect.pageId)
        }
        is EditorSelectorContentContract.Effect.Navigation.NavigateEditorRouteScreen -> {
            navController.navigateEditorRouteContentScreen(effect.episodeRef, effect.bookTitle, effect.pageId, effect.selectorId, effect.routeId)
        }
        is EditorSelectorContentContract.Effect.Navigation.NavigateEditorConditionScreen -> {
            navController.navigateEditorConditionContentScreen(
                effect.episodeRef, effect.bookTitle,
                effect.pageId, effect.selectorId, ROUTE_ID_NOT_ASSIGNED, effect.conditionId
            )
        }
    }
}
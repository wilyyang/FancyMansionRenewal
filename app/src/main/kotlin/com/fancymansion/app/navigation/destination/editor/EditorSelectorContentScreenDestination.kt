package com.fancymansion.app.navigation.destination.editor

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.fancymansion.app.navigation.HandleCommonEffect
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
        loadState = viewModel.loadState.value,
        effectFlow = viewModel.effect,
        onCommonEventSent = onCommonEventSent,
        onEventSent = onEventSent,
        onNavigationRequested = onNavigationRequested
    )
}

fun handleNavigationRequest(effect: EditorSelectorContentContract.Effect, navController: NavController) {
    when (effect) {
        else -> {}
    }
}
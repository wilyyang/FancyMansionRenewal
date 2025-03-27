package com.fancymansion.app.navigation.destination.editor

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.fancymansion.app.navigation.HandleCommonEffect
import com.fancymansion.core.presentation.base.CommonEvent
import com.fancymansion.core.presentation.base.window.TypePane
import com.fancymansion.presentation.editor.selectorList.EditorSelectorListContract
import com.fancymansion.presentation.editor.selectorList.EditorSelectorListViewModel
import com.fancymansion.presentation.editor.selectorList.composables.EditorSelectorListScreenFrame

@Composable
fun EditorSelectorListScreenDestination(
    navController: NavController,
    typePane : TypePane
) {
    val viewModel: EditorSelectorListViewModel = hiltViewModel()

    val onEventSent =  remember {
        { event : EditorSelectorListContract.Event ->
            viewModel.setEvent(event)
        }
    }

    val onCommonEventSent =  remember {
        { event : CommonEvent ->
            viewModel.setCommonEvent(event)
        }
    }

    val onNavigationRequested : (EditorSelectorListContract.Effect.Navigation) -> Unit =  remember {
        { effect : EditorSelectorListContract.Effect.Navigation ->
            handleNavigationRequest(effect, navController)
        }
    }

    HandleCommonEffect(navController = navController, commonEffectFlow = viewModel.commonEffect, onCommonEventSent = onCommonEventSent)

    EditorSelectorListScreenFrame (
        uiState = viewModel.uiState.value,
        selectorStates = viewModel.selectorStates,
        loadState = viewModel.loadState.value,
        effectFlow = viewModel.effect,
        onCommonEventSent = onCommonEventSent,
        onEventSent = onEventSent,
        onNavigationRequested = onNavigationRequested
    )
}

fun handleNavigationRequest(effect: EditorSelectorListContract.Effect, navController: NavController) {
    when (effect) {
        is EditorSelectorListContract.Effect.Navigation.NavigateEditorSelectorContentScreen -> {
            // TODO
        }
        else -> {}
    }
}
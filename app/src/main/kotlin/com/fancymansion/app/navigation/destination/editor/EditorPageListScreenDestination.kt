package com.fancymansion.app.navigation.destination.editor

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.fancymansion.app.navigation.HandleCommonEffect
import com.fancymansion.core.presentation.base.CommonEvent
import com.fancymansion.core.presentation.base.window.TypePane
import com.fancymansion.presentation.editor.pageList.EditorPageListContract
import com.fancymansion.presentation.editor.pageList.EditorPageListViewModel
import com.fancymansion.presentation.editor.pageList.composables.EditorPageListScreenFrame

@Composable
fun EditorPageListScreenDestination(
    navController: NavController,
    typePane : TypePane
) {
    val viewModel: EditorPageListViewModel = hiltViewModel()

    val onEventSent =  remember {
        { event : EditorPageListContract.Event ->
            viewModel.setEvent(event)
        }
    }

    val onCommonEventSent =  remember {
        { event : CommonEvent ->
            viewModel.setCommonEvent(event)
        }
    }

    val onNavigationRequested : (EditorPageListContract.Effect.Navigation) -> Unit =  remember {
        { effect : EditorPageListContract.Effect.Navigation ->
            handleNavigationRequest(effect, navController)
        }
    }

    HandleCommonEffect(navController = navController, commonEffectFlow = viewModel.commonEffect, onCommonEventSent = onCommonEventSent)

    EditorPageListScreenFrame(
        uiState = viewModel.uiState.value,
        loadState = viewModel.loadState.value,
        effectFlow = viewModel.effect,
        onCommonEventSent = onCommonEventSent,
        onEventSent = onEventSent,
        onNavigationRequested = onNavigationRequested
    )
}

fun handleNavigationRequest(effect: EditorPageListContract.Effect, navController: NavController) {
    when (effect) {
        is EditorPageListContract.Effect.Navigation.NavigateEditorPageContentScreen -> {
            /**
             * TODO
             */
        }
        else -> {}
    }
}
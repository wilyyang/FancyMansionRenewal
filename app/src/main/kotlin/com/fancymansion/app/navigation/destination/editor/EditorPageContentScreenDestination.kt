package com.fancymansion.app.navigation.destination.editor

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.fancymansion.app.navigation.HandleCommonEffect
import com.fancymansion.core.presentation.base.CommonEvent
import com.fancymansion.core.presentation.base.window.TypePane
import com.fancymansion.presentation.editor.pageContent.EditorPageContentContract
import com.fancymansion.presentation.editor.pageContent.EditorPageContentViewModel
import com.fancymansion.presentation.editor.pageContent.composables.EditorPageContentScreenFrame

@Composable
fun EditorPageContentScreenDestination(
    navController: NavController,
    typePane : TypePane
) {
    val viewModel: EditorPageContentViewModel = hiltViewModel()

    val onEventSent =  remember {
        { event : EditorPageContentContract.Event ->
            viewModel.setEvent(event)
        }
    }

    val onCommonEventSent =  remember {
        { event : CommonEvent ->
            viewModel.setCommonEvent(event)
        }
    }

    val onNavigationRequested : (EditorPageContentContract.Effect.Navigation) -> Unit =  remember {
        { effect : EditorPageContentContract.Effect.Navigation ->
            handleNavigationRequest(effect, navController)
        }
    }

    HandleCommonEffect(navController = navController, commonEffectFlow = viewModel.commonEffect, onCommonEventSent = onCommonEventSent)

    EditorPageContentScreenFrame(
        uiState = viewModel.uiState.value,
        contentSourceStates = viewModel.contentSourceStates,
        loadState = viewModel.loadState.value,
        effectFlow = viewModel.effect,
        onCommonEventSent = onCommonEventSent,
        onEventSent = onEventSent,
        onNavigationRequested = onNavigationRequested
    )
}

fun handleNavigationRequest(effect: EditorPageContentContract.Effect, navController: NavController) {
    when (effect) {
        else -> {}
    }
}
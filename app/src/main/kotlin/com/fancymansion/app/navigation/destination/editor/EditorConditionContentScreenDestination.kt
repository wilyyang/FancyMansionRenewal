package com.fancymansion.app.navigation.destination.editor

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import com.fancymansion.app.navigation.HandleCommonEffect
import com.fancymansion.core.presentation.base.CommonEvent
import com.fancymansion.core.presentation.base.window.TypePane
import com.fancymansion.presentation.editor.conditionContent.EditorConditionContentContract
import com.fancymansion.presentation.editor.conditionContent.EditorConditionContentViewModel
import com.fancymansion.presentation.editor.conditionContent.composables.EditorConditionContentScreenFrame

@Composable
fun EditorConditionContentScreenDestination(
    navController: NavController,
    typePane : TypePane
){
    val viewModel: EditorConditionContentViewModel = hiltViewModel()

    val onEventSent =  remember {
        { event : EditorConditionContentContract.Event ->
            viewModel.setEvent(event)
        }
    }

    val onCommonEventSent =  remember {
        { event : CommonEvent ->
            viewModel.setCommonEvent(event)
        }
    }

    val onNavigationRequested : (EditorConditionContentContract.Effect.Navigation) -> Unit =  remember {
        { effect : EditorConditionContentContract.Effect.Navigation ->
            handleNavigationRequest(effect, navController)
        }
    }

    HandleCommonEffect(navController = navController, commonEffectFlow = viewModel.commonEffect, onCommonEventSent = onCommonEventSent)

    EditorConditionContentScreenFrame (
        uiState = viewModel.uiState.value,
        loadState = viewModel.loadState.value,
        effectFlow = viewModel.effect,
        onCommonEventSent = onCommonEventSent,
        onEventSent = onEventSent,
        onNavigationRequested = onNavigationRequested
    )
}

fun handleNavigationRequest(effect: EditorConditionContentContract.Effect, navController: NavController) {}
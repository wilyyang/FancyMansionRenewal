package com.fancymansion.app.navigation.destination.launch

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import com.fancymansion.app.navigation.HandleCommonEffect
import com.fancymansion.core.presentation.base.CommonEvent
import com.fancymansion.core.presentation.base.window.TypePane
import com.fancymansion.presentation.launch.launch.LaunchContract
import com.fancymansion.presentation.launch.launch.LaunchViewModel
import com.fancymansion.presentation.launch.launch.composables.LaunchScreenFrame

@Composable
fun LaunchScreenDestination(
    navController: NavController,
    typePane : TypePane
){
    val viewModel: LaunchViewModel = hiltViewModel()

    val onEventSent =  remember {
        { event : LaunchContract.Event ->
            viewModel.setEvent(event)
        }
    }

    val onCommonEventSent =  remember {
        { event : CommonEvent ->
            viewModel.setCommonEvent(event)
        }
    }

    val onNavigationRequested : (LaunchContract.Effect.Navigation) -> Unit =  remember {
        { effect : LaunchContract.Effect.Navigation ->
            handleNavigationRequest(effect, navController)
        }
    }

    HandleCommonEffect(navController = navController, commonEffectFlow = viewModel.commonEffect, onCommonEventSent = onCommonEventSent)

    LaunchScreenFrame (
        uiState = viewModel.uiState.value,
        loadState = viewModel.loadState.value,
        effectFlow = viewModel.effect,
        onCommonEventSent = onCommonEventSent,
        onEventSent = onEventSent,
        onNavigationRequested = onNavigationRequested
    )
}

fun handleNavigationRequest(effect: LaunchContract.Effect, navController: NavController) {}
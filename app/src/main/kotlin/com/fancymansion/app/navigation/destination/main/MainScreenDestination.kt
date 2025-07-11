package com.fancymansion.app.navigation.destination.main

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.fancymansion.app.navigation.HandleCommonEffect
import com.fancymansion.app.navigation.navigateEditorBookOverviewScreen
import com.fancymansion.core.presentation.base.CommonEvent
import com.fancymansion.core.presentation.base.window.TypePane
import com.fancymansion.presentation.main.content.MainViewModel
import com.fancymansion.presentation.main.content.MainContract
import com.fancymansion.presentation.main.content.composables.MainScreenFrame

@Composable
fun MainScreenDestination(
    navController: NavController,
    typePane : TypePane
){
    val viewModel: MainViewModel = hiltViewModel()

    val onEventSent =  remember {
        { event : MainContract.Event ->
            viewModel.setEvent(event)
        }
    }

    val onCommonEventSent =  remember {
        { event : CommonEvent ->
            viewModel.setCommonEvent(event)
        }
    }

    val onNavigationRequested : (MainContract.Effect.Navigation) -> Unit =  remember {
        { effect : MainContract.Effect.Navigation ->
            handleNavigationRequest(effect, navController)
        }
    }

    HandleCommonEffect(navController = navController, commonEffectFlow = viewModel.commonEffect, onCommonEventSent = onCommonEventSent)

    MainScreenFrame (
        uiState = viewModel.uiState.value,
        loadState = viewModel.loadState.value,
        effectFlow = viewModel.effect,
        onCommonEventSent = onCommonEventSent,
        onEventSent = onEventSent,
        onNavigationRequested = onNavigationRequested
    )
}

fun handleNavigationRequest(effect: MainContract.Effect, navController: NavController) {
    when (effect) {
        is MainContract.Effect.Navigation.NavigateEditorBookOverviewScreen -> {
            navController.navigateEditorBookOverviewScreen(effect.episodeRef)
        }
    }
}
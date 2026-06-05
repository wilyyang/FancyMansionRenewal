package com.fancymansion.app.navigation.destination.homeBook

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import com.fancymansion.app.navigation.HandleCommonEffect
import com.fancymansion.core.presentation.base.CommonEvent
import com.fancymansion.core.presentation.base.window.TypePane
import com.fancymansion.presentation.homeBook.overview.HomeBookOverviewContract
import com.fancymansion.presentation.homeBook.overview.HomeBookOverviewViewModel
import com.fancymansion.presentation.homeBook.overview.composables.HomeBookOverviewScreenFrame

@Composable
fun HomeBookOverviewScreenDestination(
    navController: NavController,
    typePane : TypePane
) {
    val viewModel: HomeBookOverviewViewModel = hiltViewModel()
    val onEventSent =  remember {
        { event : HomeBookOverviewContract.Event ->
            viewModel.setEvent(event)
        }
    }

    val onCommonEventSent =  remember {
        { event : CommonEvent ->
            viewModel.setCommonEvent(event)
        }
    }

    val onNavigationRequested : (HomeBookOverviewContract.Effect.Navigation) -> Unit =  remember {
        { effect : HomeBookOverviewContract.Effect.Navigation ->
            handleNavigationRequest(effect, navController)
        }
    }

    HandleCommonEffect(navController = navController, commonEffectFlow = viewModel.commonEffect, onCommonEventSent = onCommonEventSent)

    HomeBookOverviewScreenFrame(
        uiState = viewModel.uiState.value,
        loadState = viewModel.loadState.value,
        effectFlow = viewModel.effect,
        onCommonEventSent = onCommonEventSent,
        onEventSent = onEventSent,
        onNavigationRequested = onNavigationRequested
    )
}

fun handleNavigationRequest(effect: HomeBookOverviewContract.Effect, navController: NavController) {
    // TODO
}
package com.fancymansion.app.navigation.destination.bookOverview

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import com.fancymansion.app.navigation.HandleCommonEffect
import com.fancymansion.app.navigation.navigateViewerContentScreen
import com.fancymansion.core.presentation.base.CommonEvent
import com.fancymansion.core.presentation.base.window.TypePane
import com.fancymansion.presentation.bookOverview.overview.BookOverviewContract
import com.fancymansion.presentation.bookOverview.overview.BookOverviewViewModel
import com.fancymansion.presentation.bookOverview.overview.composables.BookOverviewScreenFrame

@Composable
fun BookOverviewScreenDestination(
    navController: NavController,
    typePane : TypePane
) {
    val viewModel: BookOverviewViewModel = hiltViewModel()

    val onEventSent =  remember {
        { event : BookOverviewContract.Event ->
            viewModel.setEvent(event)
        }
    }

    val onCommonEventSent =  remember {
        { event : CommonEvent ->
            viewModel.setCommonEvent(event)
        }
    }

    val onNavigationRequested : (BookOverviewContract.Effect.Navigation) -> Unit =  remember {
        { effect : BookOverviewContract.Effect.Navigation ->
            handleNavigationRequest(effect, navController)
        }
    }

    HandleCommonEffect(navController = navController, commonEffectFlow = viewModel.commonEffect, onCommonEventSent = onCommonEventSent)

    BookOverviewScreenFrame(
        uiState = viewModel.uiState.value,
        loadState = viewModel.loadState.value,
        effectFlow = viewModel.effect,
        onCommonEventSent = onCommonEventSent,
        onEventSent = onEventSent,
        onNavigationRequested = onNavigationRequested
    )
}

fun handleNavigationRequest(effect: BookOverviewContract.Effect, navController: NavController) {
    when (effect) {
        is BookOverviewContract.Effect.Navigation.NavigateViewerContentScreen -> {
            navController.navigateViewerContentScreen(effect.episodeRef, effect.bookTitle, effect.episodeTitle)
        }
        is BookOverviewContract.Effect.Navigation.NavigateReviewListScreen -> {}
    }
}
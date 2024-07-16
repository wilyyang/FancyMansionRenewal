package com.fancymansion.app.navigation.destination.viewer

import android.app.Activity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.fancymansion.app.navigation.HandleCommonEffect
import com.fancymansion.core.presentation.base.CommonEvent
import com.fancymansion.core.presentation.window.TypeWindow
import com.fancymansion.presentation.viewer.content.ViewerContentContract
import com.fancymansion.presentation.viewer.content.ViewerContentViewModel
import com.fancymansion.presentation.viewer.content.composables.ViewerContentScreenFrame

@Composable
fun ViewerContentScreenDestination(
    navController: NavController,
    window: TypeWindow
) {
    val viewModel: ViewerContentViewModel = hiltViewModel()
    val activity = (LocalContext.current as Activity)

    val onEventSent =  remember {
        { event : ViewerContentContract.Event ->
            viewModel.setEvent(event)
        }
    }

    val onCommonEventSent =  remember {
        { event : CommonEvent ->
            viewModel.setCommonEvent(event)
        }
    }

    val onNavigationRequested : (ViewerContentContract.Effect.Navigation) -> Unit =  remember {
        { effect : ViewerContentContract.Effect.Navigation ->
            handleNavigationRequest(effect, navController, activity)
        }
    }

    HandleCommonEffect(commonEffectFlow = viewModel.commonEffect, onCommonEventSent = onCommonEventSent, navController = navController)

    ViewerContentScreenFrame(
        uiState = viewModel.uiState.value,
        loadState = viewModel.loadState.value,
        effectFlow = viewModel.effect,
        onCommonEventSent = onCommonEventSent,
        onEventSent = onEventSent,
        onNavigationRequested = onNavigationRequested
    )
}

fun handleNavigationRequest(effect: ViewerContentContract.Effect, navController: NavController, activity : Activity) {

}
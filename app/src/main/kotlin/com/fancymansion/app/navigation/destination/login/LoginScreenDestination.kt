package com.fancymansion.app.navigation.destination.login

import android.app.Activity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.fancymansion.app.navigation.HandleCommonEffect
import com.fancymansion.core.presentation.base.CommonEvent
import com.fancymansion.core.presentation.window.TypePane
import com.fancymansion.core.presentation.window.TypeWindow
import com.fancymansion.presentation.viewer.content.LoginContract
import com.fancymansion.presentation.viewer.content.LoginViewModel
import com.fancymansion.presentation.viewer.content.composables.LoginScreenFrame
import com.fancymansion.presentation.viewer.content.composables.tablet.LoginScreenFrameTablet

@Composable
fun LoginScreenDestination(
    navController: NavController,
    window: TypeWindow
) {
    val viewModel: LoginViewModel = hiltViewModel()
    val activity = (LocalContext.current as Activity)

    val onEventSent =  remember {
        { event : LoginContract.Event ->
            viewModel.setEvent(event)
        }
    }

    val onCommonEventSent =  remember {
        { event : CommonEvent ->
            viewModel.setCommonEvent(event)
        }
    }

    val onNavigationRequested =  remember {
        { effect : LoginContract.Effect.Navigation ->

            handleNavigationRequest(effect, navController, activity)
        }
    }

    HandleCommonEffect(commonEffectFlow = viewModel.commonEffect, onCommonEventSent = onCommonEventSent, navController = navController)
    when (window.pane) {
        TypePane.SINGLE -> {
            LoginScreenFrame(
                uiState = viewModel.uiState.value,
                loadState = viewModel.loadState.value,
                effectFlow = viewModel.effect,
                onCommonEventSent = onCommonEventSent,
                onEventSent = onEventSent,
                onNavigationRequested = onNavigationRequested
            )
        }
        TypePane.DUAL -> {
            LoginScreenFrameTablet(
                uiState = viewModel.uiState.value,
                loadState = viewModel.loadState.value,
                effectFlow = viewModel.effect,
                onCommonEventSent = onCommonEventSent,
                onEventSent = onEventSent,
                onNavigationRequested = onNavigationRequested
            )
        }
    }
}

fun handleNavigationRequest(effect: LoginContract.Effect, navController: NavController, activity : Activity) =
    when (effect) {
        is LoginContract.Effect.Navigation.NavigateFindIdScreen -> {
        }

        is LoginContract.Effect.Navigation.NavigateFindPassword -> {
        }

        is LoginContract.Effect.Navigation.NavigateJoinMembershipScreen -> {
        }

        is LoginContract.Effect.Navigation.NavigateSleepAccount -> {
        }

        else -> {}
    }


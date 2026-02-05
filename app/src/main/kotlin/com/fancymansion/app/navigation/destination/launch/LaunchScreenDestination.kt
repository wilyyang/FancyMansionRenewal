package com.fancymansion.app.navigation.destination.launch

import android.app.Activity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import com.fancymansion.app.auth.GoogleAuthHolder
import com.fancymansion.app.navigation.HandleCommonEffect
import com.fancymansion.core.presentation.base.CommonEvent
import com.fancymansion.core.presentation.base.window.TypePane
import com.fancymansion.presentation.launch.launch.LaunchContract
import com.fancymansion.presentation.launch.launch.LaunchViewModel
import com.fancymansion.presentation.launch.launch.composables.LaunchScreenFrame
import com.fancymansion.presentation.main.content.MainContract
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions

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

    // GoogleSignInClient 생성
    val context = LocalContext.current
    val googleSignInClient: GoogleSignInClient = GoogleAuthHolder.get(context)

    val requestGoogleIdToken = remember(googleSignInClient, onEventSent) {
        {
            googleSignInClient.silentSignIn()
                .addOnSuccessListener { account ->
                    val idToken = account.idToken
                    if (idToken != null) {
                        onEventSent(LaunchContract.Event.GoogleTokenAcquired(idToken))
                    } else {
                        onEventSent(LaunchContract.Event.GoogleLoginNeedUserAction)
                    }
                }
                .addOnFailureListener {
                    onEventSent(LaunchContract.Event.GoogleLoginNeedUserAction)
                }
            Unit
        }
    }

    // GoogleAuthLauncher 생성
    val googleAuthLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode != Activity.RESULT_OK) {
            onEventSent(LaunchContract.Event.GoogleLoginCancel)
            return@rememberLauncherForActivityResult
        }

        try {
            val account = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                .getResult(Exception::class.java)

            val idToken = account.idToken ?: error("idToken is null")
            onEventSent(LaunchContract.Event.GoogleTokenAcquired(idToken))
        } catch (t: Throwable) {
            onEventSent(LaunchContract.Event.GoogleLoginFail(t))
        }
    }

    val launchGoogleLogin = remember(googleSignInClient) {
        { googleAuthLauncher.launch(googleSignInClient.signInIntent) }
    }

    val onNavigationRequested : (LaunchContract.Effect.Navigation) -> Unit =  remember {
        { effect : LaunchContract.Effect.Navigation ->
            handleNavigationRequest(effect, navController, requestGoogleIdToken, launchGoogleLogin)
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

fun handleNavigationRequest(
    effect: LaunchContract.Effect,
    navController: NavController,
    requestGoogleIdToken: () -> Unit,
    launchGoogleLogin: () -> Unit
) {
    when (effect) {
        LaunchContract.Effect.Navigation.AttemptGoogleAutoLogin -> requestGoogleIdToken()
        LaunchContract.Effect.Navigation.GoogleLoginLauncherCall -> launchGoogleLogin()
        LaunchContract.Effect.Navigation.NavigateMain -> {
            navController.navigate(MainContract.NAME) {
                popUpTo(LaunchContract.NAME) { inclusive = true }
                launchSingleTop = true
            }
        }
    }
}
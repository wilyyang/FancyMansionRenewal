package com.fancymansion.app.navigation.destination.launch

import android.app.Activity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
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
    val googleSignInClient: GoogleSignInClient = remember(context) {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(context.getString(com.fancymansion.app.R.string.default_web_client_id))
            .requestEmail()
            .build()
        GoogleSignIn.getClient(context, gso)
    }

    // GoogleAuthLauncher 생성
    val googleAuthLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode != Activity.RESULT_OK) {
            onEventSent(LaunchContract.Event.GoogleLoginLauncherCancel)
            return@rememberLauncherForActivityResult
        }

        try {
            val account = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                .getResult(Exception::class.java)

            val idToken = account.idToken ?: error("idToken is null")
            onEventSent(LaunchContract.Event.GoogleLoginLauncherSuccess(idToken))
        } catch (t: Throwable) {
            onEventSent(LaunchContract.Event.GoogleLoginLauncherFail(t))
        }
    }

    val launchGoogleLogin = remember(googleSignInClient) {
        { googleAuthLauncher.launch(googleSignInClient.signInIntent) }
    }

    val onNavigationRequested : (LaunchContract.Effect.Navigation) -> Unit =  remember {
        { effect : LaunchContract.Effect.Navigation ->
            handleNavigationRequest(effect, navController, launchGoogleLogin)
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

fun handleNavigationRequest(effect: LaunchContract.Effect, navController: NavController, launchGoogleLogin: () -> Unit) {
    when(effect){
        is LaunchContract.Effect.Navigation.GoogleLoginLauncherCall -> {
            launchGoogleLogin()
        }

        is LaunchContract.Effect.Navigation.NavigateMain -> {
            navController.navigate(MainContract.NAME)
        }
    }
}
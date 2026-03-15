package com.fancymansion.app.navigation.destination.main

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import com.fancymansion.app.auth.GoogleAuthHolder
import com.fancymansion.app.navigation.HandleCommonEffect
import com.fancymansion.app.navigation.navigateEditorBookOverviewScreen
import com.fancymansion.app.navigation.navigateOverviewScreen
import com.fancymansion.core.presentation.base.CommonEvent
import com.fancymansion.core.presentation.base.tab.TabScreenComponents
import com.fancymansion.core.presentation.base.window.TypePane
import com.fancymansion.presentation.launch.launch.LaunchContract
import com.fancymansion.presentation.main.content.MainViewModel
import com.fancymansion.presentation.main.content.MainContract
import com.fancymansion.presentation.main.content.composables.MainScreenFrame
import com.fancymansion.presentation.main.tab.editor.EditorTabContract
import com.fancymansion.presentation.main.tab.editor.EditorTabContract.Effect.Navigation.NavigateEditorBookOverviewScreen
import com.fancymansion.presentation.main.tab.editor.EditorTabViewModel
import com.fancymansion.presentation.main.tab.library.LibraryTabContract
import com.fancymansion.presentation.main.tab.library.LibraryTabViewModel
import com.google.android.gms.auth.api.signin.GoogleSignInClient

@Composable
fun MainScreenDestination(
    navController: NavController,
    typePane : TypePane
){
    // MainViewModel
    val mainViewModel: MainViewModel = hiltViewModel()
    val onMainEventSent =  remember {
        { event : MainContract.Event ->
            mainViewModel.setEvent(event)
        }
    }
    val onMainCommonEventSent =  remember {
        { event : CommonEvent ->
            mainViewModel.setCommonEvent(event)
        }
    }

    val context = LocalContext.current
    val googleSignInClient: GoogleSignInClient = GoogleAuthHolder.get(context)
    val googleLogout = remember(googleSignInClient, onMainEventSent) {
        {
            googleSignInClient.signOut()
                .addOnSuccessListener { onMainEventSent(MainContract.Event.GoogleLogoutSuccess) }
                .addOnFailureListener { t -> onMainEventSent(MainContract.Event.GoogleLogoutFail(t)) }
            Unit
        }
    }

    val onMainNavigationRequested : (MainContract.Effect.Navigation) -> Unit =  remember {
        { effect : MainContract.Effect.Navigation ->
            handleMainNavigationRequest(effect, navController, googleLogout)
        }
    }
    HandleCommonEffect(navController = navController, commonEffectFlow = mainViewModel.commonEffect, onCommonEventSent = onMainCommonEventSent)

    // Tab : EditorTabViewModel
    val editorTabViewModel: EditorTabViewModel = hiltViewModel()
    val onEditorTabEventSent =  remember {
        { event : EditorTabContract.Event ->
            editorTabViewModel.setEvent(event)
        }
    }
    val onEditorTabCommonEventSent =  remember {
        { event : CommonEvent ->
            editorTabViewModel.setCommonEvent(event)
        }
    }
    val onEditorTabNavigationRequested: (EditorTabContract.Effect) -> Unit = remember {
        { effect : EditorTabContract.Effect ->
            handleEditorTabNavigationRequest(effect, navController)
        }
    }
    HandleCommonEffect(navController = navController, commonEffectFlow = editorTabViewModel.commonEffect, onCommonEventSent = onEditorTabCommonEventSent)

    val editorTabComponents = TabScreenComponents(
        uiState = editorTabViewModel.uiState.value,
        loadState = editorTabViewModel.loadState.value,
        effectFlow = editorTabViewModel.effect,
        onEventSent = onEditorTabEventSent,
        onCommonEventSent = onEditorTabCommonEventSent,
        onNavigationRequested = onEditorTabNavigationRequested,
    )

    // Tab : LibraryTabViewModel
    val libraryTabViewModel: LibraryTabViewModel = hiltViewModel()
    val onLibraryTabEventSent =  remember {
        { event : LibraryTabContract.Event ->
            libraryTabViewModel.setEvent(event)
        }
    }
    val onLibraryTabCommonEventSent =  remember {
        { event : CommonEvent ->
            libraryTabViewModel.setCommonEvent(event)
        }
    }
    val onLibraryTabNavigationRequested: (LibraryTabContract.Effect) -> Unit = remember {
        { effect : LibraryTabContract.Effect ->
            handleLibraryTabNavigationRequest(effect, navController)
        }
    }
    HandleCommonEffect(navController = navController, commonEffectFlow = libraryTabViewModel.commonEffect, onCommonEventSent = onLibraryTabCommonEventSent)

    val libraryTabComponents = TabScreenComponents(
        uiState = libraryTabViewModel.uiState.value,
        loadState = libraryTabViewModel.loadState.value,
        effectFlow = libraryTabViewModel.effect,
        onEventSent = onLibraryTabEventSent,
        onCommonEventSent = onLibraryTabCommonEventSent,
        onNavigationRequested = onLibraryTabNavigationRequested,
    )

    // MainScreenFrame
    MainScreenFrame (
        uiState = mainViewModel.uiState.value,
        loadState = mainViewModel.loadState.value,
        effectFlow = mainViewModel.effect,
        onCommonEventSent = onMainCommonEventSent,
        onEventSent = onMainEventSent,
        onNavigationRequested = onMainNavigationRequested,
        editorTabComponents = editorTabComponents,
        libraryTabComponents = libraryTabComponents
    )
}

fun handleMainNavigationRequest(effect: MainContract.Effect.Navigation, navController: NavController, googleLogout:() -> Unit) {
    when(effect){
        is MainContract.Effect.Navigation.RequestGoogleLogout -> {
            googleLogout()
        }
        is MainContract.Effect.Navigation.NavigateLaunchScreen -> {
            navController.navigate("${LaunchContract.NAME}/false") {
                popUpTo(0) { inclusive = true }
                launchSingleTop = true
            }
        }
    }
}

fun handleEditorTabNavigationRequest(effect: EditorTabContract.Effect, navController: NavController) {
    when(effect){
        is NavigateEditorBookOverviewScreen -> {
            navController.navigateEditorBookOverviewScreen(effect.episodeRef)
        }
    }
}

fun handleLibraryTabNavigationRequest(effect: LibraryTabContract.Effect, navController: NavController) {
    when(effect){
        is LibraryTabContract.Effect.Navigation.NavigateBookOverviewScreen -> {
            navController.navigateOverviewScreen(effect.episodeRef)
        }
    }
}
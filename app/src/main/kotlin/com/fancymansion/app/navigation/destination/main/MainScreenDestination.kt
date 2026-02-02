package com.fancymansion.app.navigation.destination.main

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import com.fancymansion.app.navigation.HandleCommonEffect
import com.fancymansion.app.navigation.navigateEditorBookOverviewScreen
import com.fancymansion.app.navigation.navigateOverviewScreen
import com.fancymansion.core.presentation.base.CommonEvent
import com.fancymansion.core.presentation.base.ViewSideEffect
import com.fancymansion.core.presentation.base.tab.TabScreenComponents
import com.fancymansion.core.presentation.base.window.TypePane
import com.fancymansion.presentation.main.content.MainViewModel
import com.fancymansion.presentation.main.content.MainContract
import com.fancymansion.presentation.main.content.composables.MainScreenFrame
import com.fancymansion.presentation.main.tab.editor.EditorTabContract
import com.fancymansion.presentation.main.tab.editor.EditorTabContract.Effect.Navigation.NavigateEditorBookOverviewScreen
import com.fancymansion.presentation.main.tab.editor.EditorTabViewModel

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
    val onMainNavigationRequested : (MainContract.Effect.Navigation) -> Unit =  remember {
        { effect : MainContract.Effect.Navigation ->
            handleNavigationRequest(effect, navController)
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
        { effect ->
            if (effect is EditorTabContract.Effect.Navigation) {
                handleNavigationRequest(effect, navController)
            }
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

    // MainScreenFrame
    MainScreenFrame (
        uiState = mainViewModel.uiState.value,
        loadState = mainViewModel.loadState.value,
        effectFlow = mainViewModel.effect,
        onCommonEventSent = onMainCommonEventSent,
        onEventSent = onMainEventSent,
        onNavigationRequested = onMainNavigationRequested,
        editorTabComponents = editorTabComponents
    )
}

fun handleNavigationRequest(effect: ViewSideEffect, navController: NavController) {
    when (effect) {
        is EditorTabContract.Effect.Navigation -> {
            when(effect){
                is NavigateEditorBookOverviewScreen -> {
                    navController.navigateEditorBookOverviewScreen(effect.episodeRef)
                }
            }
        }
        is MainContract.Effect.Navigation -> {
            when(effect){
                is MainContract.Effect.Navigation.NavigateOverviewScreen -> {
                    navController.navigateOverviewScreen(effect.episodeRef)
                }
            }
        }
        else -> {}
    }
}
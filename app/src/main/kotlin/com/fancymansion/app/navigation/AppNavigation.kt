package com.fancymansion.app.navigation

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.LocalOverscrollConfiguration
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.fancymansion.app.navigation.NavigateAnimation.slideVerticallyComposable
import com.fancymansion.app.navigation.destination.login.LoginScreenDestination
import com.fancymansion.core.presentation.window.TypeWindow
import com.fancymansion.presentation.viewer.content.Navigation.Routes.LOGIN

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AppScreenConfiguration(typeWindow: TypeWindow, density: Float) {
    CompositionLocalProvider(
        LocalOverscrollConfiguration provides null,
        LocalDensity provides Density(density = density, fontScale = 1.0f),
    ){
        AppNavigation(typeWindow)
    }
}

@Composable
fun AppNavigation(typeWindow : TypeWindow) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = LOGIN
    ) {

        slideVerticallyComposable(
            route = LOGIN,
            navController = navController
        ) {
            LoginScreenDestination(navController = navController, window = typeWindow)
        }
    }
}
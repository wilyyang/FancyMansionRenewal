package com.fancymansion.app.navigation

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.LocalOverscrollConfiguration
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.fancymansion.app.navigation.NavigateAnimation.upScreenTransition
import com.fancymansion.app.navigation.destination.viewer.ViewerContentScreenDestination
import com.fancymansion.core.common.const.ArgName
import com.fancymansion.core.presentation.window.TypeWindow
import com.fancymansion.presentation.viewer.content.ViewerContentContract

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
        startDestination = "${ViewerContentContract.NAME}/{${ArgName.NAME_USER_ID}}/{${ArgName.NAME_READ_MODE}}/{${ArgName.NAME_BOOK_ID}}/{${ArgName.NAME_EPISODE_ID}}/{${ArgName.NAME_BOOK_TITLE}}/{${ArgName.NAME_EPISODE_TITLE}}"
    ) {
        upScreenTransition(
            route = "${ViewerContentContract.NAME}/{${ArgName.NAME_USER_ID}}/{${ArgName.NAME_READ_MODE}}/{${ArgName.NAME_BOOK_ID}}/{${ArgName.NAME_EPISODE_ID}}/{${ArgName.NAME_BOOK_TITLE}}/{${ArgName.NAME_EPISODE_TITLE}}",
            arguments = listOf(NavArgument.argUserId, NavArgument.argReadMode, NavArgument.argBookId, NavArgument.argEpisodeId, NavArgument.argBookTitle, NavArgument.argEpisodeTitle),
            navController = navController
        ) {
            ViewerContentScreenDestination(navController = navController, window = typeWindow)
        }
    }
}
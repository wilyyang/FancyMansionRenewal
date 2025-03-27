package com.fancymansion.app.navigation

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.LocalOverscrollConfiguration
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.fancymansion.app.navigation.NavigateAnimation.leftScreenTransition
import com.fancymansion.app.navigation.NavigateAnimation.upScreenTransition
import com.fancymansion.app.navigation.destination.bookOverview.OverviewHomeScreenDestination
import com.fancymansion.app.navigation.destination.editor.EditorBookOverviewScreenDestination
import com.fancymansion.app.navigation.destination.editor.EditorPageContentScreenDestination
import com.fancymansion.app.navigation.destination.editor.EditorPageListScreenDestination
import com.fancymansion.app.navigation.destination.editor.EditorSelectorListScreenDestination
import com.fancymansion.app.navigation.destination.viewer.ViewerContentScreenDestination
import com.fancymansion.core.common.const.ArgName
import com.fancymansion.core.common.const.EpisodeRef
import com.fancymansion.core.common.const.PAGE_ID_NOT_ASSIGNED
import com.fancymansion.core.common.log.Logger
import com.fancymansion.core.presentation.base.window.TypePane
import com.fancymansion.presentation.bookOverview.home.OverviewHomeContract
import com.fancymansion.presentation.editor.bookOverview.EditorBookOverviewContract
import com.fancymansion.presentation.editor.pageContent.EditorPageContentContract
import com.fancymansion.presentation.editor.pageList.EditorPageListContract
import com.fancymansion.presentation.editor.selectorList.EditorSelectorListContract
import com.fancymansion.presentation.viewer.content.ViewerContentContract

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AppScreenConfiguration(typePane : TypePane, density: Float) {
    CompositionLocalProvider(
        LocalOverscrollConfiguration provides null,
        LocalDensity provides Density(density = density, fontScale = 1.0f),
    ){
        AppNavigation(typePane)
    }
}

@Composable
fun AppNavigation(typePane : TypePane) {
    val navController = rememberNavController().apply {
        logNavigationChanges()
    }

    NavHost(
        navController = navController,
        startDestination = "${EditorBookOverviewContract.NAME}/{${ArgName.NAME_USER_ID}}/{${ArgName.NAME_READ_MODE}}/{${ArgName.NAME_BOOK_ID}}/{${ArgName.NAME_EPISODE_ID}}"
    ) {
        leftScreenTransition(
            route = "${OverviewHomeContract.NAME}/{${ArgName.NAME_USER_ID}}/{${ArgName.NAME_READ_MODE}}/{${ArgName.NAME_BOOK_ID}}/{${ArgName.NAME_EPISODE_ID}}",
            arguments = listOf(NavArgument.argUserId, NavArgument.argReadMode, NavArgument.argBookId, NavArgument.argEpisodeId),
            navController = navController
        ) {
            OverviewHomeScreenDestination(navController = navController, typePane = typePane)
        }

        upScreenTransition(
            route =
                "${ViewerContentContract.NAME}/{${ArgName.NAME_USER_ID}}/{${ArgName.NAME_READ_MODE}}/{${ArgName.NAME_BOOK_ID}}/{${ArgName.NAME_EPISODE_ID}}" +
                "/{${ArgName.NAME_BOOK_TITLE}}/{${ArgName.NAME_EPISODE_TITLE}}/{${ArgName.NAME_PAGE_ID}}",
            arguments = listOf(
                NavArgument.argUserId, NavArgument.argReadMode, NavArgument.argBookId, NavArgument.argEpisodeId,
                NavArgument.argBookTitle, NavArgument.argEpisodeTitle, NavArgument.argPageId),
            navController = navController
        ) {
            ViewerContentScreenDestination(navController = navController, typePane = typePane)
        }

        upScreenTransition(
            route = "${EditorBookOverviewContract.NAME}/{${ArgName.NAME_USER_ID}}/{${ArgName.NAME_READ_MODE}}/{${ArgName.NAME_BOOK_ID}}/{${ArgName.NAME_EPISODE_ID}}",
            arguments = listOf(NavArgument.argUserId, NavArgument.argReadMode, NavArgument.argBookId, NavArgument.argEpisodeId),
            navController = navController
        ) {
            EditorBookOverviewScreenDestination(navController = navController, typePane = typePane)
        }

        upScreenTransition(
            route = "${EditorPageListContract.NAME}/{${ArgName.NAME_USER_ID}}/{${ArgName.NAME_READ_MODE}}/{${ArgName.NAME_BOOK_ID}}/{${ArgName.NAME_EPISODE_ID}}/{${ArgName.NAME_BOOK_TITLE}}/{${ArgName.NAME_IS_PAGE_LIST_EDIT_MODE}}",
            arguments = listOf(NavArgument.argUserId, NavArgument.argReadMode, NavArgument.argBookId, NavArgument.argEpisodeId,
                NavArgument.argBookTitle, NavArgument.argIsPageListEditMode),
            navController = navController
        ) {
            EditorPageListScreenDestination(navController = navController, typePane = typePane)
        }

        upScreenTransition(
            route = "${EditorPageContentContract.NAME}/{${ArgName.NAME_USER_ID}}/{${ArgName.NAME_READ_MODE}}/{${ArgName.NAME_BOOK_ID}}/{${ArgName.NAME_EPISODE_ID}}" +
                    "/{${ArgName.NAME_BOOK_TITLE}}/{${ArgName.NAME_EPISODE_TITLE}}/{${ArgName.NAME_PAGE_ID}}",
            arguments = listOf(
                NavArgument.argUserId, NavArgument.argReadMode, NavArgument.argBookId, NavArgument.argEpisodeId,
                NavArgument.argBookTitle, NavArgument.argEpisodeTitle, NavArgument.argPageId),
            navController = navController
        ) {
            EditorPageContentScreenDestination(navController = navController, typePane = typePane)
        }

        upScreenTransition(
            route = "${EditorSelectorListContract.NAME}/{${ArgName.NAME_USER_ID}}/{${ArgName.NAME_READ_MODE}}/{${ArgName.NAME_BOOK_ID}}/{${ArgName.NAME_EPISODE_ID}}/{${ArgName.NAME_BOOK_TITLE}}/{${ArgName.NAME_PAGE_ID}}/{${ArgName.NAME_IS_SELECTOR_LIST_EDIT_MODE}}",
            arguments = listOf(NavArgument.argUserId, NavArgument.argReadMode, NavArgument.argBookId, NavArgument.argEpisodeId,
                NavArgument.argBookTitle, NavArgument.argPageId, NavArgument.argIsSelectorListEditMode),
            navController = navController
        ) {
            EditorSelectorListScreenDestination(navController = navController, typePane = typePane)
        }
    }
}

fun NavController.logNavigationChanges() {
    this.addOnDestinationChangedListener { _, destination, arguments ->
        val route = destination.route?.substringBefore("/")
        val args = arguments?.keySet()?.filter { it.startsWith("NAME") }?.joinToString { "$it= ${arguments[it]}" } ?: "empty"
        Logger.print("<Navigate> $route : $args", tag = Logger.BASIC_TAG_NAME)
    }
}

fun NavController.navigateOverviewScreen(episodeRef: EpisodeRef) {
    navigate(route = "${OverviewHomeContract.NAME}/${episodeRef.userId}/${episodeRef.mode.name}/${episodeRef.bookId}/${episodeRef.episodeId}")
}

fun NavController.navigateViewerContentScreen(episodeRef: EpisodeRef, bookTitle: String, episodeTitle: String, pageId: Long = PAGE_ID_NOT_ASSIGNED) {
    navigate(route = "${ViewerContentContract.NAME}/${episodeRef.userId}/${episodeRef.mode.name}/${episodeRef.bookId}/${episodeRef.episodeId}" +
        "/$bookTitle/$episodeTitle/$pageId")
}

fun NavController.navigateEditorPageListScreen(episodeRef: EpisodeRef, bookTitle: String, isEditMode: Boolean) {
    navigate(route = "${EditorPageListContract.NAME}/${episodeRef.userId}/${episodeRef.mode.name}/${episodeRef.bookId}/${episodeRef.episodeId}/$bookTitle/$isEditMode")
}

fun NavController.navigateEditorPageContentScreen(episodeRef: EpisodeRef, bookTitle: String, episodeTitle: String, pageId: Long = PAGE_ID_NOT_ASSIGNED) {
    navigate(route = "${EditorPageContentContract.NAME}/${episodeRef.userId}/${episodeRef.mode.name}/${episodeRef.bookId}/${episodeRef.episodeId}" +
            "/$bookTitle/$episodeTitle/$pageId")
}

fun NavController.navigateEditorSelectorListScreen(episodeRef: EpisodeRef, bookTitle: String, pageId: Long, isEditMode: Boolean) {
    navigate(route = "${EditorSelectorListContract.NAME}/${episodeRef.userId}/${episodeRef.mode.name}/${episodeRef.bookId}/${episodeRef.episodeId}/$bookTitle/$pageId/$isEditMode")
}
package com.fancymansion.app.navigation

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.runtime.Composable
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavDeepLink
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable

typealias EnterLambda = AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition?
typealias ExitLambda = AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition?

private const val ANIM_DURATION_VERTICAL_ENTER = 250
private const val ANIM_DURATION_HORIZONTAL = 200
private const val ANIM_DURATION_EXIT = 150
private const val FADE_OUT_ALPHA = 0.8f
private const val SCALE = 0.85f

/**
 * Fade Animations
 */
val fadeInEnter: EnterLambda = { fadeIn() }
val fadeOutExit: ExitLambda = { fadeOut(targetAlpha = FADE_OUT_ALPHA) }

/**
 * Vertical Animations
 */
val slideUpEnter: EnterLambda = {
    slideIntoContainer(
        towards = AnimatedContentTransitionScope.SlideDirection.Up,
        animationSpec = tween(ANIM_DURATION_VERTICAL_ENTER)
    ) + scaleIn(initialScale = SCALE)
}
val slideDownExit: ExitLambda = {
    slideOutOfContainer(
        towards = AnimatedContentTransitionScope.SlideDirection.Down,
        animationSpec = tween(ANIM_DURATION_EXIT),
    ) + scaleOut(targetScale = SCALE)
}

/**
 * Horizontal Animations
 */
val slideLeftEnter: EnterLambda = {
    slideIntoContainer(
        AnimatedContentTransitionScope.SlideDirection.Left,
        animationSpec = tween(ANIM_DURATION_HORIZONTAL),
    )
}

val slideRightEnter: EnterLambda = {
    slideIntoContainer(
        AnimatedContentTransitionScope.SlideDirection.Right,
        animationSpec = tween(ANIM_DURATION_HORIZONTAL),
    )
}

val slideLeftExit: ExitLambda = {
    slideOutOfContainer(
        AnimatedContentTransitionScope.SlideDirection.Left,
        animationSpec = tween(ANIM_DURATION_HORIZONTAL),
    )
}

val slideRightExit: ExitLambda = {
    slideOutOfContainer(
        AnimatedContentTransitionScope.SlideDirection.Right,
        animationSpec = tween(ANIM_DURATION_HORIZONTAL),
    )
}

/**
 * [Before : exitTransition]     => [Target : enterTransition]
 * [Before : popEnterTransition] <= [Target : popExitTransition]
 * */
object NavigateAnimation {
    fun NavGraphBuilder.fadeScreenTransition(
        navController: NavController,
        route: String,
        arguments: List<NamedNavArgument> = emptyList(),
        deepLinks: List<NavDeepLink> = emptyList(),
        content: @Composable AnimatedContentScope.(NavBackStackEntry) -> Unit
    ) = composable(
        route = route,
        arguments = arguments,
        deepLinks = deepLinks,
        enterTransition = fadeInEnter,
        exitTransition = fadeOutExit,
        popEnterTransition = fadeInEnter,
        popExitTransition = fadeOutExit,
        content = content
    )

    fun NavGraphBuilder.upScreenTransition(
        navController: NavController,
        route: String,
        arguments: List<NamedNavArgument> = emptyList(),
        deepLinks: List<NavDeepLink> = emptyList(),
        content: @Composable AnimatedContentScope.(NavBackStackEntry) -> Unit
    ) = composable(
        route = route,
        arguments = arguments,
        deepLinks = deepLinks,
        enterTransition = slideUpEnter,
        exitTransition = fadeOutExit,
        popEnterTransition = fadeInEnter,
        popExitTransition = slideDownExit,
        content = content
    )

    fun NavGraphBuilder.leftScreenTransition(
        navController: NavController,
        route: String,
        arguments: List<NamedNavArgument> = emptyList(),
        deepLinks: List<NavDeepLink> = emptyList(),
        content: @Composable AnimatedContentScope.(NavBackStackEntry) -> Unit
    ) = composable(
        route = route,
        arguments = arguments,
        deepLinks = deepLinks,
        enterTransition = slideLeftEnter,
        exitTransition = fadeOutExit,
        popEnterTransition = fadeInEnter,
        popExitTransition = slideRightExit,
        content = content
    )
}
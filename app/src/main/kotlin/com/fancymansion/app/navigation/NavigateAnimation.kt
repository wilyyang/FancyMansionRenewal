package com.fancymansion.app.navigation

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavDeepLink
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.fancymansion.core.common.const.ANIM_DURATION_HORIZONTAL_ENTER
import com.fancymansion.core.common.const.ANIM_DURATION_HORIZONTAL_EXIT
import com.fancymansion.core.common.const.ANIM_DURATION_VERTICAL_ENTER
import com.fancymansion.core.common.const.ANIM_DURATION_VERTICAL_EXIT

typealias EnterLambda = AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition?
typealias ExitLambda = AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition?

/**
 * Vertical Animations
 */
val slideUpEnter: EnterLambda = {
    slideIntoContainer(
        towards = AnimatedContentTransitionScope.SlideDirection.Up,
        animationSpec = tween(ANIM_DURATION_VERTICAL_ENTER)
    )
}
val slideDownExit: ExitLambda = {
    slideOutOfContainer(
        towards = AnimatedContentTransitionScope.SlideDirection.Down,
        animationSpec = tween(ANIM_DURATION_VERTICAL_EXIT),
    )
}

/**
 * Horizontal Animations
 */
val slideLeftEnter: EnterLambda = {
    slideIntoContainer(
        AnimatedContentTransitionScope.SlideDirection.Left,
        animationSpec = tween(ANIM_DURATION_HORIZONTAL_ENTER),
    )
}

val slideRightEnter: EnterLambda = {
    slideIntoContainer(
        AnimatedContentTransitionScope.SlideDirection.Right,
        animationSpec = tween(ANIM_DURATION_HORIZONTAL_ENTER),
    )
}

val slideLeftExit: ExitLambda = {
    slideOutOfContainer(
        AnimatedContentTransitionScope.SlideDirection.Left,
        animationSpec = tween(ANIM_DURATION_HORIZONTAL_EXIT),
    )
}

val slideRightExit: ExitLambda = {
    slideOutOfContainer(
        AnimatedContentTransitionScope.SlideDirection.Right,
        animationSpec = tween(ANIM_DURATION_HORIZONTAL_EXIT),
    )
}

/**
 * [Before : exitTransition]     => [Target : enterTransition]
 * [Before : popEnterTransition] <= [Target : popExitTransition]
 * */
object NavigateAnimation {

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
        exitTransition = { ExitTransition.None },
        popEnterTransition =  { EnterTransition.None },
        popExitTransition =  slideDownExit,
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
        exitTransition = { ExitTransition.None },
        popEnterTransition =  { EnterTransition.None },
        popExitTransition =  slideRightExit,
        content = content
    )
}
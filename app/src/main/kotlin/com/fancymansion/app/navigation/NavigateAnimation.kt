package com.fancymansion.app.navigation

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.AnimatedContentTransitionScope
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

/**
 * [애니메이션 가이드]
 * enterTransition = 화면 이동시 새로운 이동 화면의 애니메이션    (existTransition = 화면 이동 시 남아 있는 화면)
 * popExistTransition = 뒤로 가서 화면이 꺼질 시 화면이 꺼지는 화면의 애니메이션    (popEnterTransition = 뒤로 가서 기존에 있는 화면이 나타날 때의 애니메이션)
 *
 * 이전 화면과 관련 없는 새로운 화면, 다른 디자인의 화면인 경우 -> 위아래로 이동( enter - 위로 이동 / popExist -아래로 이동 )
 * 이전 화면과 관련 있는 화면 (상세 화면 등), 비슷한 디자인의 화면인 경우 -> 양 옆으로 이동 (enter - 오른쪽 → 왼쪽 / popExist - 왼쪽 → 오른쪽)
 * */
object NavigateAnimation {
    const val FADE_OUT_ALPHA = 0.8f
    const val VERTICAL_ENTER_DURATION = 250
    const val EXIT_DURATION = 150
    const val HORIZONTAL_ENTER_DURATION = 200
    const val SCALE = 0.85f

    // popup된 destination 체크용
    var previousDestination = ""

    fun NavGraphBuilder.slideVerticalAndNextHorizontalComposable(
        navController: NavController,
        route: String,
        arguments: List<NamedNavArgument> = emptyList(),
        deepLinks: List<NavDeepLink> = emptyList(),
        content: @Composable AnimatedContentScope.(NavBackStackEntry) -> Unit
    ) {
        this.composable(
            route = route,
            arguments = arguments,
            deepLinks = deepLinks,
            enterTransition = {
                previousDestination = navController.currentDestination?.route?:""
                slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Up,
                    animationSpec = tween(VERTICAL_ENTER_DURATION),
                ) + scaleIn(initialScale = SCALE)
            },
            exitTransition = {
                slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Left,
                    animationSpec = tween(HORIZONTAL_ENTER_DURATION),
                )
            },
            popEnterTransition = {
                slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Right,
                    animationSpec = tween(HORIZONTAL_ENTER_DURATION),
                )
            },
            popExitTransition = {
                slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Down,
                            animationSpec = tween(EXIT_DURATION),
                ) + scaleOut(targetScale = SCALE)
            },
            content = content
        )
    }

    fun NavGraphBuilder.slideVerticallyComposable(
        navController: NavController,
        route: String,
        arguments: List<NamedNavArgument> = emptyList(),
        deepLinks: List<NavDeepLink> = emptyList(),
        content: @Composable AnimatedContentScope.(NavBackStackEntry) -> Unit
    ) {
        this.composable(
            route = route,
            arguments = arguments,
            deepLinks = deepLinks,
            enterTransition = {
                previousDestination = navController.currentDestination?.route?:""
                slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Up,
                    animationSpec = tween(VERTICAL_ENTER_DURATION)
                ) + scaleIn(initialScale = SCALE)
            },
            exitTransition = {
                fadeOut(targetAlpha = FADE_OUT_ALPHA)
            },
            popEnterTransition = {
                fadeIn()
            },
            popExitTransition = {
                slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Down,
                    animationSpec = tween(EXIT_DURATION),
                ) + scaleOut(targetScale = SCALE)
            },
            content = content
        )
    }

    fun NavGraphBuilder.slideHorizontallyComposable(
        navController: NavController,
        route: String,
        arguments: List<NamedNavArgument> = emptyList(),
        deepLinks: List<NavDeepLink> = emptyList(),
        content: @Composable AnimatedContentScope.(NavBackStackEntry) -> Unit
    ) {
        this.composable(
            route = route,
            arguments = arguments,
            deepLinks = deepLinks,
            enterTransition = {
                previousDestination = navController.currentDestination?.route?:""

                slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Left,
                            animationSpec = tween(HORIZONTAL_ENTER_DURATION),
                )
            },
            exitTransition = {
                fadeOut(targetAlpha = FADE_OUT_ALPHA)
            },
            popEnterTransition = {
                fadeIn()
            },
            popExitTransition = {
                slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Right,
                    animationSpec = tween(HORIZONTAL_ENTER_DURATION),
                )
            },
            content = content
        )
    }

    fun NavGraphBuilder.fadeComposable(
        navController: NavController,
        route: String,
        arguments: List<NamedNavArgument> = emptyList(),
        deepLinks: List<NavDeepLink> = emptyList(),
        content: @Composable AnimatedContentScope.(NavBackStackEntry) -> Unit
    ) {
        this.composable(
            route = route,
            arguments = arguments,
            deepLinks = deepLinks,
            enterTransition = {
                previousDestination = navController.currentDestination?.route?:""
                fadeIn()
            },
            exitTransition = {
                fadeOut(targetAlpha = FADE_OUT_ALPHA)
            },
            popEnterTransition = {
                fadeIn()
            },
            popExitTransition = {
                fadeOut(targetAlpha = FADE_OUT_ALPHA)
            },
            content = content
        )
    }
}

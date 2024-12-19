package com.fancymansion.core.presentation.compose.frame

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun BoxScope.SideDrawer(
    sideDrawerTopPadding: Dp,
    drawerVisible : Boolean,
    leftDrawerState: DrawerState? = null,
    leftDrawerBackground: (@Composable () -> Unit)? = null,
    leftDrawerContent: (@Composable () -> Unit)? = null,
    rightDrawerState: DrawerState? = null,
    rightDrawerBackground: (@Composable () -> Unit)? = null,
    rightDrawerContent: (@Composable () -> Unit)? = null,
    bottomDrawerState: DrawerState? = null,
    bottomDrawerBackground: (@Composable () -> Unit)? = null,
    bottomDrawerContent: (@Composable () -> Unit)? = null
) {

    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    val screenHeight = LocalConfiguration.current.screenHeightDp.dp

    // 왼쪽 Drawer
    if(drawerVisible && leftDrawerState != null && leftDrawerContent != null){
        val leftDrawerOffset = animateDpAsState(if (leftDrawerState.isOpen) 0.dp else -screenWidth, label = "").value

        LaunchedEffect(leftDrawerState.isOpen) {
            if (leftDrawerState.isOpen) {
                rightDrawerState?.close()
                bottomDrawerState?.close()
            }
        }

        if(leftDrawerBackground != null){
            AnimatedVisibility(
                visible = leftDrawerState.isOpen,
                enter = fadeIn(animationSpec = tween(durationMillis = 300)),
                exit = fadeOut(animationSpec = tween(durationMillis = 300))
            ) {
                leftDrawerBackground()
            }
        }

        Box(Modifier.padding(top = sideDrawerTopPadding).offset(x = leftDrawerOffset).fillMaxHeight().pointerInput(Unit){}) {
            leftDrawerContent()
        }
    }

    // 오른쪽 Drawer
    if(drawerVisible && rightDrawerState != null && rightDrawerContent != null){
        val rightDrawerOffset = animateDpAsState(if (rightDrawerState.isOpen) 0.dp else screenWidth, label = "").value

        LaunchedEffect(rightDrawerState.isOpen) {
            if (rightDrawerState.isOpen) {
                leftDrawerState?.close()
                bottomDrawerState?.close()
            }
        }

        if(rightDrawerBackground != null){
            AnimatedVisibility(
                visible = rightDrawerState.isOpen,
                enter = fadeIn(animationSpec = tween(durationMillis = 300)),
                exit = fadeOut(animationSpec = tween(durationMillis = 300))
            ) {
                rightDrawerBackground()
            }
        }

        Box(Modifier.padding(top = sideDrawerTopPadding).offset(x = rightDrawerOffset).fillMaxHeight().align(Alignment.CenterEnd).pointerInput(Unit){}) {
            rightDrawerContent()
        }
    }

    // 하단 Drawer
    if(drawerVisible && bottomDrawerState != null && bottomDrawerContent != null){
        val bottomDrawerOffset = animateDpAsState(if (bottomDrawerState.isOpen) 0.dp else (screenHeight + 40.dp), label = "").value

        LaunchedEffect(bottomDrawerState.isOpen) {
            if (bottomDrawerState.isOpen) {
                leftDrawerState?.close()
                rightDrawerState?.close()
            }
        }

        if(bottomDrawerBackground != null){
            AnimatedVisibility(
                visible = bottomDrawerState.isOpen,
                enter = fadeIn(animationSpec = tween(durationMillis = 300)),
                exit = fadeOut(animationSpec = tween(durationMillis = 300))
            ) {
                bottomDrawerBackground()
            }
        }

        Box(Modifier.padding(top = sideDrawerTopPadding).offset(y = bottomDrawerOffset).fillMaxWidth().align(Alignment.BottomCenter).pointerInput(Unit){}) {
            bottomDrawerContent()
        }
    }
}
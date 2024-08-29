package com.fancymansion.core.presentation.compose.frame

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.material3.DrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun SideDrawer(
    leftDrawerState: DrawerState? = null,
    leftDrawerContent: (@Composable () -> Unit)? = null,
    rightDrawerState: DrawerState? = null,
    rightDrawerContent: (@Composable () -> Unit)? = null,
    bottomDrawerState: DrawerState? = null,
    bottomDrawerContent: (@Composable () -> Unit)? = null,
    content: @Composable () -> Unit
) {

    val scope: CoroutineScope = rememberCoroutineScope()

    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    val screenHeight = LocalConfiguration.current.screenHeightDp.dp

    val leftDrawerOffset = animateDpAsState(if (leftDrawerState.isOpen) 0.dp else -screenWidth).value
    val rightDrawerOffset = animateDpAsState(if (rightDrawerState.isOpen) 0.dp else screenWidth).value
    val bottomDrawerOffset = animateDpAsState(if (bottomDrawerState.isOpen) 0.dp else screenHeight).value

    if(leftDrawerState != null){
        LaunchedEffect(leftDrawerState.isOpen) {
            if (leftDrawerState.isOpen) {
                rightDrawerState?.close()
                bottomDrawerState?.close()
            }
        }
    }

    if(rightDrawerState != null){
        LaunchedEffect(rightDrawerState.isOpen) {
            if (rightDrawerState.isOpen) {
                leftDrawerState?.close()
                bottomDrawerState?.close()
            }
        }
    }

    if(bottomDrawerState != null){
        LaunchedEffect(bottomDrawerState.isOpen) {
            if (bottomDrawerState.isOpen) {
                leftDrawerState.close()
                rightDrawerState.close()
            }
        }
    }


    Box(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clickable {
                    scope.launch {
                        leftDrawerState.close()
                        rightDrawerState.close()
                        bottomDrawerState.close()
                    }
                }
        ) {
            content()
        }

        // 왼쪽 Drawer
        Box(Modifier.offset(x = leftDrawerOffset).fillMaxHeight().pointerInput(Unit){}) {
            leftDrawerContent()
        }

        // 오른쪽 Drawer
        Box(Modifier.offset(x = rightDrawerOffset).fillMaxHeight().align(Alignment.CenterEnd).pointerInput(Unit){}) {
            rightDrawerContent()
        }

        // 하단 Drawer
        Box(Modifier.offset(y = bottomDrawerOffset).fillMaxWidth().align(Alignment.BottomCenter).pointerInput(Unit){}) {
            bottomDrawerContent()
        }
    }
}
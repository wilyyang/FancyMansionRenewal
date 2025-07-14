package com.fancymansion.core.presentation.compose.frame

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import com.fancymansion.core.presentation.base.LoadState
import com.fancymansion.core.presentation.compose.dialog.AlarmDialog
import com.fancymansion.core.presentation.compose.dialog.ErrorDialog
import com.fancymansion.core.presentation.compose.dialog.Loading

enum class InitShowState {
    ScreenAnimationDelay,
    InitShow,
    InitFadingOut,
    None;

    fun transState(loadState : LoadState) : InitShowState{
        return when(this){
            InitShow -> {
                when(loadState){
                    is LoadState.Init -> InitShow
                    else -> InitFadingOut
                }
            }
            else -> this
        }
    }
}

@Composable
fun CommonPopupLayerProcess(
    loadState : LoadState
) {
    val context = LocalContext.current
    when(loadState){
        is LoadState.Loading -> {
            Loading(
                loadingMessage = loadState.message
            )
        }
        is LoadState.ErrorDialog -> {
            ErrorDialog(
                title = loadState.title?.asString(context),
                message = loadState.message?.asString(context),
                errorMessage = loadState.errorMessage?.asString(context),
                background = loadState.backgroundColorCode?.let { Color(it) },
                confirmText = loadState.confirmText?.asString(context),
                dismissText = loadState.dismissText?.asString(context),
                onConfirm = loadState.onConfirm,
                onDismiss = loadState.onDismiss
            )
        }
        is LoadState.AlarmDialog -> {
            AlarmDialog(
                title = loadState.title?.asString(context),
                message = loadState.message?.asString(context),
                background = loadState.backgroundColorCode?.let { Color(it) },
                confirmText = loadState.confirmText?.asString(context),
                dismissText = loadState.dismissText?.asString(context),
                onConfirm = loadState.onConfirm,
                onDismiss = loadState.onDismiss
            )
        }
        else -> {}
    }
}
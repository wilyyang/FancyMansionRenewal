package com.fancymansion.core.presentation.compose.dialog

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import com.fancymansion.core.common.const.DELAY_LOADING_SHOW_MS
import com.fancymansion.core.presentation.compose.animation.RotatingDonutAnimation
import kotlinx.coroutines.delay

@Composable
fun Loading(
    delayMillis: Long = DELAY_LOADING_SHOW_MS,
    backgroundColor: Color = Color.Transparent,
    onDismiss: () -> Unit = {},
    loadingMessage : String? = null
) {
    val isShowAnimation = remember{mutableStateOf(false) }
    LaunchedEffect(key1 = Unit, block = {
        delay(delayMillis)
        isShowAnimation.value = true
    })
    Surface(modifier = Modifier.fillMaxSize(), color = Color.Transparent) {
        if(isShowAnimation.value){
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(backgroundColor)
            ) {
                Popup(
                    onDismissRequest = onDismiss
                ){
                    Box (
                        modifier = Modifier.semantics { contentDescription = "Loading"}.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ){
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            RotatingDonutAnimation()
                            loadingMessage?.let {
                                Spacer(Modifier.height(20.dp))
                                Text(text = loadingMessage,
                                    style = MaterialTheme.typography.titleLarge,
                                    color = MaterialTheme.colorScheme.surface)
                            }
                        }
                    }
                }
            }

        }
    }
}
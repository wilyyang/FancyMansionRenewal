package com.fancymansion.presentation.launch.launch.composables

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.fancymansion.core.presentation.base.CommonEvent
import com.fancymansion.presentation.launch.R
import com.fancymansion.presentation.launch.launch.LaunchContract
import com.fancymansion.presentation.launch.launch.composables.part.FooterText
import com.fancymansion.presentation.launch.launch.composables.part.GoogleLoginButton
import com.fancymansion.presentation.launch.launch.composables.part.logo.LaunchLogoText

@Composable
fun LaunchScreenContent(
    modifier: Modifier = Modifier,
    uiState: LaunchContract.State,
    onEventSent: (LaunchContract.Event) -> Unit,
    onCommonEventSent: (CommonEvent) -> Unit
) {
    var isLogoAnimationFinished by remember { mutableStateOf(false) }

    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp, vertical = 16.dp)
    ) {

        Column(
            modifier = Modifier.align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier.height(60.dp),
                contentAlignment = Alignment.Center
            ) {
                LaunchLogoText(
                    fullText = stringResource(R.string.launch_logo),
                    isAnimationStart = uiState.isAnimationStart,
                    onAnimationEnd = {
                        isLogoAnimationFinished = true
                    }
                )
            }

            Spacer(modifier = Modifier.height(32.dp))
            GoogleLoginButton(
                modifier = Modifier
                    .fillMaxWidth(0.6f)
                    .height(48.dp),
                text = stringResource(R.string.launch_google_login),
                enabled = uiState.isUserLoginVisible && (!uiState.isAnimationStart || isLogoAnimationFinished),
                onClick = {
                    onEventSent(LaunchContract.Event.OnClickGoogleLogin)
                }
            )
        }

        FooterText(
            modifier = Modifier.align(Alignment.BottomCenter),
            text = stringResource(R.string.launch_created_by)
        )
    }
}
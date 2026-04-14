package com.fancymansion.presentation.main.tab.my.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.fancymansion.core.presentation.base.CommonEvent
import com.fancymansion.core.presentation.base.LoadState
import com.fancymansion.core.presentation.base.SIDE_EFFECTS_KEY
import com.fancymansion.core.presentation.base.window.TypePane
import com.fancymansion.core.presentation.compose.frame.tab.TabBaseScreen
import com.fancymansion.domain.model.user.result.NicknameUpdateResult
import com.fancymansion.presentation.main.tab.my.MyTabContract
import com.fancymansion.presentation.main.tab.my.composables.part.EditNicknameDialog
import com.fancymansion.presentation.main.tab.my.composables.part.toMessage
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach

@Composable
fun MyTabScreenFrame(
    uiState: MyTabContract.State,
    loadState: LoadState,
    effectFlow: SharedFlow<MyTabContract.Effect>?,
    onCommonEventSent: (event: CommonEvent) -> Unit,
    onEventSent: (event: MyTabContract.Event) -> Unit,
    onNavigationRequested: (MyTabContract.Effect.Navigation) -> Unit
) {
    var showEditNicknameDialog by remember { mutableStateOf(false) }
    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner) {
        val lifecycle = lifecycleOwner.lifecycle
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                onCommonEventSent(CommonEvent.OnResume)
            }
        }

        lifecycle.addObserver(observer)
        onDispose {
            lifecycle.removeObserver(observer)
        }
    }

    LaunchedEffect(SIDE_EFFECTS_KEY) {
        effectFlow?.onEach { effect ->
            when (effect) {
                is MyTabContract.Effect.Navigation -> {
                    onNavigationRequested(effect)
                }

                is MyTabContract.Effect.ShowEditNicknameDialog -> {
                    showEditNicknameDialog = true
                }
                is MyTabContract.Effect.DismissEditNicknameDialog -> {
                    showEditNicknameDialog = false
                }
            }
        }?.collect()
    }

    TabBaseScreen(
        loadState = loadState,
        description = MyTabContract.NAME,
        statusBarColor = MaterialTheme.colorScheme.surface,
        typePane = TypePane.MOBILE,
        initContent = {
            MyTabSkeletonScreen()
        }
    ) {
        MyTabScreenContent(
            modifier = Modifier.fillMaxSize(),
            uiState = uiState,
            onEventSent = onEventSent,
            onCommonEventSent = onCommonEventSent
        )
    }

    if (showEditNicknameDialog) {
        EditNicknameDialog(
            text = uiState.editNickname,
            hint = uiState.nickname,
            warningMessage = if(uiState.nicknameUpdateResult is NicknameUpdateResult.Invalid){
                uiState.nicknameUpdateResult.toMessage()
            }else{
                ""
            },
            textInput = {
                onEventSent(MyTabContract.Event.NicknameTextInput(it))
            },
            onConfirm = {
                onEventSent(MyTabContract.Event.OnClickUpdateNickname)
            },
            onDismiss = { showEditNicknameDialog = false }
        )
    }
}

@Composable
fun MyTabSkeletonScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.primaryContainer)
    ) {
    }
}
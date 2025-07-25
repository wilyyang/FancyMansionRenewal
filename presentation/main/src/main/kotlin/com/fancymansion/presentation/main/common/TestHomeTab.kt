package com.fancymansion.presentation.main.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.fancymansion.core.presentation.base.BaseViewModel
import com.fancymansion.core.presentation.base.CommonEvent
import com.fancymansion.core.presentation.base.LoadState
import com.fancymansion.core.presentation.base.SIDE_EFFECTS_KEY
import com.fancymansion.core.presentation.base.ViewEvent
import com.fancymansion.core.presentation.base.ViewSideEffect
import com.fancymansion.core.presentation.base.ViewState
import com.fancymansion.core.presentation.base.window.TypePane
import com.fancymansion.core.presentation.compose.frame.tab.TabBaseScreen
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

class HomeTabContract {
    companion object {
        const val NAME = "main_tab_home"
    }

    data class State(
        val isInitSuccess : Boolean = false,
    ) : ViewState

    sealed class Event : ViewEvent

    sealed class Effect : ViewSideEffect {
        sealed class Navigation : Effect()
    }
}

@HiltViewModel
class HomeTabViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle
) : BaseViewModel<HomeTabContract.State, HomeTabContract.Event, HomeTabContract.Effect>() {

    private var isUpdateResume = false

    init {
        initializeState()
    }

    override fun setInitialState() = HomeTabContract.State()

    override fun handleEvents(event: HomeTabContract.Event) {}

    override fun handleCommonEvents(event: CommonEvent) {
        when(event){
            is CommonEvent.OnResume -> handleOnResume()
            else -> super.handleCommonEvents(event)
        }
    }

    private fun initializeState() {
        launchWithInit {
            setState {
                copy(
                    isInitSuccess = true
                )
            }
        }
    }

    // CommonEvent
    private fun handleOnResume() {
        if (isUpdateResume) {
            isUpdateResume = false
            launchWithLoading {
                // TODO 07.14 load Editor Tab
            }
        }
    }
}

@Composable
fun HomeTabScreenFrame(
    uiState: HomeTabContract.State,
    loadState: LoadState,
    effectFlow: SharedFlow<HomeTabContract.Effect>?,
    onCommonEventSent: (event: CommonEvent) -> Unit,
    onEventSent: (event: HomeTabContract.Event) -> Unit,
    onNavigationRequested: (HomeTabContract.Effect.Navigation) -> Unit
) {

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
            if(effect is HomeTabContract.Effect.Navigation){
                onNavigationRequested(effect)
            }
        }?.collect()
    }

    TabBaseScreen(
        loadState = loadState,
        description = HomeTabContract.NAME,
        statusBarColor = MaterialTheme.colorScheme.surface,
        typePane = TypePane.MOBILE,
        initContent = {
            HomeTabSkeletonScreen()
        }
    ) {
        Box(
            modifier = Modifier.fillMaxSize().background(color = Color.Red)
        )
    }
}

@Composable
fun HomeTabSkeletonScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.surface),
    ) {
    }
}
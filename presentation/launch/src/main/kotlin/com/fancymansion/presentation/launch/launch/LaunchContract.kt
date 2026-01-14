package com.fancymansion.presentation.launch.launch

import com.fancymansion.core.presentation.base.ViewEvent
import com.fancymansion.core.presentation.base.ViewSideEffect
import com.fancymansion.core.presentation.base.ViewState

class LaunchContract  {
    companion object {
        const val NAME = "launch"
    }

    data class State(
        val isInitSuccess : Boolean = false
    ) : ViewState

    sealed class Event : ViewEvent {
        data object OnClickGoogleLogin : Event()

        data object GoogleLoginLauncherCancel : Event()
        data class GoogleLoginLauncherFail(val t: Throwable) : Event()
        data class GoogleLoginLauncherSuccess(val idToken: String) : Event()
    }

    sealed class Effect : ViewSideEffect {
        sealed class Navigation : Effect(){
            data object GoogleLoginLauncherCall : Navigation()
            data object NavigateMain : Navigation()
        }
    }
}
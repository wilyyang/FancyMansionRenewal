package com.fancymansion.presentation.launch.launch

import com.fancymansion.core.presentation.base.ViewEvent
import com.fancymansion.core.presentation.base.ViewSideEffect
import com.fancymansion.core.presentation.base.ViewState

class LaunchContract  {
    companion object {
        const val NAME = "launch"
    }

    data class State(
        val isInitSuccess : Boolean = false,
        val isAutoLoginChecked : Boolean = false
    ) : ViewState

    sealed class Event : ViewEvent {
        data object OnClickGoogleLogin : Event()
        data class GoogleTokenAcquired(val idToken: String) : Event()
        data object GoogleLoginNeedUserAction : Event()
        data class GoogleLoginFail(val t: Throwable) : Event()
        data object GoogleLoginCancel : Event()
    }

    sealed class Effect : ViewSideEffect {
        sealed class Navigation : Effect(){
            data object AttemptGoogleAutoLogin : Navigation()
            data object GoogleLoginLauncherCall : Navigation()
            data object NavigateMain : Navigation()
        }
    }
}
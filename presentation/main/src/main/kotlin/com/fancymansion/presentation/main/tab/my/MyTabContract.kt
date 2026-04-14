package com.fancymansion.presentation.main.tab.my

import com.fancymansion.core.presentation.base.ViewEvent
import com.fancymansion.core.presentation.base.ViewSideEffect
import com.fancymansion.core.presentation.base.ViewState
import com.fancymansion.domain.model.user.result.NicknameUpdateResult

class MyTabContract {
    companion object {
        const val NAME = "main_tab_my"
    }

    data class State(
        val isInitSuccess: Boolean = false,
        val nickname: String = "",
        val editNickname: String = "",
        val nicknameUpdateResult: NicknameUpdateResult = NicknameUpdateResult.Unknown,
        val email: String = ""
    ) : ViewState

    sealed class Event : ViewEvent {

        data class NicknameTextInput(val input: String) : Event()
        data object OnClickEditNickname : Event()
        data object OnClickUpdateNickname : Event()
        data object OnClickLogout : Event()
        data object GoogleLogoutSuccess : Event()
        data class GoogleLogoutFail(val t: Throwable) : Event()
    }

    sealed class Effect : ViewSideEffect {
        data object ShowEditNicknameDialog : Effect()
        data object DismissEditNicknameDialog : Effect()
        sealed class Navigation : Effect() {
            data object RequestGoogleLogout : Navigation()
            data object NavigateLaunchScreen : Navigation()
        }
    }
}
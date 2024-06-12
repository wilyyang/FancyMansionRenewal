package com.fancymansion.presentation.viewer.content

import com.fancymansion.core.presentation.base.ViewEvent
import com.fancymansion.core.presentation.base.ViewSideEffect
import com.fancymansion.core.presentation.base.ViewState

class LoginContract {
    data class State(val loginValue : LoginValue, val isAutoLoginChecked: Boolean = false, val idTextMaxLength : Int = 50) :
        ViewState
    sealed class Event : ViewEvent {
        data class LoginValueUpdate(val newLoginValue : LoginValue) : Event()
        data class AutoLoginChecked(val isAutoLoginChecked : Boolean) : Event()
        object LoginButtonClicked : Event()

        object JoinMembershipButtonClicked : Event()
        object FindIdButtonClicked : Event()
        object FindPasswordButtonClicked : Event()
        data class ResetLoginTextField(val resetId : Boolean = true, val resetPassword : Boolean = true) : Event()
    }
    sealed class Effect : ViewSideEffect {
        sealed class Navigation : Effect() {
            object NavigateJoinMembershipScreen : Navigation()
            object NavigateFindIdScreen : Navigation()
            object NavigateFindPassword : Navigation()
            data class NavigateSleepAccount(val userId:String, val userName:String) : Navigation()
        }
    }
}
data class LoginValue(val userId : String = "", val password : String = "")

object Navigation {
    object Routes {
        const val LOGIN = "login"
    }
}
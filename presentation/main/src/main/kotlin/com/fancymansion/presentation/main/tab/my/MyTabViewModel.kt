package com.fancymansion.presentation.main.tab.my

import androidx.lifecycle.SavedStateHandle
import com.fancymansion.core.presentation.base.BaseViewModel
import com.fancymansion.core.presentation.base.LoadState
import com.fancymansion.domain.usecase.user.UseCaseGetUserInfoLocal
import com.fancymansion.domain.usecase.util.UseCaseGetResource
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
@HiltViewModel
class MyTabViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val useCaseGetUserInfoLocal: UseCaseGetUserInfoLocal,
    private val useCaseGetResource: UseCaseGetResource
) : BaseViewModel<MyTabContract.State, MyTabContract.Event, MyTabContract.Effect>() {

    private lateinit var userId: String

    init {
        initializeState()
    }

    override fun setInitialState() = MyTabContract.State()

    override fun handleEvents(event: MyTabContract.Event) {
        when (event) {
            is MyTabContract.Event.NicknameTextInput -> handleNicknameTextInput(event.input)
            MyTabContract.Event.OnClickEditNickname -> handleOnClickEditNickname()
            MyTabContract.Event.OnClickLogout -> handleOnClickLogout()
            MyTabContract.Event.GoogleLogoutSuccess -> handleGoogleLogoutSuccess()
            is MyTabContract.Event.GoogleLogoutFail -> handleGoogleLogoutFail(event.t)
        }
    }

    private fun initializeState() {
        launchWithInit {
            val userInfo = useCaseGetUserInfoLocal() ?: error("UserInfo is null")
            userId = userInfo.userId

            setState {
                copy(
                    isInitSuccess = true,
                    nickname = userInfo.nickname,
                    editNickname = userInfo.nickname,
                    email = userInfo.email
                )
            }
        }
    }

    /**
     * [1] handle
     */
    private fun handleNicknameTextInput(input: String) {
        setState {
            copy(
                editNickname = input
            )
        }
    }

    private fun handleOnClickEditNickname() {
        // TODO
    }

    private fun handleOnClickLogout() {
        setLoadState(LoadState.Loading())
        setEffect {
            MyTabContract.Effect.Navigation.RequestGoogleLogout
        }
    }

    private fun handleGoogleLogoutSuccess() {
        setLoadStateIdle()
        setEffect {
            MyTabContract.Effect.Navigation.NavigateLaunchScreen
        }
    }

    private fun handleGoogleLogoutFail(throwable: Throwable) {
        setLoadStateIdle()
        val throwable = throwable
    }
}
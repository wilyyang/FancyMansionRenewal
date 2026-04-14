package com.fancymansion.presentation.main.tab.my

import androidx.lifecycle.SavedStateHandle
import com.fancymansion.core.presentation.base.BaseViewModel
import com.fancymansion.core.presentation.base.LoadState
import com.fancymansion.domain.model.user.result.NicknameUpdateResult
import com.fancymansion.domain.usecase.user.UseCaseGetUserInfoLocal
import com.fancymansion.domain.usecase.user.UseCaseUpdateNickname
import com.fancymansion.domain.usecase.util.UseCaseGetResource
import com.fancymansion.domain.usecase.validator.NicknameValidator
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
@HiltViewModel
class MyTabViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val useCaseGetUserInfoLocal: UseCaseGetUserInfoLocal,
    private val useCaseUpdateNickname: UseCaseUpdateNickname,
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
            is MyTabContract.Event.OnClickUpdateNickname -> handleOnClickUpdateNickname()
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
                    editNickname = "",
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
                editNickname = input,
                nicknameUpdateResult = NicknameUpdateResult.Unknown
            )
        }
    }

    private fun handleOnClickEditNickname() {
        setState {
            copy(
                editNickname = "",
                nicknameUpdateResult = NicknameUpdateResult.Unknown
            )
        }

        setEffect {
            MyTabContract.Effect.ShowEditNicknameDialog
        }
    }

    private fun handleOnClickUpdateNickname(){
        val targetNickname = uiState.value.editNickname

        val validation = NicknameValidator.validate(targetNickname)
        if (validation is NicknameUpdateResult.Invalid){
            setState {
                copy(
                    nicknameUpdateResult = validation
                )
            }
            return
        }

        launchWithLoading {
            when(val result = useCaseUpdateNickname(userId, targetNickname)){
                is NicknameUpdateResult.Success -> {
                    setState {
                        copy(
                            nickname = targetNickname,
                            editNickname = "",
                            nicknameUpdateResult = NicknameUpdateResult.Unknown
                        )
                    }
                    setEffect {
                        MyTabContract.Effect.DismissEditNicknameDialog
                    }
                }

                else -> {
                    setState {
                        copy(
                            nicknameUpdateResult = result
                        )
                    }
                }
            }
        }
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
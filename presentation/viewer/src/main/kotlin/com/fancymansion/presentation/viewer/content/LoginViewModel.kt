package com.fancymansion.presentation.viewer.content

import androidx.lifecycle.SavedStateHandle
import com.fancymansion.core.common.const.STATUS_LOGIN_INVALID_ID_PW
import com.fancymansion.core.common.const.STATUS_LOGIN_LIMITED_SERVICE
import com.fancymansion.core.common.resource.StringValue
import com.fancymansion.core.common.throwable.ApiCallException
import com.fancymansion.core.common.throwable.ApiResultStatusException
import com.fancymansion.core.presentation.base.BaseViewModel
import com.fancymansion.core.presentation.base.CommonEffect
import com.fancymansion.core.presentation.base.LoadState
import com.fancymansion.domain.usecase.auth.LoginValidation
import com.fancymansion.domain.usecase.auth.UseCaseGetIsAutoLogin
import com.fancymansion.domain.usecase.auth.UseCaseLogin
import com.fancymansion.domain.usecase.auth.UseCaseResetLoginInfo
import com.fancymansion.domain.usecase.auth.UseCaseSetIsAutoLogin
import com.fancymansion.domain.usecase.auth.UseCaseUpdateLoginUserInfo
import com.fancymansion.presentation.viewer.R
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class LoginViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val useCaseLogin: UseCaseLogin,
    private val useCaseUpdateLoginUserInfo: UseCaseUpdateLoginUserInfo,
    private val useCaseResetLoginInfo: UseCaseResetLoginInfo,
    private val useCaseSetIsAutoLogin: UseCaseSetIsAutoLogin,
    private val useCaseGetIsAutoLogin: UseCaseGetIsAutoLogin
) : BaseViewModel<LoginContract.State, LoginContract.Event, LoginContract.Effect>() {


    override fun setInitialState() =
        LoginContract.State(loginValue = LoginValue(userId = "", password = ""))

    override fun handleEvents(event: LoginContract.Event) {
        when (event) {
            is LoginContract.Event.LoginValueUpdate -> {
                setState {
                    copy(loginValue = event.newLoginValue)
                }
            }

            is LoginContract.Event.ResetLoginTextField -> {
                setState {
                    copy(
                        loginValue = LoginValue(
                            userId = if (event.resetId) "" else uiState.value.loginValue.userId,
                            password = if (event.resetPassword) "" else uiState.value.loginValue.password
                        )
                    )
                }
            }

            is LoginContract.Event.AutoLoginChecked -> {
                scope.launch {
                    useCaseSetIsAutoLogin(event.isAutoLoginChecked)
                    setState {
                        copy(isAutoLoginChecked = event.isAutoLoginChecked)
                    }
                }
            }

            is LoginContract.Event.LoginButtonClicked -> {
                val validType = useCaseLogin.validCheck(
                    userId = uiState.value.loginValue.userId,
                    password = uiState.value.loginValue.password
                )
                val messageId = when (validType) {
                    LoginValidation.NoneId -> R.string.login_fail_request_input_id
                    LoginValidation.NonePassword -> R.string.login_fail_request_input_password
                    LoginValidation.InvalidId -> R.string.login_fail_request_valid_id
                    LoginValidation.InvalidPassword -> R.string.login_fail_data_empty
                    LoginValidation.Pass -> null
                }
                if (validType == LoginValidation.Pass) {
                    onLoginButtonClicked()
                } else {
                    setLoadState(
                        LoadState.AlarmDialog(
                            message = StringValue.StringResource(
                                resId = messageId!!
                            ),
                            dismissText = null,
                            onConfirm = {
                                setLoadStateIdle()
                            }
                        ))
                }
            }

            is LoginContract.Event.JoinMembershipButtonClicked -> {
                launchWithLoading {
                    useCaseResetLoginInfo()
                    setEffect { LoginContract.Effect.Navigation.NavigateJoinMembershipScreen }
                }
            }

            is LoginContract.Event.FindIdButtonClicked -> {
                setEffect { LoginContract.Effect.Navigation.NavigateFindIdScreen }
            }

            is LoginContract.Event.FindPasswordButtonClicked -> {
                setEffect { LoginContract.Effect.Navigation.NavigateFindPassword }
            }
        }
    }

    init {
        launchWithLoading {
            /**
             * 로그인 화면은 비로그인 상태에서만 진입하므로
             * 로그인 화면 초기 자동로그인 체크박스는 false
             */
            useCaseSetIsAutoLogin(false)

            val isAutoLogin = useCaseGetIsAutoLogin()
            setState { copy(isAutoLoginChecked = isAutoLogin) }
        }
    }

    private fun onLoginButtonClicked() = launchWithLoading(endLoadState = null) {
        uiState.value.loginValue.let { loginValue ->
            val data = useCaseLogin(userId = loginValue.userId, password = loginValue.password)
            when{
                data.isSleepAccount -> {
                    setEffect { LoginContract.Effect.Navigation.NavigateSleepAccount(userId = data.currentUserId!!, userName = data.name!!) }
                }

                else -> {
                    useCaseUpdateLoginUserInfo(loginModel = data)
                    setCommonEffect { CommonEffect.Navigation.NavigateMain }
                }
            }
        }
    }

    override fun showErrorResult(
        throwable: Throwable,
        defaultConfirm: () -> Unit,
        defaultDismiss: () -> Unit
    ) {
        when {
            throwable is ApiCallException && throwable.result.id == UseCaseLogin::class.simpleName-> {
                if(throwable is ApiResultStatusException){
                    when(throwable.result.status){
                        STATUS_LOGIN_INVALID_ID_PW -> {
                            setLoadState(
                                LoadState.AlarmDialog(
                                    message = StringValue.StringResource(R.string.login_fail_data_empty),
                                    onConfirm = {
                                        setLoadStateIdle()
                                        setEvent(LoginContract.Event.ResetLoginTextField(resetId = !uiState.value.isAutoLoginChecked))
                                    },
                                    onDismiss = {
                                        setLoadStateIdle()
                                        setEvent(LoginContract.Event.ResetLoginTextField())
                                    }
                                ))
                        }
                        else -> {
                            setLoadState(
                                LoadState.AlarmDialog(
                                    message =
                                    if (throwable.result.status == STATUS_LOGIN_LIMITED_SERVICE)
                                        StringValue.StringResource(R.string.login_fail_user_limited_service)
                                    else StringValue.DynamicString(
                                        throwable.result.message ?: "Unknown Error"
                                    ),
                                    dismissText = null,
                                    onConfirm = {
                                        setLoadStateIdle()
                                        setEvent(LoginContract.Event.ResetLoginTextField())
                                    }
                                ))
                        }
                    }
                }else{
                    setLoadState(
                        LoadState.ErrorDialog(
                            message = StringValue.StringResource(com.fancymansion.core.common.R.string.dialog_error_simple),
                            errorMessage = StringValue.DynamicString(throwable.result.message?:""),
                            dismissText = null,
                            onConfirm = defaultConfirm
                        )
                    )
                }
            }

            else -> {
                super.showErrorResult(
                    throwable = throwable,
                    defaultConfirm = defaultConfirm,
                    defaultDismiss = defaultDismiss
                )
            }
        }
    }
}
package com.fancymansion.domain.usecase.auth

import com.fancymansion.core.common.di.DispatcherIO
import com.fancymansion.domain.interfaceRepository.AuthRepository
import com.fancymansion.domain.model.auth.LoginModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class UseCaseUpdateLoginUserInfo @Inject constructor(
    @DispatcherIO private val dispatcher : CoroutineDispatcher,
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(
        token: String?,
        userId: String?,
        loginId: String?,
        userName: String?,
        nickname: String?
    ) = withContext(dispatcher) {
    }

    suspend operator fun invoke(
        loginModel: LoginModel
    ) = invoke(token = loginModel.accessToken, userId = loginModel.currentUserId, loginId = loginModel.loginId, userName = loginModel.name, nickname = loginModel.nickname)
}

class UseCaseResetLoginInfo @Inject constructor(
    @DispatcherIO private val dispatcher : CoroutineDispatcher,
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke() = withContext(dispatcher) {
    }
}
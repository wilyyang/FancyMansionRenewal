package com.fancymansion.domain.usecase.auth

import com.fancymansion.core.common.di.DispatcherIO
import com.fancymansion.core.common.util.checkEmailValidation
import com.fancymansion.core.common.util.checkPasswordValidation
import com.fancymansion.core.common.wrapper.Success
import com.fancymansion.domain.interfaceRepository.AuthRepository
import com.fancymansion.domain.usecase.getSuccessOrCancel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

enum class LoginValidation {
    NoneId, NonePassword, InvalidId, InvalidPassword, Pass
}

class UseCaseLogin @Inject constructor(
    @DispatcherIO private val dispatcher : CoroutineDispatcher,
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(userId : String, password : String) = withContext(dispatcher) {
        (authRepository.userLogin(userId, password).getSuccessOrCancel(this@UseCaseLogin::class) as Success).data
    }

    fun validCheck(userId: String?, password: String?) =
        if (userId.isNullOrBlank()) {
            LoginValidation.NoneId
        } else if (!checkEmailValidation(userId)) {
            LoginValidation.InvalidId
        } else if (password.isNullOrBlank()) {
            LoginValidation.NonePassword
        } else if (!checkPasswordValidation(password)) {
            LoginValidation.InvalidPassword
        } else {
            LoginValidation.Pass
        }
}

class UseCaseAutoLogin @Inject constructor(
    @DispatcherIO private val dispatcher : CoroutineDispatcher,
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke() = withContext(dispatcher) {
        (authRepository.autoLogin().getSuccessOrCancel(this@UseCaseAutoLogin::class) as Success).data
    }
}
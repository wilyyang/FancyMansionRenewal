package com.fancymansion.domain.usecase.auth

import com.fancymansion.core.common.di.DispatcherIO
import com.fancymansion.domain.interfaceRepository.AuthRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class UseCaseGetIsAutoLogin @Inject constructor(
    @DispatcherIO private val dispatcher : CoroutineDispatcher,
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke() = withContext(dispatcher) {
        authRepository.getIsAutoLogin()
    }
}

class UseCaseSetIsAutoLogin @Inject constructor(
    @DispatcherIO private val dispatcher : CoroutineDispatcher,
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(isAutoLogin: Boolean) = withContext(dispatcher) {
        authRepository.setIsAutoLogin(isAutoLogin)
    }
}
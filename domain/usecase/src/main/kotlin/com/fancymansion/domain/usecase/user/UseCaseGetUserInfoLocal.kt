package com.fancymansion.domain.usecase.user

import com.fancymansion.core.common.di.DispatcherIO
import com.fancymansion.domain.interfaceRepository.UserRepository
import com.fancymansion.domain.model.user.UserInfoModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class UseCaseObserveUserInfoLocal @Inject constructor(
    private val userRepository: UserRepository
) {
    operator fun invoke(): Flow<UserInfoModel?> =
        userRepository.observeUserInfoLocal()
}

class UseCaseGetUserInfoLocal @Inject constructor(
    @param:DispatcherIO private val dispatcher: CoroutineDispatcher,
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(): UserInfoModel? =
        withContext(dispatcher) {
            userRepository.getUserInfoLocal()
        }
}
package com.fancymansion.domain.usecase.user

import com.fancymansion.core.common.di.DispatcherIO
import com.fancymansion.domain.interfaceRepository.UserRepository
import com.fancymansion.domain.model.user.UserInfoModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class UseCaseGoogleLogin @Inject constructor(
    @param:DispatcherIO private val dispatcher: CoroutineDispatcher,
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(idToken: String): UserInfoModel =
        withContext(dispatcher) {
            val initUser = userRepository.signInWithGoogle(idToken)
            val userInfo = userRepository.getOrCreateUserInfoTx(initUser)
            userRepository.upsertUserInfoLocal(userInfo)
            userInfo
        }
}
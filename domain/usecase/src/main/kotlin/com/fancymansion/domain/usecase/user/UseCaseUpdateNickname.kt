package com.fancymansion.domain.usecase.user

import com.fancymansion.core.common.di.DispatcherIO
import com.fancymansion.domain.interfaceRepository.UserRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class UseCaseUpdateNickname @Inject constructor(
    @param:DispatcherIO private val dispatcher: CoroutineDispatcher,
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(uid: String, newNickname: String) =
        withContext(dispatcher) {
            userRepository.updateNickname(uid, newNickname)
        }
}
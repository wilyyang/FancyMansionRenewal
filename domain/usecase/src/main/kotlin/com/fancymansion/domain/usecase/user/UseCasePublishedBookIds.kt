package com.fancymansion.domain.usecase.user

import com.fancymansion.core.common.di.DispatcherIO
import com.fancymansion.domain.interfaceRepository.UserRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class UseCaseGetPublishedBookIds @Inject constructor(
    @param:DispatcherIO private val dispatcher: CoroutineDispatcher,
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(): Set<String> =
        withContext(dispatcher) {
            userRepository.getLocalPublishedBookIds()
        }
}


class UseCaseAddPublishedBookId @Inject constructor(
    @param:DispatcherIO private val dispatcher: CoroutineDispatcher,
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(userId: String, publishedBookId: String) =
        withContext(dispatcher) {
            userRepository.addRemotePublishedBookId(userId, publishedBookId)
            userRepository.addLocalPublishedBookId(publishedBookId)
        }
}


class UseCaseRemovePublishedBookId @Inject constructor(
    @param:DispatcherIO private val dispatcher: CoroutineDispatcher,
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(userId: String, removeTargetId: String) =
        withContext(dispatcher) {
            userRepository.removeRemotePublishedBookId(userId, removeTargetId)
            userRepository.removeLocalPublishedBookId(removeTargetId)
        }
}
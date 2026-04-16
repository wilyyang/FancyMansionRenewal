package com.fancymansion.domain.usecase.user

import com.fancymansion.core.common.di.DispatcherIO
import com.fancymansion.domain.interfaceRepository.UserRepository
import com.fancymansion.domain.model.user.BookRefModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class UseCaseGetPublishedBookRefs @Inject constructor(
    @param:DispatcherIO private val dispatcher: CoroutineDispatcher,
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(): Set<BookRefModel> =
        withContext(dispatcher) {
            userRepository.getLocalPublishedBookRefs()
        }
}


class UseCaseAddPublishedBookRef @Inject constructor(
    @param:DispatcherIO private val dispatcher: CoroutineDispatcher,
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(userId: String, bookRef: BookRefModel) =
        withContext(dispatcher) {
            userRepository.addRemotePublishedBookRef(userId, bookRef)
            userRepository.addLocalPublishedBookRef(bookRef)
        }
}


class UseCaseRemovePublishedBookRef @Inject constructor(
    @param:DispatcherIO private val dispatcher: CoroutineDispatcher,
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(userId: String, bookRef: BookRefModel) =
        withContext(dispatcher) {
            userRepository.removeRemotePublishedBookRef(userId, bookRef)
            userRepository.removeLocalPublishedBookRef(bookRef)
        }
}

class UseCaseUpdatePublishedBookRef @Inject constructor(
    @param:DispatcherIO private val dispatcher: CoroutineDispatcher,
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(userId: String, oldRef: BookRefModel, newRef: BookRefModel) =
        withContext(dispatcher) {
            userRepository.updateRemotePublishedBookRef(userId, oldRef, newRef)
            userRepository.updateLocalPublishedBookRef(oldRef, newRef)
        }
}
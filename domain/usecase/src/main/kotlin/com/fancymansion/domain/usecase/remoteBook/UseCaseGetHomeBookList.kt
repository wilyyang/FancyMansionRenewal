package com.fancymansion.domain.usecase.remoteBook

import com.fancymansion.core.common.di.DispatcherIO
import com.fancymansion.domain.interfaceRepository.BookRemoteRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class UseCaseGetHomeBookList @Inject constructor(
    @param:DispatcherIO private val dispatcher: CoroutineDispatcher,
    private val bookRemoteRepository: BookRemoteRepository
) {

    suspend operator fun invoke() = withContext(dispatcher) {
        bookRemoteRepository.getHomeBookItems()
    }
}

class UseCaseGetSelectedHomeBookList @Inject constructor(
    @param:DispatcherIO private val dispatcher: CoroutineDispatcher,
    private val bookRemoteRepository: BookRemoteRepository
) {

    suspend operator fun invoke(bookIds: List<String>) = withContext(dispatcher) {
        bookRemoteRepository.getSelectedHomeBookItems(bookIds)
    }
}

class UseCaseGetSelectedHomeBook @Inject constructor(
    @param:DispatcherIO private val dispatcher: CoroutineDispatcher,
    private val bookRemoteRepository: BookRemoteRepository
) {

    suspend operator fun invoke(bookId: String) = withContext(dispatcher) {
        bookRemoteRepository.getSelectedHomeBookItem(bookId)
    }
}
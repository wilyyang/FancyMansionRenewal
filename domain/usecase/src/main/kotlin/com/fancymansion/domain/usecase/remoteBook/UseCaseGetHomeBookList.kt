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
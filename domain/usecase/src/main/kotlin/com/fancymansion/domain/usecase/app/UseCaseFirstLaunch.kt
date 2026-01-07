package com.fancymansion.domain.usecase.app

import com.fancymansion.core.common.di.DispatcherIO
import com.fancymansion.domain.interfaceRepository.AppRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class UseCaseIsFirstLaunch @Inject constructor(
    @param:DispatcherIO private val dispatcher: CoroutineDispatcher,
    private val appRepository: AppRepository
) {
    suspend operator fun invoke(): Boolean =
        withContext(dispatcher) {
            appRepository.isFirstLaunch()
        }
}

class UseCaseCompleteOnboarding @Inject constructor(
    @param:DispatcherIO private val dispatcher: CoroutineDispatcher,
    private val appRepository: AppRepository
) {
    suspend operator fun invoke() =
        withContext(dispatcher) {
            appRepository.markLaunched()
        }
}
package com.fancymansion.test.di

import com.fancymansion.core.common.di.DispatcherIO
import com.fancymansion.core.common.di.HiltCommon
import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [HiltCommon::class],
)
object HiltTestCommon {

    @OptIn(ExperimentalCoroutinesApi::class)
    @Provides
    @DispatcherIO
    fun providesDispatcherIO(): CoroutineDispatcher = UnconfinedTestDispatcher()
}
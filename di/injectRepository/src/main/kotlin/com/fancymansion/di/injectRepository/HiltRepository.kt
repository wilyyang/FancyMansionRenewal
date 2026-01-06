package com.fancymansion.di.injectRepository

import com.fancymansion.data.repository.BookLocalRepositoryImpl
import com.fancymansion.data.repository.LogRepositoryImpl
import com.fancymansion.data.repository.AppRepositoryImpl
import com.fancymansion.domain.interfaceRepository.BookLocalRepository
import com.fancymansion.domain.interfaceRepository.LogRepository
import com.fancymansion.domain.interfaceRepository.AppRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface HiltRepository {

    @Binds
    @Singleton
    fun bindLogRepository(repository : LogRepositoryImpl) : LogRepository

    @Binds
    @Singleton
    fun bindBookLocalRepository(repository : BookLocalRepositoryImpl) : BookLocalRepository

    @Binds
    @Singleton
    fun bindAppRepository(repository : AppRepositoryImpl) : AppRepository
}
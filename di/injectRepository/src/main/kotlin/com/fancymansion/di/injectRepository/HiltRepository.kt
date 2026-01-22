package com.fancymansion.di.injectRepository

import com.fancymansion.data.repository.BookLocalRepositoryImpl
import com.fancymansion.data.repository.LogRepositoryImpl
import com.fancymansion.data.repository.AppRepositoryImpl
import com.fancymansion.data.repository.BookRemoteRepositoryImpl
import com.fancymansion.data.repository.UserRepositoryImpl
import com.fancymansion.domain.interfaceRepository.BookLocalRepository
import com.fancymansion.domain.interfaceRepository.LogRepository
import com.fancymansion.domain.interfaceRepository.AppRepository
import com.fancymansion.domain.interfaceRepository.BookRemoteRepository
import com.fancymansion.domain.interfaceRepository.UserRepository
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
    fun bindAppRepository(repository : AppRepositoryImpl) : AppRepository

    @Binds
    @Singleton
    fun bindUserRepository(repository : UserRepositoryImpl) : UserRepository

    @Binds
    @Singleton
    fun bindBookLocalRepository(repository : BookLocalRepositoryImpl) : BookLocalRepository

    @Binds
    @Singleton
    fun bindBookRemoteRepository(repository : BookRemoteRepositoryImpl) : BookRemoteRepository

    @Binds
    @Singleton
    fun bindLogRepository(repository : LogRepositoryImpl) : LogRepository
}
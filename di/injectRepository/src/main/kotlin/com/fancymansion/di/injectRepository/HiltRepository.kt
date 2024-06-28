package com.fancymansion.di.injectRepository

import com.fancymansion.data.repository.AuthRepositoryImpl
import com.fancymansion.data.repository.BookLocalRepositoryImpl
import com.fancymansion.data.repository.LogRepositoryImpl
import com.fancymansion.domain.interfaceRepository.AuthRepository
import com.fancymansion.domain.interfaceRepository.BookLocalRepository
import com.fancymansion.domain.interfaceRepository.LogRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface HiltRepository {

    @Binds
    fun bindAuthRepository(repository : AuthRepositoryImpl) : AuthRepository

    @Binds
    fun bindLogRepository(repository : LogRepositoryImpl) : LogRepository

    @Binds
    fun bindBookLocalRepository(repository : BookLocalRepositoryImpl) : BookLocalRepository
}




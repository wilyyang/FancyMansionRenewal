package com.fancymansion.test.di

import com.fancymansion.data.repository.BookLocalRepositoryImpl
import com.fancymansion.di.injectRepository.HiltRepository
import com.fancymansion.domain.interfaceRepository.BookLocalRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [HiltRepository::class],
)
interface HiltTestRepository {

    @Binds
    fun bindBookLocalRepository(repository : BookLocalRepositoryImpl) : BookLocalRepository
}
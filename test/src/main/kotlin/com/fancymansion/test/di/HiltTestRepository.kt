package com.fancymansion.test.di

import com.fancymansion.di.injectRepository.HiltRepository
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


}
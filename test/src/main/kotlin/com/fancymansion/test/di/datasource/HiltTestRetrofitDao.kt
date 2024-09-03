package com.fancymansion.test.di.datasource

import com.fancymansion.data.datasource.network.auth.di.HiltRetrofitDao
import dagger.Module
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [HiltRetrofitDao::class]
)
class HiltTestRetrofitDao {



}
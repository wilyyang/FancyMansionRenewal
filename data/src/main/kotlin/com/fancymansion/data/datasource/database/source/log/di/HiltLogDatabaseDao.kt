package com.fancymansion.data.datasource.database.source.log.di

import com.fancymansion.data.datasource.database.base.LogDatabaseHelper
import com.fancymansion.data.datasource.database.source.log.dao.LogDatabaseDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class HiltLogDatabaseDao {

    @Singleton
    @Provides
    fun provideLogDatabaseDao(databaseHelper : LogDatabaseHelper) : LogDatabaseDao = databaseHelper.logDatabaseDao()
}
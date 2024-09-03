package com.fancymansion.data.datasource.database.log.di

import android.content.Context
import com.fancymansion.data.datasource.database.log.LogDatabaseHelper
import com.fancymansion.data.datasource.database.log.dao.LogDatabaseDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class HiltLogDatabase {
    @Singleton
    @Provides
    fun provideLogDatabaseHelper(
        @ApplicationContext context : Context
    ) = LogDatabaseHelper.getDataBase(context, CoroutineScope(SupervisorJob()))

    @Singleton
    @Provides
    fun provideLogDatabaseDao(databaseHelper : LogDatabaseHelper) : LogDatabaseDao = databaseHelper.logDatabaseDao()
}
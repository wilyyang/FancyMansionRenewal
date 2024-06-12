package com.fancymansion.data.datasource.database.base.di

import android.content.Context
import com.fancymansion.data.datasource.database.base.LogDatabaseHelper
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
class HiltRoomDatabaseHelper {
    @Singleton
    @Provides
    fun provideHiltLogDatabaseHelper(
        @ApplicationContext context : Context
    ) = LogDatabaseHelper.getDataBase(context, CoroutineScope(SupervisorJob()))
}
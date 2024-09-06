package com.fancymansion.data.datasource.database.book.di

import android.content.Context
import com.fancymansion.data.datasource.database.book.BookDatabaseHelper
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
class HiltBookDatabaseHelper {

    @Singleton
    @Provides
    fun provideBookDatabaseHelper(
        @ApplicationContext context : Context
    ) = BookDatabaseHelper.getDataBase(context, CoroutineScope(SupervisorJob()))
}
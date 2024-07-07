package com.fancymansion.data.datasource.database.source.book.di

import android.content.Context
import com.fancymansion.data.datasource.database.source.book.BookDatabaseHelper
import com.fancymansion.data.datasource.database.source.book.dao.BookDatabaseDao
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
class HiltBookDatabase {
    @Singleton
    @Provides
    fun provideBookDatabaseHelper(
        @ApplicationContext context : Context
    ) = BookDatabaseHelper.getDataBase(context, CoroutineScope(SupervisorJob()))

    @Singleton
    @Provides
    fun provideBookDatabaseDao(databaseHelper : BookDatabaseHelper) : BookDatabaseDao = databaseHelper.bookDatabaseDao()
}
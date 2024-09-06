package com.fancymansion.data.datasource.database.book.di

import com.fancymansion.data.datasource.database.book.BookDatabaseHelper
import com.fancymansion.data.datasource.database.book.dao.BookDatabaseDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class HiltBookDatabaseDao {

    @Singleton
    @Provides
    fun provideBookDatabaseDao(databaseHelper : BookDatabaseHelper) : BookDatabaseDao = databaseHelper.bookDatabaseDao()
}
package com.fancymansion.data.datasource.appStorage.book.di

import android.content.Context
import com.fancymansion.data.datasource.appStorage.book.BookStorageSource
import com.fancymansion.data.datasource.appStorage.book.BookStorageSourceImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class HiltBookStorage {

    @Provides
    @Singleton
    fun bindBookStorageSource(@ApplicationContext context: Context) : BookStorageSource = BookStorageSourceImpl(context)
}
package com.fancymansion.data.datasource.appStorage.di

import com.fancymansion.data.datasource.appStorage.book.BookStorageSource
import com.fancymansion.data.datasource.appStorage.book.BookStorageSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface HiltAppStorage {

    @Binds
    fun bindBookStorageSource(controller : BookStorageSourceImpl) : BookStorageSource
}
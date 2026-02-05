package com.fancymansion.data.datasource.dataStore.di

import android.content.Context
import com.fancymansion.data.datasource.dataStore.app.AppStoreSource
import com.fancymansion.data.datasource.dataStore.app.AppStoreSourceImpl
import com.fancymansion.data.datasource.dataStore.user.UserStoreSource
import com.fancymansion.data.datasource.dataStore.user.UserStoreSourceImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object HiltAppStoreSource {
    @Provides
    @Singleton
    fun provideAppStoreSource(@ApplicationContext context: Context): AppStoreSource =
        AppStoreSourceImpl(context)

    @Provides
    @Singleton
    fun provideUserStoreSource(@ApplicationContext context: Context): UserStoreSource =
        UserStoreSourceImpl(context)
}
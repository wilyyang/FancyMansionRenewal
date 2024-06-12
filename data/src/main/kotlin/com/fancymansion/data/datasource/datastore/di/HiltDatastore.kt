package com.fancymansion.data.datasource.datastore.di

import android.content.Context
import com.fancymansion.data.datasource.datastore.AuthDatastore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class HiltDatastore {

    @Provides
    @Singleton
    fun providesAuthDatastore(@ApplicationContext context: Context): AuthDatastore = AuthDatastore(context)
}
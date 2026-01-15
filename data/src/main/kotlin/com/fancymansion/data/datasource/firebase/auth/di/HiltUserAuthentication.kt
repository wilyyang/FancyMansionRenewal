package com.fancymansion.data.datasource.firebase.auth.di

import com.fancymansion.data.datasource.firebase.auth.UserAuthentication
import com.fancymansion.data.datasource.firebase.auth.UserAuthenticationImpl
import com.google.firebase.auth.FirebaseAuth
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object HiltUserAuthentication {

    @Provides
    @Singleton
    fun provideUserAuthentication(): UserAuthentication = UserAuthenticationImpl(FirebaseAuth.getInstance())
}
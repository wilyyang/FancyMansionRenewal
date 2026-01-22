package com.fancymansion.data.datasource.firebase.di

import com.fancymansion.data.datasource.firebase.auth.UserAuthentication
import com.fancymansion.data.datasource.firebase.auth.UserAuthenticationImpl
import com.fancymansion.data.datasource.firebase.database.book.BookFirestoreDatabase
import com.fancymansion.data.datasource.firebase.database.book.BookFirestoreDatabaseImpl
import com.fancymansion.data.datasource.firebase.database.user.UserFirestoreDatabase
import com.fancymansion.data.datasource.firebase.database.user.UserFirestoreDatabaseImpl
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class HiltFirebaseDatasource {

    @Provides
    @Singleton
    fun provideUserAuthentication(): UserAuthentication =
        UserAuthenticationImpl(FirebaseAuth.getInstance())

    @Provides
    @Singleton
    fun provideUserFirestoreDatabase(): UserFirestoreDatabase =
        UserFirestoreDatabaseImpl(FirebaseFirestore.getInstance())

    @Provides
    @Singleton
    fun provideBookFirestoreDatabase(): BookFirestoreDatabase =
        BookFirestoreDatabaseImpl(FirebaseFirestore.getInstance())
}
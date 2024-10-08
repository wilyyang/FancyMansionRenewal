package com.fancymansion.test.di

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.fancymansion.data.datasource.appStorage.book.BookStorageSource
import com.fancymansion.data.datasource.appStorage.book.di.HiltBookStorage
import com.fancymansion.data.datasource.database.book.BookDatabaseHelper
import com.fancymansion.data.datasource.database.book.di.HiltBookDatabaseHelper
import com.fancymansion.test.fake.bookStorage.FakeBookStorageSource
import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import javax.inject.Singleton

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [HiltBookStorage::class, HiltBookDatabaseHelper::class],
)
class HiltTestDatasource {

    @Provides
    @Singleton
    fun providesBookStorage() : BookStorageSource {
        val testContext: Context = ApplicationProvider.getApplicationContext()
        return FakeBookStorageSource(testContext)
    }

    @Singleton
    @Provides
    fun provideBookDatabaseHelper(): BookDatabaseHelper = Room.inMemoryDatabaseBuilder(
        ApplicationProvider.getApplicationContext(),
        BookDatabaseHelper::class.java
    ).allowMainThreadQueries().build()
}
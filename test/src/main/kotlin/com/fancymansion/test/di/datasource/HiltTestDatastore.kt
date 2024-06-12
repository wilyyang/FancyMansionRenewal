package com.fancymansion.test.di.datasource

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.fancymansion.data.datasource.datastore.AuthDatastore
import com.fancymansion.data.datasource.datastore.di.HiltDatastore
import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import javax.inject.Singleton

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [HiltDatastore::class],
)
class HiltTestDatastore {

    @Provides
    @Singleton
    fun providesAuthDatastore(): AuthDatastore {
        val testContext: Context = ApplicationProvider.getApplicationContext()
        return AuthDatastore(testContext)
    }
}
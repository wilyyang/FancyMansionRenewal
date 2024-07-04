package com.fancymansion.test.di.datasource

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.fancymansion.data.datasource.network.source.dao.AuthRetrofitDao
import com.fancymansion.data.datasource.network.source.di.HiltRetrofitDao
import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import javax.inject.Singleton

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [HiltRetrofitDao::class]
)
class HiltTestRetrofitDao {

    @Provides
    @Singleton
    fun providesAuthRetrofitDao() : AuthRetrofitDao {
        val testContext: Context = ApplicationProvider.getApplicationContext()
        return AuthRetrofitDaoFake(testContext)
    }

}
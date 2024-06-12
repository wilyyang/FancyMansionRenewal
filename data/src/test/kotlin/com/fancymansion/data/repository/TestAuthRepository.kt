package com.fancymansion.data.repository

import com.google.common.truth.Truth.assertThat
import com.fancymansion.core.common.di.HiltCommon
import com.fancymansion.core.common.wrapper.Success
import com.fancymansion.data.datasource.database.base.di.HiltRoomDatabaseHelper
import com.fancymansion.data.datasource.datastore.di.HiltDatastore
import com.fancymansion.data.datasource.network.source.di.HiltRetrofitDao
import com.fancymansion.domain.interfaceRepository.AuthRepository
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.HiltTestApplication
import dagger.hilt.android.testing.UninstallModules
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import javax.inject.Inject

@HiltAndroidTest
@RunWith(RobolectricTestRunner::class)
@Config(application = HiltTestApplication::class, sdk = [33])
@UninstallModules(HiltCommon::class, HiltDatastore::class, HiltRetrofitDao::class, HiltRoomDatabaseHelper::class)
class TestAuthRepository {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var repository : AuthRepository

    @Before
    fun setUp() = runTest {
        hiltRule.inject()
    }

    @Test
    fun api_userLogin_check_data() = runTest {
        val result = repository.userLogin(userId = "user1", password = "password")
        assertThat(result is Success).isTrue()
    }

    @Test
    fun api_autoLogin_check_data() = runTest {
        val result = repository.autoLogin()
        assertThat(result is Success).isTrue()
    }

    @Test
    fun pref_isAutoLogin_check() = runTest {
        assertThat(repository.getIsAutoLogin()).isFalse()
        repository.setIsAutoLogin(true)
        assertThat(repository.getIsAutoLogin()).isTrue()
    }
}
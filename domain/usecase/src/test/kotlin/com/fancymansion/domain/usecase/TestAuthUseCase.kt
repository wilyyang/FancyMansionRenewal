package com.fancymansion.domain.usecase

import com.google.common.truth.Truth.assertThat
import com.fancymansion.core.common.di.HiltCommon
import com.fancymansion.di.injectRepository.HiltRepository
import com.fancymansion.domain.model.auth.LoginModel
import com.fancymansion.domain.usecase.auth.UseCaseLogin
import com.fancymansion.domain.usecase.auth.UseCaseResetLoginInfo
import com.fancymansion.domain.usecase.auth.UseCaseUpdateLoginUserInfo
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
@UninstallModules(HiltCommon::class, HiltRepository::class)
class TestAuthUseCase {
    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var useCaseLogin: UseCaseLogin

    @Inject
    lateinit var useCaseUpdateLoginUserInfo: UseCaseUpdateLoginUserInfo

    @Inject
    lateinit var useCaseResetLoginInfo: UseCaseResetLoginInfo


    @Before
    fun setUp() = runTest {
        hiltRule.inject()
    }

    @Test
    fun test_UseCaseLogin_true() = runTest {
        val result = useCaseLogin("valid", "valid")
        assertThat(result is LoginModel).isTrue()
    }

    @Test
    fun test_UseCaseUpdateLoginUserInfo_insert() = runTest {
        val serverData = useCaseLogin("valid", "valid")
        assertThat(serverData is LoginModel).isTrue()

        println(serverData)
        useCaseUpdateLoginUserInfo(serverData)

        val token = ""

        assertThat(serverData.accessToken == token).isTrue()
    }

    @Test
    fun test_UseCaseResetLoginInfo() = runTest {
        val serverData = useCaseLogin("valid", "valid")
        assertThat(serverData is LoginModel).isTrue()
        println(serverData)
        useCaseUpdateLoginUserInfo(serverData)

        useCaseResetLoginInfo()

        val token = ""

        assertThat(token == "").isTrue()
    }
}
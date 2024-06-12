package com.fancymansion.presentation.viewer

import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.remember
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.common.truth.Truth
import com.fancymansion.core.common.const.CurrentDensity
import com.fancymansion.core.common.di.HiltCommon
import com.fancymansion.core.presentation.base.CommonEvent
import com.fancymansion.core.presentation.base.LoadState
import com.fancymansion.di.injectRepository.HiltRepository
import com.fancymansion.domain.usecase.auth.UseCaseSetIsAutoLogin
import com.fancymansion.presentation.viewer.content.LoginContract
import com.fancymansion.presentation.viewer.content.LoginValue
import com.fancymansion.presentation.viewer.content.LoginViewModel
import com.fancymansion.presentation.viewer.content.Navigation
import com.fancymansion.presentation.viewer.content.composables.LoginScreenFrame
import com.fancymansion.test.fake.FakeActivity
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.HiltTestApplication
import dagger.hilt.android.testing.UninstallModules
import org.junit.Before
import org.junit.FixMethodOrder
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.MethodSorters
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import javax.inject.Inject


@HiltAndroidTest
@RunWith(RobolectricTestRunner::class)
@Config(application = HiltTestApplication::class, sdk = [33])
@UninstallModules(HiltCommon::class, HiltRepository::class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
class TestLoginViewModel {
    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeRule = createAndroidComposeRule<FakeActivity>()

    @Inject
    lateinit var useCaseSetIsAutoLogin : UseCaseSetIsAutoLogin

    lateinit var viewModel: LoginViewModel

    var getEffect: LoginContract.Effect? = null

    @ExperimentalAnimationApi
    @Before
    fun setUp() {
        CurrentDensity.density = 1.0f
        hiltRule.inject()

        composeRule.activity.setContent {
            viewModel = hiltViewModel()

            val onEventSent =  remember {
                { event : LoginContract.Event ->
                    viewModel.setEvent(event)
                }
            }

            val onCommonEventSent =  remember {
                { event : CommonEvent ->
                    viewModel.setCommonEvent(event)
                }
            }

            val onNavigationRequested =  remember {
                { effect : LoginContract.Effect.Navigation ->
                    getEffect = effect
                }
            }

            LoginScreenFrame(
                uiState = viewModel.uiState.value,
                loadState = viewModel.loadState.value,
                effectFlow = viewModel.effect,
                onCommonEventSent = onCommonEventSent,
                onEventSent = onEventSent,
                onNavigationRequested = onNavigationRequested
            )
        }
        composeRule.waitForIdle()
    }

    @Test
    fun test_loginScreen_1_init() {
        composeRule.onNodeWithContentDescription(Navigation.Routes.LOGIN)

        val initState = viewModel.setInitialState()
        Truth.assertThat(viewModel.uiState.value == initState).isTrue()

        composeRule.waitUntil {
            composeRule.onAllNodesWithTag(LoadState.Idle.javaClass.simpleName)
                .fetchSemanticsNodes().size == 1
        }
        Truth.assertThat(viewModel.loadState.value == LoadState.Idle).isTrue()
    }

    @Test
    fun test_loginScreen_2_login_fail() {
        viewModel.setEvent(LoginContract.Event.LoginValueUpdate(newLoginValue = LoginValue("", "valid")))
        viewModel.setEvent(LoginContract.Event.LoginButtonClicked)

        composeRule.waitUntil {
            viewModel.loadState.value is LoadState.AlarmDialog
        }
        Truth.assertThat(viewModel.loadState.value is LoadState.AlarmDialog).isTrue()
    }
}
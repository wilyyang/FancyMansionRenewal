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
import com.fancymansion.presentation.viewer.content.ViewerContentContract
import com.fancymansion.presentation.viewer.content.ViewerContentViewModel
import com.fancymansion.presentation.viewer.content.Navigation
import com.fancymansion.presentation.viewer.content.composables.ViewerContentScreenFrame
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


@HiltAndroidTest
@RunWith(RobolectricTestRunner::class)
@Config(application = HiltTestApplication::class, sdk = [33])
@UninstallModules(HiltCommon::class, HiltRepository::class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
class TestViewerContentViewModel {
    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeRule = createAndroidComposeRule<FakeActivity>()

    lateinit var viewModel: ViewerContentViewModel

    var getEffect: ViewerContentContract.Effect? = null

    @ExperimentalAnimationApi
    @Before
    fun setUp() {
        CurrentDensity.density = 1.0f
        hiltRule.inject()

        composeRule.activity.setContent {
            viewModel = hiltViewModel()

            val onEventSent =  remember {
                { event : ViewerContentContract.Event ->
                    viewModel.setEvent(event)
                }
            }

            val onCommonEventSent =  remember {
                { event : CommonEvent ->
                    viewModel.setCommonEvent(event)
                }
            }

            val onNavigationRequested =  remember {
                { effect : ViewerContentContract.Effect.Navigation ->
                    getEffect = effect
                }
            }

            ViewerContentScreenFrame(
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
        composeRule.onNodeWithContentDescription(Navigation.Routes.VIEWER_CONTENT)

        val initState = viewModel.setInitialState()
        Truth.assertThat(viewModel.uiState.value == initState).isFalse()

        composeRule.waitUntil {
            composeRule.onAllNodesWithTag(LoadState.Idle.javaClass.simpleName)
                .fetchSemanticsNodes().size == 1
        }
        Truth.assertThat(viewModel.loadState.value == LoadState.Idle).isTrue()
    }
}
package com.fancymansion.presentation.viewer

import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.remember
import androidx.compose.ui.test.junit4.ComposeTestRule
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import com.fancymansion.core.common.const.ArgName
import com.fancymansion.core.common.const.EpisodeRef
import com.fancymansion.core.common.const.PageLineHeight
import com.fancymansion.core.common.const.PageMarginHorizontal
import com.fancymansion.core.common.const.PageTextSize
import com.fancymansion.core.common.const.PageTheme
import com.fancymansion.core.common.const.PageType
import com.fancymansion.core.common.const.ReadMode
import com.fancymansion.core.common.const.SelectorPaddingVertical
import com.fancymansion.core.common.const.getBookId
import com.fancymansion.core.common.const.getEpisodeId
import com.fancymansion.core.common.const.sampleUserId
import com.fancymansion.core.common.const.testUserId
import com.fancymansion.core.common.di.HiltCommon
import com.fancymansion.core.presentation.base.CommonEvent
import com.fancymansion.core.presentation.base.LoadState
import com.fancymansion.di.injectRepository.HiltRepository
import com.fancymansion.domain.model.book.LogicModel
import com.fancymansion.domain.usecase.book.UseCaseBookList
import com.fancymansion.domain.usecase.book.UseCaseBookLogic
import com.fancymansion.domain.usecase.book.UseCaseLoadBook
import com.fancymansion.domain.usecase.book.UseCaseMakeBook
import com.fancymansion.domain.usecase.book.UseCasePageSetting
import com.fancymansion.presentation.viewer.content.ViewerContentContract
import com.fancymansion.presentation.viewer.content.ViewerContentViewModel
import com.fancymansion.presentation.viewer.content.composables.ViewerContentScreenFrame
import com.fancymansion.test.fake.FakeActivity
import com.google.common.truth.Truth
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.HiltTestApplication
import dagger.hilt.android.testing.UninstallModules
import kotlinx.coroutines.runBlocking
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
class TestViewerContentViewModel {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeRule = createAndroidComposeRule<FakeActivity>()

    private val saveStatedHandle = SavedStateHandle()

    private lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var viewModel: ViewerContentViewModel

    @Inject
    lateinit var useCasePageSetting: UseCasePageSetting

    @Inject
    lateinit var useCaseLoadBook: UseCaseLoadBook

    @Inject
    lateinit var useCaseBookLogic: UseCaseBookLogic

    @Inject
    lateinit var useCaseMakeBook: UseCaseMakeBook

    @Inject
    lateinit var useCaseBookList: UseCaseBookList

    private lateinit var testEventSent: (event: ViewerContentContract.Event) -> Unit
    private var getEffect: ViewerContentContract.Effect? = null
    private lateinit var targetLogic : LogicModel

    private val testRef = EpisodeRef(
        userId = sampleUserId,
        mode = ReadMode.EDIT,
        bookId = getBookId(sampleUserId, ReadMode.EDIT, 0),
        episodeId = getEpisodeId(sampleUserId, ReadMode.EDIT, 0)
    )

    private suspend fun ComposeTestRule.waitForInitEnd() {
        this.waitForIdle()
        this.waitUntil(
            timeoutMillis = 3000L
        ) {
            this.onAllNodesWithTag(LoadState.Init::class.java.simpleName)
                .fetchSemanticsNodes().isEmpty()
        }
        this.waitForIdle()
    }
    private suspend fun ComposeTestRule.waitForLoadingEnd() {
        this.waitForIdle()
        this.waitUntil(
            timeoutMillis = 3000L
        ) {
            this.onAllNodesWithTag(LoadState.Loading::class.java.simpleName)
                .fetchSemanticsNodes().isEmpty()
        }
        this.waitForIdle()
    }

    @ExperimentalAnimationApi
    @Before
    fun setUp() = runBlocking {
        hiltRule.inject()
        viewModelFactory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(
                modelClass: Class<T>,
                extras: CreationExtras
            ): T {
                return ViewerContentViewModel(
                    saveStatedHandle,
                    useCasePageSetting,
                    useCaseLoadBook,
                    useCaseBookLogic
                ) as T
            }
        }

        useCaseBookList.makeSampleEpisode()
        targetLogic = useCaseLoadBook.loadLogic(testRef)
        setupViewModel()

        composeRule.waitForInitEnd()
    }

    private fun setupViewModel(){
        saveStatedHandle[ArgName.NAME_USER_ID] = testRef.userId
        saveStatedHandle[ArgName.NAME_READ_MODE] = testRef.mode
        saveStatedHandle[ArgName.NAME_BOOK_ID] = testRef.bookId
        saveStatedHandle[ArgName.NAME_EPISODE_ID] = testRef.episodeId
        saveStatedHandle[ArgName.NAME_BOOK_TITLE] = ""
        saveStatedHandle[ArgName.NAME_EPISODE_TITLE] = ""
        saveStatedHandle[ArgName.NAME_PAGE_ID] = 1L

        composeRule.activity.setContent {
            viewModel = viewModel(checkNotNull(LocalViewModelStoreOwner.current), factory = viewModelFactory)
            testEventSent = remember { viewModel::setEvent }
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

            val onNavigationRequested : (ViewerContentContract.Effect.Navigation) -> Unit =  remember {
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
    }

    @Test
    fun `동작 - OnConfirmMoveSavePageDialog, 결과 - Success (pageId 에서 3으로 이동)`()  = runTest {
        // Init
        val startPageId = targetLogic.logics.first { it.type == PageType.START }.pageId
        val initPageId = viewModel.uiState.value.pageWrapper?.id
        println("currentPageId : $initPageId, startPageId : $startPageId")
        Truth.assertThat(initPageId).isEqualTo(startPageId)

        // Call Event
        val savedPageId = 3L
        testEventSent(ViewerContentContract.Event.OnConfirmMoveSavePageDialog(savedPageId))
        composeRule.waitForLoadingEnd()

        val currentPageId = viewModel.uiState.value.pageWrapper?.id
        println("currentPageId : $currentPageId, savePageId : $savedPageId")
        Truth.assertThat(currentPageId).isEqualTo(savedPageId)
    }

    @Test
    fun `동작 - OnClickSelector, 결과 - Success (선택지로 페이지 이동)`()  = runTest {
        // Init
        val initPageId = viewModel.uiState.value.pageWrapper?.id!!
        val firstSelector = targetLogic.logics.first { it.pageId == initPageId }.selectors[0]
        val nextPageId = useCaseBookLogic.getNextRoutePageId(testRef, firstSelector.routes)

        // Call Event
        testEventSent(ViewerContentContract.Event.OnClickSelector(initPageId, firstSelector.selectorId))
        composeRule.waitForLoadingEnd()

        val currentPageId = viewModel.uiState.value.pageWrapper?.id
        println("currentPageId : $currentPageId, initPageId : $initPageId, nextPageId : $nextPageId")
        Truth.assertThat(currentPageId).isEqualTo(nextPageId)
    }

    @Test
    fun `동작 - ChangeSettingPageTheme, 결과 - Success`()  = runTest {
        // Init
        val beforeTheme = viewModel.uiState.value.pageSetting.pageTheme

        // Call Event
        testEventSent(ViewerContentContract.Event.SettingEvent.ChangeSettingPageTheme(PageTheme.THEME_IVORY))
        composeRule.waitForLoadingEnd()

        val currentTheme = viewModel.uiState.value.pageSetting.pageTheme
        println("beforeTheme.pageTheme : ${beforeTheme}, currentTheme.pageTheme : ${currentTheme}")
        Truth.assertThat(beforeTheme).isNotEqualTo(currentTheme)
        Truth.assertThat(currentTheme).isEqualTo(PageTheme.THEME_IVORY)
    }

    @Test
    fun `동작 - SettingContentTextSize, 결과 - Success`()  = runTest {
        // Init
        val origin = viewModel.uiState.value.pageSetting.pageContentSetting.textSize
        val incValue = PageTextSize.values[PageTextSize.values.indexOf(origin) + 1]

        // Call Event Increment
        testEventSent(ViewerContentContract.Event.SettingEvent.IncrementSettingContentTextSize)
        composeRule.waitForLoadingEnd()

        var currentValue = viewModel.uiState.value.pageSetting.pageContentSetting.textSize
        Truth.assertThat(currentValue).isEqualTo(incValue)

        // Call Event Decrement
        testEventSent(ViewerContentContract.Event.SettingEvent.DecrementSettingContentTextSize)
        composeRule.waitForLoadingEnd()

        currentValue = viewModel.uiState.value.pageSetting.pageContentSetting.textSize
        Truth.assertThat(currentValue).isEqualTo(origin)
    }

    @Test
    fun `동작 - SettingContentLineHeight, 결과 - Success`()  = runTest {
        // Init
        val origin = viewModel.uiState.value.pageSetting.pageContentSetting.lineHeight
        val incValue = PageLineHeight.values[PageLineHeight.values.indexOf(origin) + 1]

        // Call Event Increment
        testEventSent(ViewerContentContract.Event.SettingEvent.IncrementSettingContentLineHeight)
        composeRule.waitForLoadingEnd()

        var currentValue = viewModel.uiState.value.pageSetting.pageContentSetting.lineHeight
        Truth.assertThat(currentValue).isEqualTo(incValue)

        // Call Event Decrement
        testEventSent(ViewerContentContract.Event.SettingEvent.DecrementSettingContentLineHeight)
        composeRule.waitForLoadingEnd()

        currentValue = viewModel.uiState.value.pageSetting.pageContentSetting.lineHeight
        Truth.assertThat(currentValue).isEqualTo(origin)
    }

    @Test
    fun `동작 - SettingContentTextMarginHorizontal, 결과 - Success`()  = runTest {
        // Init
        val origin = viewModel.uiState.value.pageSetting.pageContentSetting.textMarginHorizontal
        val incValue = PageMarginHorizontal.values[PageMarginHorizontal.values.indexOf(origin) + 1]

        // Call Event Increment
        testEventSent(ViewerContentContract.Event.SettingEvent.IncrementSettingContentTextMarginHorizontal)
        composeRule.waitForLoadingEnd()

        var currentValue = viewModel.uiState.value.pageSetting.pageContentSetting.textMarginHorizontal
        Truth.assertThat(currentValue).isEqualTo(incValue)

        // Call Event Decrement
        testEventSent(ViewerContentContract.Event.SettingEvent.DecrementSettingContentTextMarginHorizontal)
        composeRule.waitForLoadingEnd()

        currentValue = viewModel.uiState.value.pageSetting.pageContentSetting.textMarginHorizontal
        Truth.assertThat(currentValue).isEqualTo(origin)
    }

    @Test
    fun `동작 - SettingContentImageMarginHorizontal, 결과 - Success`()  = runTest {
        // Init
        val origin = viewModel.uiState.value.pageSetting.pageContentSetting.imageMarginHorizontal
        val incValue = PageMarginHorizontal.values[PageMarginHorizontal.values.indexOf(origin) + 1]

        // Call Event Increment
        testEventSent(ViewerContentContract.Event.SettingEvent.IncrementSettingContentImageMarginHorizontal)
        composeRule.waitForLoadingEnd()

        var currentValue = viewModel.uiState.value.pageSetting.pageContentSetting.imageMarginHorizontal
        Truth.assertThat(currentValue).isEqualTo(incValue)

        // Call Event Decrement
        testEventSent(ViewerContentContract.Event.SettingEvent.DecrementSettingContentImageMarginHorizontal)
        composeRule.waitForLoadingEnd()

        currentValue = viewModel.uiState.value.pageSetting.pageContentSetting.imageMarginHorizontal
        Truth.assertThat(currentValue).isEqualTo(origin)
    }

    @Test
    fun `동작 - SettingSelectorTextSize, 결과 - Success`()  = runTest {
        // Init
        val origin = viewModel.uiState.value.pageSetting.selectorSetting.textSize
        val incValue = PageTextSize.values[PageTextSize.values.indexOf(origin) + 1]

        // Call Event Increment
        testEventSent(ViewerContentContract.Event.SettingEvent.IncrementSettingSelectorTextSize)
        composeRule.waitForLoadingEnd()

        var currentValue = viewModel.uiState.value.pageSetting.selectorSetting.textSize
        Truth.assertThat(currentValue).isEqualTo(incValue)

        // Call Event Decrement
        testEventSent(ViewerContentContract.Event.SettingEvent.DecrementSettingSelectorTextSize)
        composeRule.waitForLoadingEnd()

        currentValue = viewModel.uiState.value.pageSetting.selectorSetting.textSize
        Truth.assertThat(currentValue).isEqualTo(origin)
    }

    @Test
    fun `동작 - SettingSelectorPaddingVertical, 결과 - Success`()  = runTest {
        // Init
        val origin = viewModel.uiState.value.pageSetting.selectorSetting.paddingVertical
        val incValue = SelectorPaddingVertical.values[SelectorPaddingVertical.values.indexOf(origin) + 1]

        // Call Event Increment
        testEventSent(ViewerContentContract.Event.SettingEvent.IncrementSettingSelectorPaddingVertical)
        composeRule.waitForLoadingEnd()

        var currentValue = viewModel.uiState.value.pageSetting.selectorSetting.paddingVertical
        Truth.assertThat(currentValue).isEqualTo(incValue)

        // Call Event Decrement
        testEventSent(ViewerContentContract.Event.SettingEvent.DecrementSettingSelectorPaddingVertical)
        composeRule.waitForLoadingEnd()

        currentValue = viewModel.uiState.value.pageSetting.selectorSetting.paddingVertical
        Truth.assertThat(currentValue).isEqualTo(origin)
    }
}
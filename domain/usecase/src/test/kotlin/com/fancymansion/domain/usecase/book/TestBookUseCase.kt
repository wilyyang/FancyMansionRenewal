package com.fancymansion.domain.usecase.book

import com.fancymansion.core.common.const.EpisodeRef
import com.fancymansion.core.common.const.ReadMode
import com.fancymansion.core.common.const.getBookId
import com.fancymansion.core.common.const.getEpisodeId
import com.fancymansion.core.common.const.testUserId
import com.fancymansion.core.common.di.HiltCommon
import com.fancymansion.di.injectRepository.HiltRepository
import com.fancymansion.domain.model.book.EditorModel
import com.fancymansion.domain.model.book.PageSettingModel
import com.google.common.truth.Truth
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
class TestBookUseCase {
    @get:Rule
    var hiltRule = HiltAndroidRule(this)

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

    private val testRef = EpisodeRef(
        userId = testUserId,
        mode = ReadMode.READ,
        bookId = getBookId(testUserId, 0),
        episodeId = getEpisodeId(testUserId, 0)
    )


    @Before
    fun setUp() = runTest {
        hiltRule.inject()
        useCaseBookList.makeSampleEpisode(testRef, EditorModel())
    }

    @Test
    fun `대상 - PageSetting, 동작 - get, 결과 - equal null`()  = runTest {
        val loadSetting = useCasePageSetting.getPageSetting(testRef.userId, testRef.mode.name, testRef.episodeId)
        Truth.assertThat(loadSetting == null).isTrue()
    }

    @Test
    fun `대상 - PageSetting, 동작 - save, 결과 - Success equal`()  = runTest {
        val defaultSetting = PageSettingModel()
        useCasePageSetting.savePageSetting(testRef.userId, testRef.mode.name, testRef.episodeId, defaultSetting)
        val loadSetting = useCasePageSetting.getPageSetting(testRef.userId, testRef.mode.name, testRef.episodeId)
        Truth.assertThat(loadSetting == defaultSetting).isTrue()
    }

    @Test
    fun `대상 - ProgressPageId, 동작 - get, 결과 - equal null`()  = runTest {
        val id = useCaseBookLogic.getReadingProgressPageId(testRef)
        Truth.assertThat(id == null).isTrue()
    }

    @Test
    fun `대상 - ProgressPageId, 동작 - update, 결과 - Success equal`()  = runTest {
        useCaseBookLogic.updateReadingProgressPageId(testRef, 3)
        val id = useCaseBookLogic.getReadingProgressPageId(testRef)
        Truth.assertThat(id == 3L).isTrue()
    }

    /**
     * Test Selector
     *     "pageId": 5,
     *     "selectorId": 3
     */
    private val TEST_SELECTOR_CONDITION_PAGE_ID = 5L

    private val TEST_SELECTOR_CONDITION_SELF_ID = 1L
    private val TEST_SELECTOR_CONDITION_TARGET_ID = 2L
    @Test
    fun `대상 - Logic, 동작 - selector default count, 결과 - Success`()  = runTest {
        val logic = useCaseLoadBook.loadLogic(testRef)
        val selectors = useCaseBookLogic.getVisibleSelectors(episodeRef = testRef, logic = logic, pageId = TEST_SELECTOR_CONDITION_PAGE_ID)
        Truth.assertThat(selectors.size == 2).isTrue()
    }

    @Test
    fun `대상 - Logic, 동작 - selector condition, 결과 - Fail`()  = runTest {
        val logic = useCaseLoadBook.loadLogic(testRef)
        useCaseBookLogic.incrementActionCount(testRef, TEST_SELECTOR_CONDITION_PAGE_ID, TEST_SELECTOR_CONDITION_SELF_ID)
        useCaseBookLogic.incrementActionCount(testRef, TEST_SELECTOR_CONDITION_PAGE_ID, TEST_SELECTOR_CONDITION_TARGET_ID)
        val selectors = useCaseBookLogic.getVisibleSelectors(episodeRef = testRef, logic = logic, pageId = TEST_SELECTOR_CONDITION_PAGE_ID)
        Truth.assertThat(selectors.size == 3).isFalse()
    }
    @Test
    fun `대상 - Logic, 동작 - selector condition, 결과 - Success`()  = runTest {
        val logic = useCaseLoadBook.loadLogic(testRef)
        useCaseBookLogic.incrementActionCount(testRef, TEST_SELECTOR_CONDITION_PAGE_ID, TEST_SELECTOR_CONDITION_SELF_ID)
        useCaseBookLogic.incrementActionCount(testRef, TEST_SELECTOR_CONDITION_PAGE_ID, TEST_SELECTOR_CONDITION_SELF_ID)
        useCaseBookLogic.incrementActionCount(testRef, TEST_SELECTOR_CONDITION_PAGE_ID, TEST_SELECTOR_CONDITION_TARGET_ID)
        val selectors = useCaseBookLogic.getVisibleSelectors(episodeRef = testRef, logic = logic, pageId = TEST_SELECTOR_CONDITION_PAGE_ID)
        Truth.assertThat(selectors.size == 3).isTrue()
    }

    /**
     * Test Route
     * "pageId": 1,
     * "selectorId": 2
     * "routeId": 1
     */
    private val TEST_ROUTE_CONDITION_PAGE_ID = 1L
    private val TEST_ROUTE_CONDITION_SELECTOR_ID = 2L

    private val TEST_ROUTE_SELF_PAGE_ID = 2L
    private val TEST_ROUTE_SELF_SELECTOR_ID = 2L

    private val TEST_DEFAULT_NEXT_PAGE_ID = 4L
    private val TEST_ROUTE_CONDITION_NEXT_PAGE_ID = 5L

    @Test
    fun `대상 - Logic, 동작 - route default, 결과 - Success (next page 4)`()  = runTest {
        val logic = useCaseLoadBook.loadLogic(testRef)
        val selectors = useCaseBookLogic.getVisibleSelectors(episodeRef = testRef, logic = logic, pageId = TEST_ROUTE_CONDITION_PAGE_ID)
        val selector = selectors.first { it.selectorId == TEST_ROUTE_CONDITION_SELECTOR_ID }

        val nextPageId = useCaseBookLogic.getNextRoutePageId(testRef, selector.routes)
        Truth.assertThat(nextPageId == TEST_DEFAULT_NEXT_PAGE_ID).isTrue()
    }

    @Test
    fun `대상 - Logic, 동작 - route condition, 결과 - Fail (next page 5)`()  = runTest {
        val logic = useCaseLoadBook.loadLogic(testRef)
        val selectors = useCaseBookLogic.getVisibleSelectors(episodeRef = testRef, logic = logic, pageId = TEST_ROUTE_CONDITION_PAGE_ID)
        val selector = selectors.first { it.selectorId == TEST_ROUTE_CONDITION_SELECTOR_ID }
        val nextPageId = useCaseBookLogic.getNextRoutePageId(testRef, selector.routes)
        Truth.assertThat(nextPageId == TEST_ROUTE_CONDITION_NEXT_PAGE_ID).isFalse()
    }

    @Test
    fun `대상 - Logic, 동작 - route condition, 결과 - Success (next page 5)`()  = runTest {
        val logic = useCaseLoadBook.loadLogic(testRef)
        val selectors = useCaseBookLogic.getVisibleSelectors(episodeRef = testRef, logic = logic, pageId = TEST_ROUTE_CONDITION_PAGE_ID)
        val selector = selectors.first { it.selectorId == TEST_ROUTE_CONDITION_SELECTOR_ID }

        useCaseBookLogic.incrementActionCount(testRef, pageId = TEST_ROUTE_SELF_PAGE_ID, selectorId = TEST_ROUTE_SELF_SELECTOR_ID)

        val nextPageId = useCaseBookLogic.getNextRoutePageId(testRef, selector.routes)
        Truth.assertThat(nextPageId == TEST_ROUTE_CONDITION_NEXT_PAGE_ID).isTrue()
    }
}
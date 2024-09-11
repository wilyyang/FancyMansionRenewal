package com.fancymansion.domain.usecase.book

import com.fancymansion.core.common.const.EpisodeRef
import com.fancymansion.core.common.const.ReadMode
import com.fancymansion.core.common.di.HiltCommon
import com.fancymansion.di.injectRepository.HiltRepository
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

    private val defaultRef = EpisodeRef(
        userId = "test_user_id",
        mode = ReadMode.READ,
        bookId = "test_book_id",
        episodeId = "test_book_id_0"
    )


    @Before
    fun setUp() = runTest {
        hiltRule.inject()
        useCaseMakeBook.makeSampleEpisode(defaultRef)
    }

    @Test
    fun `Given - UseCase, Target - PageSetting, When - get, Then - equal null`()  = runTest {
        val loadSetting = useCasePageSetting.getPageSetting(defaultRef.userId, defaultRef.mode.name, defaultRef.episodeId)
        Truth.assertThat(loadSetting == null).isTrue()
    }

    @Test
    fun `Given - UseCase, Target - PageSetting, When - save, Then - Success equal`()  = runTest {
        val defaultSetting = PageSettingModel()
        useCasePageSetting.savePageSetting(defaultRef.userId, defaultRef.mode.name, defaultRef.episodeId, defaultSetting)
        val loadSetting = useCasePageSetting.getPageSetting(defaultRef.userId, defaultRef.mode.name, defaultRef.episodeId)
        Truth.assertThat(loadSetting == defaultSetting).isTrue()
    }

    @Test
    fun `Given - UseCase, Target - ProgressPageId, When - get, Then - equal null`()  = runTest {
        val id = useCaseBookLogic.getReadingProgressPageId(defaultRef)
        Truth.assertThat(id == null).isTrue()
    }

    @Test
    fun `Given - UseCase, Target - ProgressPageId, When - update, Then - Success equal`()  = runTest {
        useCaseBookLogic.updateReadingProgressPageId(defaultRef, 3)
        val id = useCaseBookLogic.getReadingProgressPageId(defaultRef)
        Truth.assertThat(id!! == 3L).isTrue()
    }

    /**
     * Test Selector
     * "pageId": 1,
     *       "selectors": [
     *         { "selectorId": 1, "routes": [ { "routeTargetPageId": 2  } ] },
     *         { "selectorId": 2, "routes": [ { "routeTargetPageId": 3  } ] },
     *         { "selectorId": 3, "routes": [ { "routeTargetPageId": 11 } ],
     *           "showConditions": [
     *              { "conditionId": 1, "type": "SHOW_SELECTOR",
     *                "conditionRule": { "type": "COUNT", "count": 7, "logicalOp": "AND", "relationOp": "LESS_THAN",
     *                                   "selfActionId": {  "pageId": 1 } }
     *              },
     *              { "conditionId": 2, "type": "SHOW_SELECTOR",
     *                "conditionRule": { "type": "COUNT", "count": 1, "logicalOp": "AND", "relationOp": "GREATER_THAN",
     *                                   "selfActionId": {  "pageId": 2 } }
     *             },
     *             { "conditionId": 3, "type": "SHOW_SELECTOR"
     *               "conditionRule": { "type": "TARGET", "logicalOp": "OR", "relationOp": "LESS_THAN",
     *                                  "selfActionId": {  "pageId": 2 },
     *                                  "targetActionId": { "pageId": 3 } }
     *              }
     *           ]
     *         }
     *       ]
     */
    private val TEST_SELECTOR_CONDITION_COUNT_TARGET_PAGE_ID = 1L
    private val TEST_SELECTOR_CONDITION_TARGET_ID_2 = 2L
    private val TEST_SELECTOR_CONDITION_TARGET_ID_3 = 3L
    @Test
    fun `Given - UseCase, Target - Logic, When - selector first, Then - Success`()  = runTest {
        val logic = useCaseLoadBook.loadLogic(defaultRef)
        val selectors = useCaseBookLogic.getVisibleSelectors(episodeRef = defaultRef, logic = logic, pageId = TEST_SELECTOR_CONDITION_COUNT_TARGET_PAGE_ID)
        Truth.assertThat(selectors.size == 2).isTrue()
    }

    @Test
    fun `Given - UseCase, Target - Logic, When - selector condition, COUNT+TARGET, Then - Success`()  = runTest {
        val logic = useCaseLoadBook.loadLogic(defaultRef)
        useCaseBookLogic.incrementActionCount(defaultRef, TEST_SELECTOR_CONDITION_TARGET_ID_2)
        useCaseBookLogic.incrementActionCount(defaultRef, TEST_SELECTOR_CONDITION_TARGET_ID_2)
        useCaseBookLogic.incrementActionCount(defaultRef, TEST_SELECTOR_CONDITION_TARGET_ID_3)
        useCaseBookLogic.incrementActionCount(defaultRef, TEST_SELECTOR_CONDITION_TARGET_ID_3)
        useCaseBookLogic.incrementActionCount(defaultRef, TEST_SELECTOR_CONDITION_TARGET_ID_3)
        val selectors = useCaseBookLogic.getVisibleSelectors(episodeRef = defaultRef, logic = logic, pageId = TEST_SELECTOR_CONDITION_COUNT_TARGET_PAGE_ID)
        Truth.assertThat(selectors.size == 3).isTrue()
    }

    @Test
    fun `Given - UseCase, Target - Logic, When - selector condition, COUNT+TARGET, Then - Fail (TARGET not)`()  = runTest {
        val logic = useCaseLoadBook.loadLogic(defaultRef)
        useCaseBookLogic.incrementActionCount(defaultRef, TEST_SELECTOR_CONDITION_TARGET_ID_2)
        useCaseBookLogic.incrementActionCount(defaultRef, TEST_SELECTOR_CONDITION_TARGET_ID_2)
        useCaseBookLogic.incrementActionCount(defaultRef, TEST_SELECTOR_CONDITION_TARGET_ID_3)
        useCaseBookLogic.incrementActionCount(defaultRef, TEST_SELECTOR_CONDITION_TARGET_ID_3)
        val selectors = useCaseBookLogic.getVisibleSelectors(episodeRef = defaultRef, logic = logic, pageId = TEST_SELECTOR_CONDITION_COUNT_TARGET_PAGE_ID)
        Truth.assertThat(selectors.size == 3).isFalse()
    }

    /**
     * Test Route
     * "pageId": 5,
     *       "selectors": [
     *         {
     *           "selectorId": 1,
     *           "routes": [
     *             {  "routeId": 1,  "routeTargetPageId": 6,
     *                "routeConditions": [  {  "conditionId": 1,
     *                                         "conditionRule": {  "count": 2, "relationOp": "GREATER_THAN",
     *                                                             "selfActionId": { "pageId": 5, "selectorId": 1  },
     *                                                             "type": "COUNT" }  }  ]
     *             },
     *             {  "routeId": 2,  "routeTargetPageId": 5  }
     *       ]
     */
    private val TEST_ROUTE_CONDITION_COUNT_PAGE_ID = 5L
    private val TEST_ROUTE_CONDITION_COUNT_ID_5 = 5L

    private val TEST_ROUTE_TARGET_PAGE_ID_6 = 6L
    private val TEST_ROUTE_TARGET_PAGE_ID_5 = 5L

    @Test
    fun `Given - UseCase, Target - Logic, When - route default, Then - Success pageId 5`()  = runTest {
        val logic = useCaseLoadBook.loadLogic(defaultRef)
        val selectors = useCaseBookLogic.getVisibleSelectors(episodeRef = defaultRef, logic = logic, pageId = TEST_ROUTE_CONDITION_COUNT_PAGE_ID)
        val selector = selectors.first { it.selectorId == 1L }

        val nextPageId = useCaseBookLogic.getNextRoutePageId(defaultRef, selector.routes)
        Truth.assertThat(nextPageId == TEST_ROUTE_TARGET_PAGE_ID_5).isTrue()
    }

    @Test
    fun `Given - UseCase, Target - Logic, When - route count condition, Then - Fail pageId 6`()  = runTest {
        val logic = useCaseLoadBook.loadLogic(defaultRef)
        val selectors = useCaseBookLogic.getVisibleSelectors(episodeRef = defaultRef, logic = logic, pageId = TEST_ROUTE_CONDITION_COUNT_PAGE_ID)
        val selector = selectors.first { it.selectorId == 1L }

        useCaseBookLogic.incrementActionCount(defaultRef, pageId = 5L, selectorId = 1L)
        useCaseBookLogic.incrementActionCount(defaultRef, pageId = 5L, selectorId = 1L)

        val nextPageId = useCaseBookLogic.getNextRoutePageId(defaultRef, selector.routes)
        Truth.assertThat(nextPageId == TEST_ROUTE_TARGET_PAGE_ID_6).isFalse()
    }

    @Test
    fun `Given - UseCase, Target - Logic, When - route count condition, Then - Success pageId 6`()  = runTest {
        val logic = useCaseLoadBook.loadLogic(defaultRef)
        val selectors = useCaseBookLogic.getVisibleSelectors(episodeRef = defaultRef, logic = logic, pageId = TEST_ROUTE_CONDITION_COUNT_PAGE_ID)
        val selector = selectors.first { it.selectorId == 1L }

        useCaseBookLogic.incrementActionCount(defaultRef, pageId = 5L, selectorId = 1L)
        useCaseBookLogic.incrementActionCount(defaultRef, pageId = 5L, selectorId = 1L)
        useCaseBookLogic.incrementActionCount(defaultRef, pageId = 5L, selectorId = 1L)

        val nextPageId = useCaseBookLogic.getNextRoutePageId(defaultRef, selector.routes)
        Truth.assertThat(nextPageId == TEST_ROUTE_TARGET_PAGE_ID_6).isTrue()
    }
}
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

    private val pageIdCountConditionRule = 3L
    private val routeIdDefault = 6L


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
     * Test Target
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
    @Test
    fun `Given - UseCase, Target - Logic, When - selector first, Then - Success`()  = runTest {
        val logic = useCaseLoadBook.loadLogic(defaultRef)
        val selectors = useCaseBookLogic.getVisibleSelectors(episodeRef = defaultRef, logic = logic, pageId = 1L)
        Truth.assertThat(selectors.size == 2).isTrue()
    }

    @Test
    fun `Given - UseCase, Target - Logic, When - selector condition, Then - Success`()  = runTest {
        val logic = useCaseLoadBook.loadLogic(defaultRef)
        useCaseBookLogic.incrementActionCount(defaultRef, 2)
        useCaseBookLogic.incrementActionCount(defaultRef, 2)
        useCaseBookLogic.incrementActionCount(defaultRef, 3)
        useCaseBookLogic.incrementActionCount(defaultRef, 3)
        useCaseBookLogic.incrementActionCount(defaultRef, 3)
        val selectors = useCaseBookLogic.getVisibleSelectors(episodeRef = defaultRef, logic = logic, pageId = 1L)
        Truth.assertThat(selectors.size == 3).isTrue()
    }
}
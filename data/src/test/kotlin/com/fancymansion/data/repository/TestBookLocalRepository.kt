package com.fancymansion.data.repository

import com.fancymansion.core.common.const.EpisodeRef
import com.fancymansion.core.common.const.PageTheme
import com.fancymansion.core.common.const.ReadMode
import com.fancymansion.core.common.const.getBookId
import com.fancymansion.core.common.const.getEpisodeId
import com.fancymansion.core.common.const.testUserId
import com.fancymansion.core.common.di.HiltCommon
import com.fancymansion.data.datasource.appStorage.book.BookStorageSource
import com.fancymansion.data.datasource.appStorage.book.di.HiltBookStorage
import com.fancymansion.data.datasource.appStorage.book.model.asModel
import com.fancymansion.data.datasource.database.book.di.HiltBookDatabaseHelper
import com.fancymansion.domain.interfaceRepository.BookLocalRepository
import com.fancymansion.domain.model.book.ActionIdModel
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
@UninstallModules(HiltCommon::class, HiltBookStorage::class, HiltBookDatabaseHelper::class)
class TestBookLocalRepository {
    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var repository : BookLocalRepository
    @Inject
    lateinit var bookStorage : BookStorageSource

    private val testRef = EpisodeRef(
        userId = testUserId,
        mode = ReadMode.READ,
        bookId = getBookId(testUserId, 0),
        episodeId = getEpisodeId(testUserId, 0)
    )

    @Before
    fun setUp() = runTest {
        hiltRule.inject()
        repository.makeSampleEpisode(testRef, EditorModel())
    }

    @Test
    fun `대상 - PageSetting, 동작 - insert, update, 결과 - Success (equal)`()  = runTest {
        val pageSetting = PageSettingModel()
        repository.insertPageSetting(testRef.userId, testRef.mode.name, testRef.bookId, pageSetting)

        val updateSetting = pageSetting.copy(pageTheme = PageTheme.THEME_IVORY)
        repository.updatePageSetting(testRef.userId, testRef.mode.name, testRef.bookId, updateSetting)

        val targetSetting = repository.getPageSetting(testRef.userId, testRef.mode.name, testRef.bookId)

        Truth.assertThat(pageSetting == targetSetting).isFalse()
        Truth.assertThat(updateSetting == targetSetting).isTrue()
    }

    @Test
    fun `대상 - ActionCount, 동작 - insert, update, 결과 - Success (equal)`()  = runTest {
        val actionId = ActionIdModel(pageId = 1, selectorId = 1)
        repository.insertActionCount(testRef, actionId)
        val initValue = repository.getActionCount(testRef, actionId)
        Truth.assertThat(initValue == 1).isTrue()

        repository.updateActionCount(testRef, actionId, newCount = 2)
        val newValue = repository.getActionCount(testRef, actionId)
        Truth.assertThat(newValue == 2).isTrue()
    }

    @Test
    fun `대상 - ReadingProgress, 동작 - initValue, get, 결과 - Success (초기값 없음)`()  = runTest {
        val initPageId = repository.getReadingProgressPageId(testRef)
        Truth.assertThat(initPageId == null).isTrue()
    }

    @Test
    fun `대상 - ReadingProgress, 동작 - insert, update, 결과 - Success (디폴트값)`()  = runTest {
        repository.insertReadingProgress(testRef, pageId = 3)
        val insertPageId = repository.getReadingProgressPageId(testRef)
        Truth.assertThat(insertPageId?.toInt() == 3).isTrue()

        repository.updateReadingProgressPageId(testRef, newPageId = 5)
        val newPageId = repository.getReadingProgressPageId(testRef)
        Truth.assertThat(newPageId?.toInt() == 5).isTrue()
    }

    @Test
    fun `대상 - Logic, 동작 - load, 결과 - Success (변환 일치)`()  = runTest {
        val logic = bookStorage.loadLogic(testRef).asModel()
        val loadLogic = repository.loadLogic(testRef)
        Truth.assertThat(logic == loadLogic).isTrue()
    }

    @Test
    fun `대상 - Page, 동작 - load, 결과 - Success (변환 일치)`()  = runTest {
        val page = bookStorage.loadPage(testRef, pageId = 1).asModel()
        val loadPage = repository.loadPage(testRef, pageId = 1)
        Truth.assertThat(page == loadPage).isTrue()
    }

    @Test
    fun `대상 - Page, 동작 - load, 결과 - Fail (다른 페이지)`()  = runTest {
        val page1 = repository.loadPage(testRef, pageId = 1)
        val page2 = repository.loadPage(testRef, pageId = 2)
        Truth.assertThat(page1 == page2).isFalse()
    }
}
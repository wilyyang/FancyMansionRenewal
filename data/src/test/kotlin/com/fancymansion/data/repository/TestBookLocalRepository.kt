package com.fancymansion.data.repository

import com.fancymansion.core.common.const.EpisodeRef
import com.fancymansion.core.common.const.PageTheme
import com.fancymansion.core.common.const.ReadMode
import com.fancymansion.core.common.di.HiltCommon
import com.fancymansion.data.datasource.appStorage.book.BookStorageSource
import com.fancymansion.data.datasource.appStorage.book.di.HiltBookStorage
import com.fancymansion.data.datasource.appStorage.book.model.asModel
import com.fancymansion.data.datasource.database.book.di.HiltBookDatabaseHelper
import com.fancymansion.domain.interfaceRepository.BookLocalRepository
import com.fancymansion.domain.model.book.ActionIdModel
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

    private val defaultRef = EpisodeRef(
        userId = "test_user_id",
        mode = ReadMode.READ,
        bookId = "test_book_id",
        episodeId = "test_book_id_0"
    )

    @Before
    fun setUp() = runTest {
        hiltRule.inject()
        repository.makeSampleEpisode(defaultRef)
    }

    @Test
    fun `Given - Database, Target - PageSetting, When - insert, update, Then - Success equal`()  = runTest {
        val pageSetting = PageSettingModel()
        repository.insertPageSetting(defaultRef.userId, defaultRef.mode.name, defaultRef.bookId, pageSetting)

        val updateSetting = pageSetting.copy(pageTheme = PageTheme.THEME_IVORY)
        repository.updatePageSetting(defaultRef.userId, defaultRef.mode.name, defaultRef.bookId, updateSetting)

        val targetSetting = repository.getPageSetting(defaultRef.userId, defaultRef.mode.name, defaultRef.bookId)

        Truth.assertThat(pageSetting == targetSetting).isFalse()
        Truth.assertThat(updateSetting == targetSetting).isTrue()
    }

    @Test
    fun `Given - Database, Target - ActionCount, When - insert, update, Then - Success equal`()  = runTest {
        val actionId = ActionIdModel(pageId = 1, selectorId = 1)
        repository.insertActionCount(defaultRef, actionId)
        val initValue = repository.getActionCount(defaultRef, actionId)
        Truth.assertThat(initValue == 1).isTrue()

        repository.updateActionCount(defaultRef, actionId, newCount = 2)
        val newValue = repository.getActionCount(defaultRef, actionId)
        Truth.assertThat(newValue == 2).isTrue()
    }

    @Test
    fun `Given - Database, Target - ReadingProgress, When - initValue, get, Then - Success equal null`()  = runTest {
        val initPageId = repository.getReadingProgressPageId(defaultRef)
        Truth.assertThat(initPageId == null).isTrue()
    }

    @Test
    fun `Given - Database, Target - ReadingProgress, When - insert, update, Then - Success equal`()  = runTest {
        repository.insertReadingProgress(defaultRef, pageId = 3)
        val insertPageId = repository.getReadingProgressPageId(defaultRef)
        Truth.assertThat(insertPageId?.toInt() == 3).isTrue()

        repository.updateReadingProgressPageId(defaultRef, newPageId = 5)
        val newPageId = repository.getReadingProgressPageId(defaultRef)
        Truth.assertThat(newPageId?.toInt() == 5).isTrue()
    }

    @Test
    fun `Given - Storage, Target - Logic, When - load, Then - Success equal`()  = runTest {
        val logic = bookStorage.loadLogic(defaultRef).asModel()
        val loadLogic = repository.loadLogic(defaultRef)
        Truth.assertThat(logic == loadLogic).isTrue()
    }

    @Test
    fun `Given - Storage, Target - Page, When - load, Then - Success equal`()  = runTest {
        val page = bookStorage.loadPage(defaultRef, pageId = 1).asModel()
        val loadPage = repository.loadPage(defaultRef, pageId = 1)
        Truth.assertThat(page == loadPage).isTrue()
    }

    @Test
    fun `Given - Storage, Target - Page, When - load, Then - Fail not equal`()  = runTest {
        val page1 = repository.loadPage(defaultRef, pageId = 1)
        val page2 = repository.loadPage(defaultRef, pageId = 2)
        Truth.assertThat(page1 == page2).isFalse()
    }
}
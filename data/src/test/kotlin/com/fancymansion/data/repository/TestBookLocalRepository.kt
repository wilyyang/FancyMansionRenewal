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



    @Before
    fun setUp() = runTest {
        hiltRule.inject()
    }

    private val defaultRef = EpisodeRef(
        userId = "test_user_id",
        mode = ReadMode.READ,
        bookId = "test_book_id",
        episodeId = "test_book_id_0"
    )

    @Test
    fun `Given - Database, Target - PageSetting, When - insert, load, get, Then - Success equal`()  = runTest {
        val pageSetting = PageSettingModel()
        repository.insertPageSetting(defaultRef.userId, defaultRef.mode.name, defaultRef.bookId, pageSetting)

        val updateSetting = pageSetting.copy(pageTheme = PageTheme.THEME_IVORY)
        repository.updatePageSetting(defaultRef.userId, defaultRef.mode.name, defaultRef.bookId, updateSetting)

        val targetSetting = repository.getPageSetting(defaultRef.userId, defaultRef.mode.name, defaultRef.bookId)

        Truth.assertThat(pageSetting == targetSetting).isFalse()
        Truth.assertThat(updateSetting == targetSetting).isTrue()
    }

    @Test
    fun `Given - Storage, Target - Logic, When - load, Then - Success equal`()  = runTest {
        val logic = bookStorage.loadLogic(defaultRef).asModel()
        val loadLogic = repository.loadLogic(defaultRef)
        Truth.assertThat(logic == loadLogic).isTrue()
    }
}
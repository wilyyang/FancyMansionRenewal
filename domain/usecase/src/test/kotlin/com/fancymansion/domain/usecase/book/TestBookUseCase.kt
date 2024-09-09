package com.fancymansion.domain.usecase.book

import com.fancymansion.core.common.const.EpisodeRef
import com.fancymansion.core.common.const.ReadMode
import com.fancymansion.core.common.di.HiltCommon
import com.fancymansion.di.injectRepository.HiltRepository
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
    lateinit var useCaseBookLogic: UseCaseBookLogic

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
    fun `Given - BookUseCase, Target - PageId, When - Update, Then - Success Equal`()  = runTest {
        useCaseBookLogic.updateReadingProgressPageId(defaultRef, 1L)
        val id = useCaseBookLogic.getReadingProgressPageId(defaultRef)
        Truth.assertThat(id == 1L).isTrue()
    }
}
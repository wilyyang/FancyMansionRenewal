package com.fancymansion.test.fake.bookStorage

import android.content.Context
import com.fancymansion.core.common.const.EpisodeRef
import com.fancymansion.core.common.const.ReadMode
import com.fancymansion.data.datasource.appStorage.book.BookStorageSource
import com.fancymansion.data.datasource.appStorage.book.coverFile
import com.fancymansion.data.datasource.appStorage.book.episodeThumbnailFile
import com.fancymansion.data.datasource.appStorage.book.model.BookInfoData
import com.fancymansion.data.datasource.appStorage.book.model.EpisodeInfoData
import com.fancymansion.data.datasource.appStorage.book.model.LogicData
import com.fancymansion.data.datasource.appStorage.book.model.PageData
import com.fancymansion.data.datasource.appStorage.book.pageImageFile
import com.fancymansion.test.fake.bookStorage.data.FakeBookStorageData
import java.io.File

class FakeBookStorageSource(context: Context) : BookStorageSource {

    private val root : File = File("/test_root/")
    private val fakeBookData = FakeBookStorageData(context)

    /**
     * Dir
     */
    override suspend fun makeUserDir(userId: String) { }

    override suspend fun deleteUserDir(userId: String) { }

    override suspend fun makeBookDir(userId: String, mode: ReadMode, bookId: String) { }

    override suspend fun deleteBookDir(userId: String, mode: ReadMode, bookId: String) { }

    override suspend fun makeEpisodeDir(episodeRef: EpisodeRef) { }

    override suspend fun deleteEpisodeDir(episodeRef: EpisodeRef) { }

    /**
     * File
     */
    override suspend fun makeBookInfo(
        userId: String,
        mode: ReadMode,
        bookId: String,
        bookInfo: BookInfoData
    ): Boolean = true

    override suspend fun loadBookInfo(
        userId: String,
        mode: ReadMode,
        bookId: String
    ): BookInfoData = fakeBookData.getBookInfo_base()

    override suspend fun makeEpisodeInfo(
        episodeRef: EpisodeRef,
        episodeInfo: EpisodeInfoData
    ): Boolean = true

    override suspend fun loadEpisodeInfo(episodeRef: EpisodeRef): EpisodeInfoData = fakeBookData.getEpisodeInfo_base()

    override suspend fun makeLogic(episodeRef: EpisodeRef, logic: LogicData): Boolean = true

    override suspend fun loadLogic(episodeRef: EpisodeRef): LogicData = fakeBookData.getLogic_base()

    override suspend fun makePage(episodeRef: EpisodeRef, pageId: Long, page: PageData): Boolean = true

    override suspend fun loadPage(episodeRef: EpisodeRef, pageId: Long): PageData = fakeBookData.getPage_base(pageId)

    override suspend fun loadPageImage(episodeRef: EpisodeRef, imageName: String): File =
        root.pageImageFile(episodeRef, imageName)

    override suspend fun loadEpisodeThumbnail(episodeRef: EpisodeRef, imageName: String): File =
        root.episodeThumbnailFile(episodeRef, imageName)

    override suspend fun loadCoverImage(
        userId: String,
        mode: ReadMode,
        bookId: String,
        imageName: String
    ): File = root.coverFile(userId, mode, bookId, imageName)

    override suspend fun makePageImageFromResource(
        episodeRef: EpisodeRef,
        imageName: String,
        resourceId: Int
    ) { }

    override suspend fun makeEpisodeThumbnailFromResource(
        episodeRef: EpisodeRef,
        imageName: String,
        resourceId: Int
    ) { }

    override suspend fun makeCoverImageFromResource(
        userId: String,
        mode: ReadMode,
        bookId: String,
        imageName: String,
        resourceId: Int
    ) { }

    override suspend fun makeSampleEpisode(episodeRef: EpisodeRef): Boolean { return true }
}
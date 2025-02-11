package com.fancymansion.test.fake.bookStorage

import android.content.Context
import android.net.Uri
import com.fancymansion.core.common.const.EpisodeRef
import com.fancymansion.core.common.const.ReadMode
import com.fancymansion.core.common.util.readModuleRawFile
import com.fancymansion.core.common.util.type
import com.fancymansion.data.R
import com.fancymansion.data.datasource.appStorage.book.BookStorageSource
import com.fancymansion.data.datasource.appStorage.book.coverFile
import com.fancymansion.data.datasource.appStorage.book.episodeThumbnailFile
import com.fancymansion.data.datasource.appStorage.book.model.BookInfoData
import com.fancymansion.data.datasource.appStorage.book.model.EpisodeInfoData
import com.fancymansion.data.datasource.appStorage.book.model.LogicData
import com.fancymansion.data.datasource.appStorage.book.model.PageData
import com.fancymansion.data.datasource.appStorage.book.model.SourceData
import com.fancymansion.data.datasource.appStorage.book.pageImageFile
import com.fancymansion.data.datasource.appStorage.book.sample.SAMPLE_IMAGE_LIST
import com.google.gson.GsonBuilder
import com.google.gson.JsonDeserializer
import com.google.gson.JsonSerializer
import java.io.File

class FakeBookStorageSource(private val context: Context) : BookStorageSource {

    private val root : File = File("/test_root/")

    private val bookInfoMap : HashMap<String, BookInfoData> = hashMapOf()
    private val episodeInfoMap : HashMap<String, EpisodeInfoData> = hashMapOf()
    private val logicMap : HashMap<String, LogicData> = hashMapOf()
    private val pageMap : HashMap<String, PageData> = hashMapOf()

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
    ): Boolean {
        bookInfoMap["$userId ${mode.name} $bookId"] = bookInfo
        return true
    }

    override suspend fun loadBookInfo(
        userId: String,
        mode: ReadMode,
        bookId: String
    ): BookInfoData = bookInfoMap["$userId ${mode.name} $bookId"]!!

    override suspend fun makeEpisodeInfo(
        episodeRef: EpisodeRef,
        episodeInfo: EpisodeInfoData
    ): Boolean {
        episodeInfoMap["${episodeRef.userId} ${episodeRef.mode.name} ${episodeRef.bookId} ${episodeRef.episodeId}"] =
            episodeInfo
        return true
    }

    override suspend fun loadEpisodeInfo(episodeRef: EpisodeRef): EpisodeInfoData =
        episodeInfoMap["${episodeRef.userId} ${episodeRef.mode.name} ${episodeRef.bookId} ${episodeRef.episodeId}"]!!

    override suspend fun makeLogic(episodeRef: EpisodeRef, logic: LogicData): Boolean {
        logicMap["${episodeRef.userId} ${episodeRef.mode.name} ${episodeRef.bookId} ${episodeRef.episodeId}"] =
            logic
        return true
    }

    override suspend fun loadLogic(episodeRef: EpisodeRef): LogicData =
        logicMap["${episodeRef.userId} ${episodeRef.mode.name} ${episodeRef.bookId} ${episodeRef.episodeId}"]!!

    override suspend fun makePage(episodeRef: EpisodeRef, pageId: Long, page: PageData): Boolean {
        pageMap["${episodeRef.userId} ${episodeRef.mode.name} ${episodeRef.bookId} ${episodeRef.episodeId} $pageId"] =
            page
        return true
    }

    override suspend fun loadPage(episodeRef: EpisodeRef, pageId: Long): PageData =
        pageMap["${episodeRef.userId} ${episodeRef.mode.name} ${episodeRef.bookId} ${episodeRef.episodeId} $pageId"]!!

    override suspend fun deletePage(episodeRef: EpisodeRef, pageId: Long): Boolean {
        TODO("Not yet implemented")
    }

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

    override suspend fun deletePageImage(episodeRef: EpisodeRef, imageName: String): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun deleteCoverImage(
        userId: String,
        mode: ReadMode,
        bookId: String,
        imageName: String
    ): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun makePageImageFromUri(
        episodeRef: EpisodeRef,
        imageName: String,
        uri: Uri
    ): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun makeCoverImageFromUri(
        userId: String,
        mode: ReadMode,
        bookId: String,
        imageName: String,
        uri: Uri
    ): Boolean {
        TODO("Not yet implemented")
    }

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

    override suspend fun makeSampleEpisode(episodeRef: EpisodeRef): Boolean {
        val gson = GsonBuilder()
            .registerTypeAdapter(SourceData::class.java, JsonSerializer<SourceData> { src, _, context -> src?.toJson(context!!) })
            .registerTypeAdapter(SourceData::class.java, JsonDeserializer { json, _, context -> SourceData.fromJson(json!!, context!!) })
            .create()

        // make sample dir
        makeBookDir(
            userId = episodeRef.userId,
            mode = episodeRef.mode,
            bookId = episodeRef.bookId
        )

        makeEpisodeDir(
            episodeRef = episodeRef
        )

        // make sample image
        SAMPLE_IMAGE_LIST.forEach {
            makePageImageFromResource(episodeRef, it.first, it.second)
        }

        // read raw file
        val bookInfo : BookInfoData = gson.fromJson(context.readModuleRawFile(R.raw.base_book_info), type<BookInfoData>())
        val episodeInfo : EpisodeInfoData = gson.fromJson(context.readModuleRawFile(R.raw.base_episode_info), type<EpisodeInfoData>())
        val logic : LogicData = gson.fromJson(context.readModuleRawFile(R.raw.base_logic), type<LogicData>())
        val pages = (1..11).map { pageId ->
            val resourceId = getSamplePageResourceId(pageId)
            gson.fromJson<PageData>(context.readModuleRawFile(resourceId), type<PageData>())
        }


        // make sample file
        return makeBookInfo(
            userId = episodeRef.userId,
            mode = episodeRef.mode,
            bookId = episodeRef.bookId,
            bookInfo = bookInfo
        ) && makeEpisodeInfo(episodeRef = episodeRef, episodeInfo = episodeInfo)
                && makeLogic(episodeRef, logic)
                && pages.all { page -> makePage(episodeRef, page.id, page) }
    }

    private fun getSamplePageResourceId(pageId: Int): Int {
        val resourceName = "base_page_$pageId"
        return context.resources.getIdentifier(resourceName, "raw", context.packageName)
    }

    override suspend fun getPageImageFiles(episodeRef: EpisodeRef): Array<File> {
        TODO("Not yet implemented")
    }
}
package com.fancymansion.data.datasource.appStorage.book

import android.content.Context
import android.net.Uri
import com.fancymansion.core.common.const.EpisodeRef
import com.fancymansion.core.common.const.ReadMode
import com.fancymansion.core.common.util.readModuleRawFile
import com.fancymansion.core.common.util.type
import com.fancymansion.data.R
import com.fancymansion.data.datasource.appStorage.book.model.BookInfoData
import com.fancymansion.data.datasource.appStorage.book.model.EpisodeInfoData
import com.fancymansion.data.datasource.appStorage.book.model.LogicData
import com.fancymansion.data.datasource.appStorage.book.model.PageData
import com.fancymansion.data.datasource.appStorage.book.model.SourceData
import com.fancymansion.data.datasource.appStorage.book.sample.SAMPLE_IMAGE_LIST
import com.google.gson.GsonBuilder
import com.google.gson.JsonDeserializer
import com.google.gson.JsonSerializer
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.InputStream

class BookStorageSourceImpl(private val context : Context) : BookStorageSource {
    private val root : File = context.getExternalFilesDir(null)!!

    private val gson = GsonBuilder()
        .registerTypeAdapter(SourceData::class.java, JsonSerializer<SourceData> { src, _, context -> src?.toJson(context!!) })
        .registerTypeAdapter(SourceData::class.java, JsonDeserializer { json, _, context -> SourceData.fromJson(json!!, context!!) })
        .create()

    private fun File.writeJson(data: Any) : Boolean {
        if (exists()) {
            delete()
        }

        return FileOutputStream(this).use { stream ->
            stream.write(gson.toJson(data).toByteArray())
            exists()
        }
    }

    private fun File.readJson(clazz: Class<*>): Any {
        return FileInputStream(this).bufferedReader().use { stream ->
            gson.fromJson(stream.readText(), clazz)
        }
    }

    private suspend fun copyStreamToFile(inputStream: InputStream, outputFile: File) {
        outputFile.outputStream().use { outputStream ->
            inputStream.copyTo(outputStream, bufferSize = 1024)
        }
    }

    /**
     * Dir
     */
    override suspend fun makeUserDir(userId: String) {
        ReadMode.entries.forEach { mode ->
            File(root, BookPath.modePath(userId, mode)).let { modeDir ->
                if(!modeDir.exists()){
                    modeDir.mkdirs()
                }
            }
        }
    }

    override suspend fun deleteUserDir(userId: String) {
        File(root, BookPath.userPath(userId)).deleteRecursively()
    }

    override suspend fun makeBookDir(userId: String, mode: ReadMode, bookId: String) {
        root.bookFile(userId, mode, bookId).mkdirs()
    }

    override suspend fun deleteBookDir(userId: String, mode: ReadMode, bookId: String) {
        root.bookFile(userId, mode, bookId).deleteRecursively()
    }

    override suspend fun makeEpisodeDir(episodeRef: EpisodeRef) {
        root.mediaFile(episodeRef).mkdirs()
        root.pagesFile(episodeRef).mkdirs()
    }

    override suspend fun deleteEpisodeDir(episodeRef: EpisodeRef) {
        root.episodeFile(episodeRef).deleteRecursively()
    }

    /**
     * File
     */
    override suspend fun makeBookInfo(
        userId: String,
        mode: ReadMode,
        bookId: String,
        bookInfo: BookInfoData
    ): Boolean = root.bookInfoFile(userId, mode, bookId).writeJson(bookInfo)

    override suspend fun loadBookInfo(
        userId: String,
        mode: ReadMode,
        bookId: String
    ): BookInfoData = root.bookInfoFile(userId, mode, bookId).readJson(BookInfoData::class.java) as BookInfoData

    override suspend fun makeEpisodeInfo(
        episodeRef: EpisodeRef,
        episodeInfo: EpisodeInfoData
    ): Boolean = root.episodeInfoFile(episodeRef).writeJson(episodeInfo)

    override suspend fun loadEpisodeInfo(
        episodeRef: EpisodeRef
    ): EpisodeInfoData = root.episodeInfoFile(episodeRef).readJson(EpisodeInfoData::class.java) as EpisodeInfoData

    override suspend fun makeLogic(episodeRef: EpisodeRef, logic: LogicData): Boolean =
        root.logicFile(episodeRef).writeJson(logic)

    override suspend fun loadLogic(episodeRef: EpisodeRef): LogicData =
        root.logicFile(episodeRef).readJson(LogicData::class.java) as LogicData

    override suspend fun makePage(episodeRef: EpisodeRef, pageId: Long, page: PageData): Boolean =
        root.pageFile(episodeRef, pageId).writeJson(page)

    override suspend fun loadPage(episodeRef: EpisodeRef, pageId: Long): PageData =
        root.pageFile(episodeRef, pageId).readJson(PageData::class.java) as PageData

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

    override suspend fun deletePageImage(episodeRef: EpisodeRef, imageName: String): Boolean
    = root.pageImageFile(episodeRef, imageName).delete()

    override suspend fun deleteCoverImage(
        userId: String,
        mode: ReadMode,
        bookId: String,
        imageName: String
    ): Boolean = root.coverFile(userId, mode, bookId, imageName).delete()

    override suspend fun makePageImageFromUri(
        episodeRef: EpisodeRef,
        imageName: String,
        uri: Uri
    ): Boolean {
        return try {
            val targetFile = root.pageImageFile(episodeRef, imageName)
            context.contentResolver.openInputStream(uri)?.use { inputStream ->
                FileOutputStream(targetFile).use { outputStream ->
                    inputStream.copyTo(outputStream)
                }
            }
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    override suspend fun makeCoverImageFromUri(
        userId: String,
        mode: ReadMode,
        bookId: String,
        imageName: String,
        uri: Uri
    ): Boolean {
        return try {
            val targetFile = root.coverFile(userId, mode, bookId, imageName)
            context.contentResolver.openInputStream(uri)?.use { inputStream ->
                FileOutputStream(targetFile).use { outputStream ->
                    inputStream.copyTo(outputStream)
                }
            }
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    override suspend fun makePageImageFromResource(
        episodeRef: EpisodeRef,
        imageName: String,
        resourceId: Int
    ) {
        val inputStream = context.resources.openRawResource(resourceId)
        val outputFile = root.pageImageFile(episodeRef, imageName)
        copyStreamToFile(inputStream, outputFile)
    }

    override suspend fun makeEpisodeThumbnailFromResource(
        episodeRef: EpisodeRef,
        imageName: String,
        resourceId: Int
    ) {
        val inputStream = context.resources.openRawResource(resourceId)
        val outputFile = root.episodeThumbnailFile(episodeRef, imageName)
        copyStreamToFile(inputStream, outputFile)
    }

    override suspend fun makeCoverImageFromResource(
        userId: String,
        mode: ReadMode,
        bookId: String,
        imageName: String,
        resourceId: Int
    ) {
        val inputStream = context.resources.openRawResource(resourceId)
        val outputFile = root.coverFile(userId, mode, bookId, imageName)
        copyStreamToFile(inputStream, outputFile)
    }

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
        makeCoverImageFromResource(episodeRef.userId, episodeRef.mode, episodeRef.bookId, "test_book_id_cover_1.png", R.drawable.test_book_id_cover_1)
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
}
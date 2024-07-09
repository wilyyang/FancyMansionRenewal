package com.fancymansion.data.datasource.appStorage.book

import android.content.Context
import com.fancymansion.core.common.const.BookRef
import com.fancymansion.core.common.const.ReadMode
import com.fancymansion.core.common.log.Logger
import com.fancymansion.data.datasource.appStorage.book.model.ConfigData
import com.fancymansion.data.datasource.appStorage.book.model.LogicData
import com.fancymansion.data.datasource.appStorage.book.model.PageData
import com.fancymansion.data.datasource.appStorage.book.model.SourceData
import com.google.gson.GsonBuilder
import com.google.gson.JsonDeserializer
import com.google.gson.JsonSerializer
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.InputStream
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BookStorageSourceImpl @Inject internal constructor(
    @ApplicationContext private val context : Context
) : BookStorageSource {
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

    override suspend fun makeBookDir(bookRef: BookRef) {
        root.mediaFile(bookRef).mkdirs()
        root.pagesFile(bookRef).mkdirs()
    }

    override suspend fun deleteBookDir(bookRef: BookRef) {
        root.bookFile(bookRef).deleteRecursively()
    }

    /**
     * File
     */
    override suspend fun makeConfig(bookRef: BookRef, config: ConfigData): Boolean =
        root.configFile(bookRef).writeJson(config)

    override suspend fun loadConfig(bookRef: BookRef): ConfigData =
        root.configFile(bookRef).readJson(ConfigData::class.java) as ConfigData

    override suspend fun makeLogic(bookRef: BookRef, logic: LogicData): Boolean =
        root.logicFile(bookRef).writeJson(logic)

    override suspend fun loadLogic(bookRef: BookRef): LogicData =
        root.logicFile(bookRef).readJson(LogicData::class.java) as LogicData

    override suspend fun makePage(bookRef: BookRef, pageId: Long, page: PageData): Boolean =
        root.pageFile(bookRef, pageId).writeJson(page)

    override suspend fun loadPage(bookRef: BookRef, pageId: Long): PageData =
        root.pageFile(bookRef, pageId).readJson(PageData::class.java) as PageData

    override suspend fun loadImage(bookRef: BookRef, imageName: String) : File = root.pageImageFile(bookRef, imageName)

    override suspend fun loadCover(bookRef: BookRef, coverName: String) : File = root.coverFile(bookRef, coverName)

    override suspend fun makeImageFromResource(
        bookRef: BookRef,
        imageName: String,
        resourceId: Int
    ) {
        val inputStream = context.resources.openRawResource(resourceId)
        val outputFile = root.pageImageFile(bookRef, imageName)
        copyStreamToFile(inputStream, outputFile)
    }

    override suspend fun makeCoverFromResource(
        bookRef: BookRef,
        coverName: String,
        resourceId: Int
    ) {
        val inputStream = context.resources.openRawResource(resourceId)
        val outputFile = root.coverFile(bookRef, coverName)
        copyStreamToFile(inputStream, outputFile)
    }
}
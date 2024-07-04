package com.fancymansion.data.datasource.appStorage.book

import android.content.Context
import com.fancymansion.core.common.const.BookRef
import com.fancymansion.core.common.const.ReadMode
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BookStorageSourceImpl @Inject internal constructor(
    @ApplicationContext private val context : Context
) : BookStorageSource {
    private val root : File = context.getExternalFilesDir(null)!!

    suspend fun makeUserDir(userId: String) {
        ReadMode.entries.forEach { mode ->
            File(root, BookPath.modePath(userId, mode)).let { edit ->
                if(!edit.exists()){
                    edit.mkdirs()
                }
            }
        }
    }

    suspend fun deleteUserDir(userId: String) {
        File(root, BookPath.userPath(userId)).deleteRecursively()
    }

    suspend fun makeBookDir(bookRef: BookRef) {
        root.mediaFile(bookRef).mkdirs()
        root.contentFile(bookRef).mkdirs()
    }

    suspend fun deleteBookDir(bookRef: BookRef) {
        root.bookFile(bookRef).deleteRecursively()
    }

//
//    /**
//     * Config
//     */
//    suspend fun makeConfig(config : Config) = tryBooleanScope {
//        fileConfig(root, config.userId, ReadMode.from(config.readMode), config.bookId)?.let {
//            if (it.exists()) {
//                it.delete()
//            }
//
//            FileOutputStream(it).use { stream ->
//                stream.write(Gson().toJson(config).toByteArray())
//            }
//            it.exists()
//        } ?: false
//    }
//
//    suspend fun getConfig(userId : String, readMode : ReadMode, bookId : String) : ConfigData? = tryNullableScope {
//        fileConfig(root, userId, readMode, bookId)?.let {
//            if (it.exists()) {
//                val configJson = FileInputStream(it).bufferedReader().use { stream -> stream.readText() }
//                Gson().fromJson(configJson, ConfigData::class.java)
//            }else{
//                null
//            }
//        }
//    }
//
//    suspend fun makeLogicFile(logic : LogicData, userId : String, readMode : ReadMode, bookId : String) = tryBooleanScope {
//        fileLogic(root, userId, readMode, bookId)?.let {
//            if (it.exists()) {
//                it.delete()
//            }
//
//            FileOutputStream(it).use { stream ->
//                stream.write(Gson().toJson(logic).toByteArray())
//            }
//            it.exists()
//        } ?: false
//    }
//
//    suspend fun makePageContentFile(pageContent : PageContentData, userId : String, readMode : ReadMode, bookId : String) = tryBooleanScope {
//        filePage(root, userId, readMode, bookId, pageContent.pageId)?.let {
//            if (it.exists()) {
//                it.delete()
//            }
//
//            FileOutputStream(it).use { stream ->
//                stream.write(Gson().toJson(pageContent).toByteArray())
//            }
//            it.exists()
//        } ?: false
//    }
//
//    suspend fun makeCoverImageFileFromResource(
//        userId : String, readMode : ReadMode, bookId : String, imageName : String, resourceId : Int
//    ) {
//        fileCover(root, userId, readMode, bookId, imageName)?.let { file ->
//            val inputStream : InputStream = context.resources.openRawResource(resourceId)
//            val outputStream = FileOutputStream(file)
//            val buff = ByteArray(1024)
//
//            var read = 0
//            try {
//                while (inputStream.read(buff).also { read = it } > 0) {
//                    outputStream.write(buff, 0, read)
//                }
//            } finally {
//                inputStream.close()
//                outputStream.close()
//            }
//        }
//    }
//
//    suspend fun makeImageFileFromResource(userId : String, readMode : ReadMode, bookId : String, imageName : String, resourceId : Int) {
//        fileMediaImage(root, userId, readMode, bookId, imageName)?.let { file ->
//            val inputStream : InputStream = context.resources.openRawResource(resourceId)
//            val outputStream = FileOutputStream(file)
//            val buff = ByteArray(1024)
//
//            var read = 0
//            try {
//                while (inputStream.read(buff).also { read = it } > 0) {
//                    outputStream.write(buff, 0, read)
//                }
//            } finally {
//                inputStream.close()
//                outputStream.close()
//            }
//        }
//    }
//
//    /**
//     * Get Object
//     */
//    suspend fun getConfigFromFile(userId : String, readMode : ReadMode, bookId : String) : ConfigData? = tryNullableScope {
//        fileConfig(root, userId, readMode, bookId)?.let {
//            if (it.exists()) {
//                val configJson = FileInputStream(it).bufferedReader().use { stream -> stream.readText() }
//                Gson().fromJson(configJson, ConfigData::class.java)
//            }else{
//                null
//            }
//        }
//    }
//
//    suspend fun getCoverImageFromFile(userId : String, readMode : ReadMode, bookId : String, image : String) : File? = tryNullableScope {
//        fileCover(root, userId, readMode, bookId, image)
//    }
//
//    suspend fun getLogicFromFile(userId : String, readMode : ReadMode, bookId : String) : LogicData? = tryNullableScope {
//        fileLogic(root, userId, readMode, bookId)?.let {
//            if (it.exists()) {
//                val logicJson = FileInputStream(it).bufferedReader().use { stream -> stream.readText() }
//                Gson().fromJson(logicJson, LogicData::class.java)
//            }else{
//                null
//            }
//        }
//    }
//    suspend fun getPageContentFromFile(userId : String, readMode : ReadMode, bookId : String, pageId : Long) : PageContentData? = tryNullableScope {
//        filePage(root, userId, readMode, bookId, pageId)?.let {
//            if (it.exists()) {
//                val pageJson = FileInputStream(it).bufferedReader().use { stream -> stream.readText() }
//                Gson().fromJson(pageJson, PageContentData::class.java)
//            }else{
//                null
//            }
//        }
//    }
//
//    suspend fun getImageFromFile(userId : String, readMode : ReadMode, bookId : String, image : String) : File? = tryNullableScope {
//        fileMediaImage(root, userId, readMode, bookId, image)
//    }
}
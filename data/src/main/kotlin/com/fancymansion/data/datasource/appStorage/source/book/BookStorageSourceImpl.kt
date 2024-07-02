package com.fancymansion.data.datasource.appStorage.source.book

import com.fancymansion.core.common.const.BookRef
import com.fancymansion.core.common.util.joinPath
import java.io.File

class BookStorageSourceImpl : BookStorageSource {

    class BookPath(private val bookRef: BookRef) {
        private val book = "book".joinPath(bookRef.userId)
            .joinPath(bookRef.mode.name)
            .joinPath(bookRef.bookId)

        private val content = book.joinPath("content")

        val config = book.joinPath("config.json")
        val logic = content.joinPath("logic.json")

        fun pathImage(imageName: String) = content.joinPath("media").joinPath(imageName)
        fun pathPage(pageId: Long) = content.joinPath("page").joinPath("page_$pageId.json")
        fun pathCover(imageName: String) = book.joinPath(imageName)
    }

    fun initRootFolder():Boolean{
        return try{
            if(!rootPath.exists()){
                rootPath.mkdirs()
            }
            true
        }catch (e: Exception){
            false
        }
    }

    // make data
    fun makeBookFolder(bookId: Long): Boolean{
        try{
            val dir = File(rootPath, dirBook(bookId))
            if(dir.exists()){
                dir.deleteRecursively()
            }
            dir.mkdirs()
            val content = File(rootPath, dirContent(bookId))
            content.mkdirs()
            val media = File(rootPath, dirMedia(bookId))
            media.mkdirs()
            val slide = File(rootPath, dirSlide(bookId))
            slide.mkdirs()
            return true
        }catch (e: Exception){
            Log.d(TAG, ""+e.printStackTrace())
            return false
        }
    }

    fun makeConfigFile(config: Config): Boolean{
        try{
            val file = File(rootPath, fileConfig(config.bookId))
            if(file.exists()){
                file.delete()
            }
            FileOutputStream(file).use {
                it.write(Json.encodeToString(config).toByteArray())
            }
        }catch (e: Exception){
            Log.d(TAG, ""+e.printStackTrace())
            return false
        }
        return true
    }

    fun makeSlideFile(bookId: Long, slide: Slide): Boolean{
        try{
            val file = File(rootPath, fileSlide(bookId, slide.slideId))
            if(file.exists()){
                file.delete()
            }
            FileOutputStream(file).use {
                it.write(Json.encodeToString(slide).toByteArray())
            }
        }catch (e: Exception){
            Log.d(TAG, ""+e.printStackTrace())
            return false
        }
        return true
    }

    fun makeLogicFile(logic: Logic): Boolean{
        try{
            val file = File(rootPath, fileLogic(logic.bookId))
            if(file.exists()){
                file.delete()
            }
            FileOutputStream(file).use {
                it.write(Json.encodeToString(logic).toByteArray())
            }
        }catch (e: Exception){
            Log.d(TAG, ""+e.printStackTrace())
            return false
        }
        return true
    }

    // get data
    fun getConfigFromFile(bookId: Long): Config?{
        var config: Config? = null
        try{
            val file = File(rootPath, fileConfig(bookId))
            if(file.exists()){
                val configJson = FileInputStream(file).bufferedReader().use { it.readText() }
                config = Json.decodeFromString<Config>(configJson)
            }
        }catch (e: Exception){
            Log.d(TAG, ""+e.printStackTrace())
        }
        return config
    }

    fun getLogicFromFile(bookId: Long): Logic?{
        var logic: Logic? = null
        try{
            val file = File(rootPath, fileLogic(bookId))

            if(file.exists()){
                val logicJson = FileInputStream(file).bufferedReader().use { it.readText() }
                logic = Json.decodeFromString<Logic>(logicJson)
            }

        }catch (e: Exception){
            Log.d(TAG, ""+e.printStackTrace())
        }
        return logic
    }

    fun getSlideFromFile(bookId: Long, slideId: Long): Slide?{
        var slide: Slide? = null
        try{
            val file = File(rootPath, fileSlide(bookId, slideId))

            if(file.exists()){
                val slideJson = FileInputStream(file).bufferedReader().use { it.readText() }
                slide = Json.decodeFromString<Slide>(slideJson)
            }

        }catch (e: Exception){
            Log.d(TAG, ""+e.printStackTrace())
        }
        return slide
    }

    fun getImageFile(bookId: Long, imageName: String, isCover: Boolean = false): File?{
        val file = File(rootPath, if (isCover) {fileCover(bookId, imageName)} else {fileImage(bookId, imageName)})
        return if(imageName != "" && file.exists()) { file } else { null }
    }

    // Make Sample
    fun createSampleBookFiles(){
        val sampleId = 12345L
        val config = Json.decodeFromString<Config>(SampleBook.getConfigSample(sampleId))
        val logic = Json.decodeFromString<Logic>(SampleBook.getLogicSample(sampleId))

        makeBookFolder(config.bookId)
        makeConfigFile(config)

        for(i in 1 .. 11){
            val slide = Json.decodeFromString<Slide>(SampleBook.getSlideSample(i * 1_00_00_00_00L))
            makeSlideFile(config.bookId, slide)
        }

        makeLogicFile(logic)
        val array = arrayOf("image_1.gif", "image_2.gif", "image_3.gif", "image_4.gif", "image_5.gif", "image_6.gif", "fish_cat.jpg", "game_end.jpg")
        for (imageName in array){
            val file = File(rootPath, fileImage(sampleId, imageName))
            val input: InputStream = context.resources.openRawResource(SampleBook.getSampleImageId(imageName))
            val out = FileOutputStream(file)
            val buff = ByteArray(1024)
            var read = 0
            try {
                while (input.read(buff).also { read = it } > 0) {
                    out.write(buff, 0, read)
                }
            } finally {
                input.close()
                out.close()
            }
        }

        val coverName = "image_1.gif"
        val coverImage = File(rootPath, fileCover(sampleId, coverName))
        val input: InputStream = context.resources.openRawResource(SampleBook.getSampleImageId(coverName))
        val out = FileOutputStream(coverImage)
        val buff = ByteArray(1024)
        var read = 0
        try {
            while (input.read(buff).also { read = it } > 0) {
                out.write(buff, 0, read)
            }
        } finally {
            input.close()
            out.close()
        }
    }

}
package com.fancymansion.data.datasource.appStorage.book

import android.content.Context
import android.net.Uri
import com.fancymansion.core.common.const.EpisodeRef
import com.fancymansion.core.common.const.ReadMode
import com.fancymansion.core.common.const.getBookId
import com.fancymansion.core.common.const.getCoverFileName
import com.fancymansion.core.common.const.getEpisodeId
import com.fancymansion.core.common.const.pageStartWith
import com.fancymansion.core.common.const.sampleUserId
import com.fancymansion.data.R
import com.fancymansion.data.datasource.appStorage.book.model.BookInfoData
import com.fancymansion.data.datasource.appStorage.book.model.EditorData
import com.fancymansion.data.datasource.appStorage.book.model.EpisodeInfoData
import com.fancymansion.data.datasource.appStorage.book.model.IntroduceData
import com.fancymansion.data.datasource.appStorage.book.model.KeywordData
import com.fancymansion.data.datasource.appStorage.book.model.LogicData
import com.fancymansion.data.datasource.appStorage.book.model.PageData
import com.fancymansion.data.datasource.appStorage.book.model.SourceData
import com.fancymansion.data.datasource.appStorage.book.model.asData
import com.fancymansion.data.datasource.appStorage.book.sample.SAMPLE_IMAGE_LIST
import com.fancymansion.domain.model.book.KeywordModel
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
    override suspend fun getUserBookFolderNameList(
        userId: String,
        mode: ReadMode
    ) : List<String> {
        return File(root, BookPath.modePath(userId, mode)).listFiles()
            ?.filter { it.isDirectory }
            ?.map { it.name }
            ?: emptyList()
    }

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

    override suspend fun deleteLogic(episodeRef: EpisodeRef): Boolean =
        root.logicFile(episodeRef).delete()

    override suspend fun makePage(episodeRef: EpisodeRef, pageId: Long, page: PageData): Boolean =
        root.pageFile(episodeRef, pageId).writeJson(page)

    override suspend fun loadPage(episodeRef: EpisodeRef, pageId: Long): PageData =
        root.pageFile(episodeRef, pageId).readJson(PageData::class.java) as PageData

    override suspend fun deletePage(episodeRef: EpisodeRef, pageId: Long): Boolean =
        root.pageFile(episodeRef, pageId).delete()

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

    override suspend fun bookLogicFileExists(
        episodeRef: EpisodeRef
    ) : Boolean = root.logicFile(episodeRef).exists()

    override suspend fun makeSampleEpisode(episodeRef: EpisodeRef): Boolean {
        if(root.mediaFile(episodeRef).exists()){
            return true
        }

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
        makeCoverImageFromResource(
            episodeRef.userId,
            episodeRef.mode,
            episodeRef.bookId,
            getCoverFileName(episodeRef.bookId, 0, "png"),
            R.drawable.sample_1_1
        )
        SAMPLE_IMAGE_LIST.forEach {
            makePageImageFromResource(episodeRef, it.first, it.second)
        }

        val bookInfo: BookInfoData = gson.fromJson(
            context.assets.open("base_book_info.json").bufferedReader().use { it.readText() },
            BookInfoData::class.java
        )

        val episodeInfo: EpisodeInfoData = gson.fromJson(
            context.assets.open("base_episode_info.json").bufferedReader().use { it.readText() },
            EpisodeInfoData::class.java
        )

        val logic: LogicData = gson.fromJson(
            context.assets.open("base_logic.json").bufferedReader().use { it.readText() },
            LogicData::class.java
        )

        val pages: List<PageData> = (1..7).map { pageNumber ->
            val fileName = "base_page_$pageNumber.json"
            gson.fromJson(
                context.assets.open(fileName).bufferedReader().use { it.readText() },
                PageData::class.java
            )
        }

        /**
         * Make Edit Book Samples
         */
        makeEditBookInfoSampleList(episodeRef.userId)

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

    override suspend fun getPageImageFiles(episodeRef: EpisodeRef, pageId: Long) : List<File> {
        return root.mediaFile(episodeRef).listFiles()?.asList()?.filter { it.name.startsWith("$pageStartWith$pageId") }?:listOf()
    }

    override suspend fun updateEditTime(episodeRef: EpisodeRef): Boolean {
        val newEpisode = loadEpisodeInfo(episodeRef).copy(
            editTime = System.currentTimeMillis()
        )
        return makeEpisodeInfo(episodeRef, newEpisode)
    }

    override suspend fun updatePageCount(episodeRef: EpisodeRef, pageCount: Int): Boolean {
        val newEpisode = loadEpisodeInfo(episodeRef).copy(
            pageCount = pageCount
        )
        return makeEpisodeInfo(episodeRef, newEpisode)
    }

    private suspend fun makeEditBookInfoSampleList(userId: String) {
        val titles = listOf(
            "사랑의 법칙", "시간의 파편", "유령 도시의 연대기", "끝나지 않는 이야기", "비밀의 정원 이야기",
            "별이 머문 자리", "무지개의 심장 이야기", "새벽의 노래 이야기", "달빛 그림자 이야기", "하늘을 걷는 아이",
            "잃어버린 계절 이야기", "고양이의 꿈", "기억의 조각", "폭풍 속으로", "미로 속의 너",
            "어느 날의 약속 이야기", "은하수를 따라", "눈물의 연대기", "코끼리의 시간", "거울 저편의 세계 이야기",
            "우주 너머의 메아리", "비 오는 날의 연주", "잊혀진 약속", "파란 하늘의 아이", "시간 도둑",
            "달려라, 토끼!", "섬광 속의 너", "조용한 혁명", "그림자 속에서", "하루의 끝"
        )
        val keywords = listOf(
            KeywordModel(id = 1001, category = "장르", name = "판타지"),
            KeywordModel(id = 1002, category = "장르", name = "로맨스"),
            KeywordModel(id = 1003, category = "장르", name = "모험"),
            KeywordModel(id = 1004, category = "장르", name = "드라마"),
            KeywordModel(id = 1005, category = "장르", name = "스릴러"),
            KeywordModel(id = 1006, category = "장르", name = "공포"),
            KeywordModel(id = 1007, category = "장르", name = "SF"),
            KeywordModel(id = 1008, category = "장르", name = "미스터리"),
            KeywordModel(id = 1009, category = "장르", name = "코미디"),
            KeywordModel(id = 1010, category = "장르", name = "역사"),

            KeywordModel(id = 2001, category = "주제", name = "우정"),
            KeywordModel(id = 2002, category = "주제", name = "자기 발견"),
            KeywordModel(id = 2003, category = "주제", name = "용기"),
            KeywordModel(id = 2004, category = "주제", name = "생물 다양성"),
            KeywordModel(id = 2005, category = "주제", name = "성장 이야기"),
            KeywordModel(id = 2006, category = "주제", name = "상상력"),
            KeywordModel(id = 2007, category = "주제", name = "공동체"),
            KeywordModel(id = 2008, category = "주제", name = "도전"),
            KeywordModel(id = 2009, category = "주제", name = "희생"),
            KeywordModel(id = 2010, category = "주제", name = "사랑"),
            KeywordModel(id = 2011, category = "주제", name = "배신"),
            KeywordModel(id = 2012, category = "주제", name = "복수"),
            KeywordModel(id = 2013, category = "주제", name = "자유"),
            KeywordModel(id = 2014, category = "주제", name = "권력"),
            KeywordModel(id = 2015, category = "주제", name = "희망"),
            KeywordModel(id = 2016, category = "주제", name = "절망"),

            KeywordModel(id = 3001, category = "스타일", name = "리얼리즘"),
            KeywordModel(id = 3002, category = "스타일", name = "서정적"),
            KeywordModel(id = 3003, category = "스타일", name = "풍자적"),
            KeywordModel(id = 3004, category = "스타일", name = "몽환적"),
            KeywordModel(id = 3005, category = "스타일", name = "역설적"),
            KeywordModel(id = 3006, category = "스타일", name = "비극적"),
            KeywordModel(id = 3007, category = "스타일", name = "희극적"),
            KeywordModel(id = 3008, category = "스타일", name = "서사적"),

            KeywordModel(id = 4001, category = "배경", name = "중세"),
            KeywordModel(id = 4002, category = "배경", name = "현대"),
            KeywordModel(id = 4003, category = "배경", name = "근미래"),
            KeywordModel(id = 4004, category = "배경", name = "고대"),
            KeywordModel(id = 4005, category = "배경", name = "외계"),
            KeywordModel(id = 4006, category = "배경", name = "가상세계"),
            KeywordModel(id = 4007, category = "배경", name = "디스토피아"),
            KeywordModel(id = 4008, category = "배경", name = "유토피아")
        ).map { it.asData() }

        for (i in 0 until 30) {
            val bookId = getBookId(sampleUserId, ReadMode.EDIT, i)
            val episodeId = getEpisodeId(sampleUserId, ReadMode.EDIT, i)

            val episodeRef = EpisodeRef(
                userId = userId,
                mode = ReadMode.EDIT,
                bookId = bookId,
                episodeId = episodeId
            )

            makeBookDir(userId, ReadMode.EDIT, bookId)
            makeEpisodeDir(episodeRef)

            val resourceFileName = "test_cover_$i.jpg"
            val coverFileName = getCoverFileName(bookId, 0, "jpg")
            // 실제 존재하는 drawable 리소스를 재사용하거나 범위 제한 필요
            val coverResId = context.resources.getIdentifier(resourceFileName.substringBeforeLast("."), "drawable", context.packageName)
            makeCoverImageFromResource(userId, ReadMode.EDIT, bookId, coverFileName, coverResId)


            val bookInfo = BookInfoData(
                id = bookId,
                introduce = IntroduceData(
                    title = titles[i % titles.size],
                    coverList = listOf(coverFileName),
                    keywordList = keywords.shuffled().take((1..minOf(6, keywords.size)).random()).sortedBy { it.id },
                    description = "테스터 $i 가 만든 ${titles[i % titles.size]} 책입니다."
                ),
                editor = EditorData(
                    editorId = "editor_$i",
                    editorName = "테스터 $i",
                    editorEmail = "tester$i@example.com"
                )
            )

            val now = System.currentTimeMillis()
            val daysAgo = (0..365).random()
            val createTime = now - daysAgo * 24 * 60 * 60 * 1000L
            val editTime = createTime + (1..5).random() * 60 * 60 * 1000L // 1~5시간 후에 수정했다고 가정

            val episodeInfo = EpisodeInfoData(
                bookId = bookId,
                createTime = editTime,
                editTime = editTime,
                id = episodeId,
                readMode = ReadMode.EDIT,
                title = "",
                pageCount = 0,
                version = 0
            )

            val logic = LogicData(
                id = 1,
                logics = listOf()
            )

            makeBookInfo(userId, ReadMode.EDIT, bookId, bookInfo)
            makeEpisodeInfo(episodeRef, episodeInfo)
            makeLogic(episodeRef, logic)
        }
    }
}
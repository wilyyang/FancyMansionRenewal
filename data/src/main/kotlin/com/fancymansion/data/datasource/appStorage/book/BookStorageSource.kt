package com.fancymansion.data.datasource.appStorage.book

import android.net.Uri
import com.fancymansion.core.common.const.EpisodeRef
import com.fancymansion.core.common.const.ReadMode
import com.fancymansion.data.datasource.appStorage.book.model.BookInfoData
import com.fancymansion.data.datasource.appStorage.book.model.EpisodeInfoData
import com.fancymansion.data.datasource.appStorage.book.model.LogicData
import com.fancymansion.data.datasource.appStorage.book.model.PageData
import java.io.File

interface BookStorageSource {
    /**
     * Dir
     */
    suspend fun makeUserDir(userId: String)

    suspend fun deleteUserDir(userId: String)

    suspend fun makeBookDir(userId: String, mode: ReadMode, bookId: String)

    suspend fun deleteBookDir(userId: String, mode: ReadMode, bookId: String)

    suspend fun makeEpisodeDir(episodeRef: EpisodeRef)

    suspend fun deleteEpisodeDir(episodeRef: EpisodeRef)

    /**
     * File
     */
    suspend fun makeBookInfo(
        userId: String,
        mode: ReadMode,
        bookId: String,
        bookInfo: BookInfoData
    ): Boolean

    suspend fun loadBookInfo(
        userId: String,
        mode: ReadMode,
        bookId: String
    ): BookInfoData

    suspend fun makeEpisodeInfo(
        episodeRef: EpisodeRef,
        episodeInfo: EpisodeInfoData
    ): Boolean

    suspend fun loadEpisodeInfo(episodeRef: EpisodeRef): EpisodeInfoData

    suspend fun makeLogic(episodeRef: EpisodeRef, logic: LogicData): Boolean

    suspend fun loadLogic(episodeRef: EpisodeRef): LogicData

    suspend fun makePage(episodeRef: EpisodeRef, pageId: Long, page: PageData): Boolean

    suspend fun loadPage(episodeRef: EpisodeRef, pageId: Long): PageData

    suspend fun deletePage(episodeRef: EpisodeRef, pageId: Long): Boolean

    suspend fun loadPageImage(episodeRef: EpisodeRef, imageName: String) : File

    suspend fun loadEpisodeThumbnail(episodeRef: EpisodeRef, imageName: String): File

    suspend fun loadCoverImage(userId: String, mode: ReadMode, bookId: String, imageName: String) : File

    suspend fun deletePageImage(episodeRef: EpisodeRef, imageName: String): Boolean

    suspend fun deleteCoverImage(
        userId: String,
        mode: ReadMode,
        bookId: String,
        imageName: String
    ): Boolean

    suspend fun makePageImageFromUri(episodeRef: EpisodeRef, imageName: String, uri: Uri): Boolean

    suspend fun makeCoverImageFromUri(
        userId: String,
        mode: ReadMode,
        bookId: String,
        imageName: String,
        uri: Uri
    ): Boolean

    suspend fun makePageImageFromResource(episodeRef: EpisodeRef, imageName: String, resourceId: Int)

    suspend fun makeEpisodeThumbnailFromResource(
        episodeRef: EpisodeRef,
        imageName: String,
        resourceId: Int
    )

    suspend fun makeCoverImageFromResource(
        userId: String,
        mode: ReadMode,
        bookId: String,
        imageName: String,
        resourceId: Int
    )

    suspend fun makeSampleEpisode(episodeRef: EpisodeRef) : Boolean
}
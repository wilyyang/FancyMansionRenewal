package com.fancymansion.domain.interfaceRepository

import android.net.Uri
import com.fancymansion.core.common.const.EpisodeRef
import com.fancymansion.core.common.const.ReadMode
import com.fancymansion.domain.model.book.ActionIdModel
import com.fancymansion.domain.model.book.BookInfoModel
import com.fancymansion.domain.model.book.EpisodeInfoModel
import com.fancymansion.domain.model.book.LogicModel
import com.fancymansion.domain.model.book.PageModel
import com.fancymansion.domain.model.book.PageSettingModel
import kotlinx.coroutines.flow.Flow
import java.io.File

interface BookLocalRepository {
    /**
     * PageSetting
     */
    suspend fun getPageSetting(userId: String, mode: String, bookId: String): PageSettingModel?
    fun getPageSettingFlow(userId: String, mode: String, bookId: String): Flow<PageSettingModel?>
    suspend fun insertPageSetting(userId: String, mode: String, bookId: String, pageSetting: PageSettingModel)
    suspend fun updatePageSetting(userId: String, mode: String, bookId: String, pageSetting: PageSettingModel)
    suspend fun deletePageSettingByBookId(userId: String, mode: String, bookId: String)


    /**
     * App Storage : Directory
     */
    suspend fun makeUserDir(userId: String)
    suspend fun deleteUserDir(userId: String)
    suspend fun makeBookDir(userId: String, mode : ReadMode, bookId : String)
    suspend fun deleteBookDir(userId: String, mode : ReadMode, bookId : String)
    suspend fun makeEpisodeDir(episodeRef: EpisodeRef)
    suspend fun deleteEpisodeDir(episodeRef: EpisodeRef)

    /**
     * App Storage : File
     */
    suspend fun makeBookInfo(userId: String, mode : ReadMode, bookId : String, bookInfo: BookInfoModel): Boolean
    suspend fun loadBookInfo(userId: String, mode : ReadMode, bookId : String): BookInfoModel
    suspend fun makeEpisodeInfo(episodeRef: EpisodeRef, episodeInfo: EpisodeInfoModel): Boolean
    suspend fun loadEpisodeInfo(episodeRef: EpisodeRef): EpisodeInfoModel

    suspend fun makeLogic(episodeRef: EpisodeRef, logic: LogicModel): Boolean
    suspend fun loadLogic(episodeRef: EpisodeRef): LogicModel
    suspend fun makePage(episodeRef: EpisodeRef, pageId: Long, page: PageModel): Boolean
    suspend fun loadPage(episodeRef: EpisodeRef, pageId: Long): PageModel
    suspend fun deletePage(episodeRef: EpisodeRef, pageId: Long): Boolean
    suspend fun loadPageImage(episodeRef: EpisodeRef, imageName: String) : File
    suspend fun loadEpisodeThumbnail(episodeRef: EpisodeRef, imageName: String) : File
    suspend fun loadCoverImage(userId: String, mode : ReadMode, bookId : String, imageName: String) : File


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

    suspend fun makePageImageFromResource(
        episodeRef: EpisodeRef,
        imageName: String,
        resourceId: Int
    )

    suspend fun makeEpisodeThumbnailFromResource(
        episodeRef: EpisodeRef,
        imageName: String,
        resourceId: Int
    )

    suspend fun makeCoverImageFromResource(
        userId: String,
        mode : ReadMode,
        bookId : String,
        imageName: String,
        resourceId: Int
    )
    suspend fun makeSampleEpisode(episodeRef: EpisodeRef): Boolean

    suspend fun getPageImageFiles(episodeRef: EpisodeRef, pageId: Long): List<File>

    /**
     * ActionCount
     */
    suspend fun deleteActionCountByEpisode(episodeRef: EpisodeRef)
    suspend fun updateActionCount(episodeRef: EpisodeRef, actionId: ActionIdModel, newCount : Int)
    suspend fun insertActionCount(episodeRef: EpisodeRef, actionId: ActionIdModel)
    suspend fun getActionCount(episodeRef: EpisodeRef, actionId: ActionIdModel) : Int?

    /**
     * ReadingProgress
     */
    suspend fun deleteReadingProgressByEpisode(episodeRef: EpisodeRef)
    suspend fun updateReadingProgressPageId(episodeRef: EpisodeRef, newPageId: Long)
    suspend fun insertReadingProgress(episodeRef: EpisodeRef, pageId: Long)
    suspend fun getReadingProgressPageId(episodeRef: EpisodeRef): Long?
}
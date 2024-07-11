package com.fancymansion.domain.interfaceRepository

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
    suspend fun getEpisodePageSetting(episodeRef: EpisodeRef): PageSettingModel?
    fun getEpisodePageSettingFlow(episodeRef: EpisodeRef): Flow<PageSettingModel>
    suspend fun saveEpisodePageSetting(episodeRef: EpisodeRef, pageSetting: PageSettingModel)
    suspend fun deleteEpisodePageSetting(episodeRef: EpisodeRef)

    suspend fun makeUserDir(userId: String)
    suspend fun deleteUserDir(userId: String)
    suspend fun makeBookDir(userId: String, mode : ReadMode, bookId : String)
    suspend fun deleteBookDir(userId: String, mode : ReadMode, bookId : String)
    suspend fun makeEpisodeDir(episodeRef: EpisodeRef)
    suspend fun deleteEpisodeDir(episodeRef: EpisodeRef)

    suspend fun makeBookInfo(userId: String, mode : ReadMode, bookId : String, bookInfo: BookInfoModel): Boolean
    suspend fun loadBookInfo(userId: String, mode : ReadMode, bookId : String): BookInfoModel
    suspend fun makeEpisodeInfo(episodeRef: EpisodeRef, episodeInfo: EpisodeInfoModel): Boolean
    suspend fun loadEpisodeInfo(episodeRef: EpisodeRef): EpisodeInfoModel

    suspend fun makeLogic(episodeRef: EpisodeRef, logic: LogicModel): Boolean
    suspend fun loadLogic(episodeRef: EpisodeRef): LogicModel
    suspend fun makePage(episodeRef: EpisodeRef, pageId: Long, page: PageModel): Boolean
    suspend fun loadPage(episodeRef: EpisodeRef, pageId: Long): PageModel
    suspend fun loadPageImage(episodeRef: EpisodeRef, imageName: String) : File
    suspend fun loadEpisodeThumbnail(episodeRef: EpisodeRef, imageName: String) : File
    suspend fun loadCoverImage(userId: String, mode : ReadMode, bookId : String, imageName: String) : File

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

    suspend fun deleteActionCountByEpisode(episodeRef: EpisodeRef)
    suspend fun updateActionCount(episodeRef: EpisodeRef, actionId: ActionIdModel, newCount : Int)
    suspend fun insertActionCount(episodeRef: EpisodeRef, actionId: ActionIdModel)
    suspend fun getActionCount(episodeRef: EpisodeRef, actionId: ActionIdModel) : Int?

    suspend fun deleteReadingProgressByEpisode(episodeRef: EpisodeRef)
    suspend fun getReadingProgressPageId(episodeRef: EpisodeRef): Long?
    suspend fun insertReadingProgress(episodeRef: EpisodeRef, pageId: Long)
    suspend fun updateReadingProgressPageId(episodeRef: EpisodeRef, newPageId: Long)
}
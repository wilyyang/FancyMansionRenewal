package com.fancymansion.data.datasource.database.book.dao

import androidx.room.*
import com.fancymansion.data.datasource.database.book.model.PageSettingData
import kotlinx.coroutines.flow.Flow

@Dao
interface BookDatabaseDao {

    /**
     * PageSetting
     */
    @Query("DELETE FROM PageSettingData WHERE userId = :userId AND mode = :mode AND bookId = :bookId")
    suspend fun deletePageSettingByBookId(userId: String, mode: String, bookId: String)

    @Query("DELETE FROM PageSettingData WHERE userId = :userId")
    suspend fun deletePageSettingByUserId(userId: String)

    @Query("SELECT * FROM PageSettingData WHERE userId = :userId AND mode = :mode AND bookId = :bookId")
    suspend fun getPageSetting(userId: String, mode: String, bookId: String): PageSettingData?

    @Query("SELECT * FROM PageSettingData WHERE userId = :userId AND mode = :mode AND bookId = :bookId")
    fun getPageSettingFlow(userId: String, mode: String, bookId: String): Flow<PageSettingData?>

    @Insert
    suspend fun insertPageSetting(pageSettingData: PageSettingData)

    @Update
    suspend fun updatePageSetting(pageSettingData: PageSettingData)


    /**
     * ActionCount
     */
    @Query("DELETE FROM ActionCountData WHERE userId = :userId AND mode = :mode AND bookId = :bookId AND episodeId = :episodeId")
    suspend fun deleteActionCountByEpisode(userId: String, mode: String, bookId: String, episodeId: String)

    @Query("DELETE FROM ActionCountData WHERE userId = :userId")
    suspend fun deleteActionCountByUserId(userId: String)

    @Query("SELECT count FROM ActionCountData WHERE userId = :userId AND mode = :mode AND bookId = :bookId AND episodeId = :episodeId AND pageId = :pageId AND selectorId = :selectorId AND routeId = :routeId")
    suspend fun getActionCount(userId: String, mode: String, bookId: String, episodeId: String, pageId: Long, selectorId: Long, routeId: Long): Int?

    @Query("INSERT OR REPLACE INTO ActionCountData (userId, mode, bookId, episodeId, pageId, selectorId, routeId, count) VALUES (:userId, :mode, :bookId, :episodeId, :pageId, :selectorId, :routeId, 1)")
    suspend fun insertActionCount(userId: String, mode: String, bookId: String, episodeId: String, pageId: Long, selectorId: Long, routeId: Long)

    @Query("UPDATE ActionCountData SET count = :newCount WHERE userId = :userId AND mode = :mode AND bookId = :bookId AND episodeId = :episodeId AND pageId = :pageId AND selectorId = :selectorId AND routeId = :routeId")
    suspend fun updateActionCount(userId: String, mode: String, bookId: String, episodeId: String, pageId: Long, selectorId: Long, routeId: Long, newCount: Int)


    /**
     * ReadingProgress
     */
    @Query("DELETE FROM ReadingProgress WHERE userId = :userId AND mode = :mode AND bookId = :bookId AND episodeId = :episodeId")
    suspend fun deleteReadingProgressByEpisode(userId: String, mode: String, bookId: String, episodeId: String)

    @Query("DELETE FROM ReadingProgress WHERE userId = :userId")
    suspend fun deleteReadingProgressByUserId(userId: String)

    @Query("SELECT pageId FROM ReadingProgress WHERE userId = :userId AND mode = :mode AND bookId = :bookId AND episodeId = :episodeId")
    suspend fun getReadingProgressPageId(userId: String, mode: String, bookId: String, episodeId: String): Long?

    @Query("INSERT OR REPLACE INTO ReadingProgress (userId, mode, bookId, episodeId, pageId) VALUES (:userId, :mode, :bookId, :episodeId, :pageId)")
    suspend fun insertReadingProgress(userId: String, mode: String, bookId: String, episodeId: String, pageId: Long)

    @Query("UPDATE ReadingProgress SET pageId = :newPageId WHERE userId = :userId AND mode = :mode AND bookId = :bookId AND episodeId = :episodeId")
    suspend fun updateReadingProgressPageId(userId: String, mode: String, bookId: String, episodeId: String, newPageId: Long)
}
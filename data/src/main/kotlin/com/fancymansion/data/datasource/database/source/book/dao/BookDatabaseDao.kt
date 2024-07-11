package com.fancymansion.data.datasource.database.source.book.dao

import androidx.room.*

@Dao
interface BookDatabaseDao {

    /**
     * ActionCount
     */
    @Query("DELETE FROM ActionCountData WHERE userId = :userId AND mode = :mode AND bookId = :bookId AND episodeId = :episodeId")
    suspend fun deleteActionCountByEpisode(userId: String, mode: String, bookId: String, episodeId: String)

    @Query("DELETE FROM ActionCountData WHERE userId = :userId")
    suspend fun deleteActionCountByUserId(userId: String)

    @Query("SELECT count FROM ActionCountData WHERE userId = :userId AND mode = :mode AND bookId = :bookId AND episodeId = :episodeId AND actionId = :actionId")
    suspend fun getActionCount(userId: String, mode: String, bookId: String, episodeId: String, actionId: Long): Int?

    @Query("INSERT OR REPLACE INTO ActionCountData (userId, mode, bookId, episodeId, actionId, count) VALUES (:userId, :mode, :bookId, :episodeId, :actionId, 1)")
    suspend fun insertActionCount(userId: String, mode: String, bookId: String, episodeId: String, actionId: Long)

    @Query("UPDATE ActionCountData SET count = :newCount WHERE userId = :userId AND mode = :mode AND bookId = :bookId AND episodeId = :episodeId AND actionId = :actionId")
    suspend fun updateActionCount(userId: String, mode: String, bookId: String, episodeId: String, actionId: Long, newCount: Int)


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
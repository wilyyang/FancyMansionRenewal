package com.fancymansion.data.datasource.database.source.book.dao

import androidx.room.*

@Dao
interface BookDatabaseDao {

    /**
     * ActionCount
     */
    @Query("DELETE FROM ActionCountData WHERE userId = :userId AND mode = :mode AND bookId = :bookId")
    suspend fun deleteActionCountByBook(userId: String, mode: String, bookId: String)

    @Query("DELETE FROM ActionCountData WHERE userId = :userId")
    suspend fun deleteActionCountByUserId(userId: String)

    @Query("SELECT count FROM ActionCountData WHERE userId = :userId AND mode = :mode AND bookId = :bookId AND actionId = :actionId")
    suspend fun getActionCount(userId: String, mode: String, bookId: String, actionId: Long): Int?

    @Query("INSERT OR REPLACE INTO ActionCountData (userId, mode, bookId, actionId, count) VALUES (:userId, :mode, :bookId, :actionId, 1)")
    suspend fun insertActionCount(userId: String, mode: String, bookId: String, actionId: Long)

    @Query("UPDATE ActionCountData SET count = :newCount WHERE userId = :userId AND mode = :mode AND bookId = :bookId AND actionId = :actionId")
    suspend fun updateActionCount(userId: String, mode: String, bookId: String, actionId: Long, newCount: Int)


    /**
     * ReadingProgress
     */
    @Query("DELETE FROM ReadingProgress WHERE userId = :userId AND mode = :mode AND bookId = :bookId")
    suspend fun deleteReadingProgressByBook(userId: String, mode: String, bookId: String)

    @Query("DELETE FROM ReadingProgress WHERE userId = :userId")
    suspend fun deleteReadingProgressByUserId(userId: String)

    @Query("SELECT pageId FROM ReadingProgress WHERE userId = :userId AND mode = :mode AND bookId = :bookId")
    suspend fun getReadingProgressPageId(userId: String, mode: String, bookId: String): Long?

    @Query("INSERT OR REPLACE INTO ReadingProgress (userId, mode, bookId, pageId) VALUES (:userId, :mode, :bookId, :pageId)")
    suspend fun insertReadingProgress(userId: String, mode: String, bookId: String, pageId: Long)

    @Query("UPDATE ReadingProgress SET pageId = :newPageId WHERE userId = :userId AND mode = :mode AND bookId = :bookId")
    suspend fun updateReadingProgressPageId(userId: String, mode: String, bookId: String, newPageId: Long)
}
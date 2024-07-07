package com.fancymansion.data.datasource.database.source.book.dao

import androidx.room.*

@Dao
interface BookDatabaseDao {

    @Query("DELETE FROM ActionCountData WHERE userId = :userId AND mode = :mode AND bookId = :bookId")
    suspend fun deleteActionCountByBook(userId: String, mode: String, bookId: String)

    @Query("DELETE FROM ActionCountData WHERE userId = :userId")
    suspend fun deleteActionCountByUserId(userId: String)

    @Query("SELECT count FROM ActionCountData WHERE userId = :userId AND mode = :mode AND bookId = :bookId AND actionId = :actionId")
    suspend fun getActionCount(userId: String, mode: String, bookId: String, actionId: String): Int?

    @Query("INSERT OR REPLACE INTO ActionCountData (userId, mode, bookId, actionId, count) VALUES (:userId, :mode, :bookId, :actionId, 1)")
    suspend fun insertActionCount(userId: String, mode: String, bookId: String, actionId: String)

    @Query("UPDATE ActionCountData SET count = :newCount WHERE userId = :userId AND mode = :mode AND bookId = :bookId AND actionId = :actionId")
    suspend fun updateActionCount(userId: String, mode: String, bookId: String, actionId: String, newCount: Int)
}
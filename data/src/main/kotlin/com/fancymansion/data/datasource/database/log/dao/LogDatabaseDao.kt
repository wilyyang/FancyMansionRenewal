package com.fancymansion.data.datasource.database.log.dao

import androidx.room.*
import com.fancymansion.data.datasource.database.log.model.LogData

@Dao
interface LogDatabaseDao {

    @Query("SELECT * FROM LogData")
    suspend fun getLogAll() : List<LogData>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLog(log: LogData)

    @Query("DELETE FROM LogData WHERE idx < (SELECT idx FROM LogData ORDER BY idx DESC LIMIT 1 OFFSET 30000);")
    suspend fun deleteLog()

    @Query("DELETE FROM LogData")
    suspend fun deleteLogAll()
}
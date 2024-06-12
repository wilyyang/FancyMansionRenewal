package com.fancymansion.data.datasource.database.base

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.fancymansion.data.datasource.database.source.log.dao.LogDatabaseDao
import com.fancymansion.data.datasource.database.source.log.model.LogData
import kotlinx.coroutines.CoroutineScope

@Database(
    version = 1,
    exportSchema = true,
    entities = [
        LogData::class
    ]
)
abstract class LogDatabaseHelper : RoomDatabase() {
    abstract fun logDatabaseDao() : LogDatabaseDao

    companion object {

        @Volatile
        private var instance : LogDatabaseHelper? = null

        fun getDataBase(contextApplication : Context, scope : CoroutineScope) : LogDatabaseHelper {
            return instance ?: synchronized(this) {
                val database = Room.databaseBuilder(contextApplication, LogDatabaseHelper::class.java, "log")
                    .addCallback(CallbackDatabaseLog(scope))
                    .build()
                instance = database
                database
            }
        }

        private class CallbackDatabaseLog(
            private val scope : CoroutineScope,
        ) : Callback() {
            override fun onCreate(db : SupportSQLiteDatabase) {
                super.onCreate(db)
            }
        }
    }
}
package com.fancymansion.data.datasource.database.source.book

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.fancymansion.data.datasource.database.source.book.dao.BookDatabaseDao
import com.fancymansion.data.datasource.database.source.book.model.ActionCountData
import com.fancymansion.data.datasource.database.source.book.model.ReadingProgressData
import kotlinx.coroutines.CoroutineScope


@Database(
    version = 1,
    exportSchema = true,
    entities = [
        ActionCountData::class,
        ReadingProgressData::class
    ]
)
abstract class BookDatabaseHelper : RoomDatabase() {
    abstract fun bookDatabaseDao() : BookDatabaseDao

    companion object {

        @Volatile
        private var instance : BookDatabaseHelper? = null

        fun getDataBase(contextApplication : Context, scope : CoroutineScope) : BookDatabaseHelper {
            return instance ?: synchronized(this) {
                val database = Room.databaseBuilder(contextApplication, BookDatabaseHelper::class.java, "book")
                    .addCallback(CallbackDatabaseBook(scope))
                    .build()
                instance = database
                database
            }
        }

        private class CallbackDatabaseBook(
            private val scope : CoroutineScope,
        ) : Callback() {
            override fun onCreate(db : SupportSQLiteDatabase) {
                super.onCreate(db)
            }
        }
    }
}
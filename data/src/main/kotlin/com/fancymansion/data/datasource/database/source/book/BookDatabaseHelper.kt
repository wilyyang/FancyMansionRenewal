package com.fancymansion.data.datasource.database.source.book

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.fancymansion.core.common.const.ACTION_ID_NOT_ASSIGNED
import com.fancymansion.data.datasource.database.source.book.dao.BookDatabaseDao
import com.fancymansion.data.datasource.database.source.book.model.ActionCountData
import com.fancymansion.data.datasource.database.source.book.model.ActionIdData
import com.fancymansion.data.datasource.database.source.book.model.ReadingProgress
import kotlinx.coroutines.CoroutineScope

class ActionIdDataTypeConverter {

    @TypeConverter
    fun fromActionIdData(actionIdData: ActionIdData): String {
        return "${actionIdData.pageId}_${actionIdData.selectorId}_${actionIdData.routeId}"
    }

    @TypeConverter
    fun toActionIdData(value: String): ActionIdData {
        val parts = value.split("_")
        return ActionIdData(
            pageId = parts[0].toLongOrNull() ?: ACTION_ID_NOT_ASSIGNED,
            selectorId = parts[1].toLongOrNull() ?: ACTION_ID_NOT_ASSIGNED,
            routeId = parts[2].toLongOrNull() ?: ACTION_ID_NOT_ASSIGNED
        )
    }
}

@Database(
    version = 1,
    exportSchema = true,
    entities = [
        ActionCountData::class,
        ActionIdData::class,
        ReadingProgress::class
    ]
)
@TypeConverters(ActionIdDataTypeConverter::class)
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
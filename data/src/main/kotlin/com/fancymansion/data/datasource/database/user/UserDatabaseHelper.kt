package com.fancymansion.data.datasource.database.user

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.fancymansion.data.datasource.database.user.dao.UserDatabaseDao
import com.fancymansion.data.datasource.database.user.model.UserInfoLocalData
import kotlinx.coroutines.CoroutineScope

@Database(
    version = 1,
    exportSchema = false,
    entities = [
        UserInfoLocalData::class
    ]
)
abstract class UserDatabaseHelper  : RoomDatabase() {
    abstract fun userDatabaseDao() : UserDatabaseDao

    companion object {

        @Volatile
        private var instance : UserDatabaseHelper? = null

        fun getDataBase(contextApplication : Context, scope : CoroutineScope) : UserDatabaseHelper {
            return instance ?: synchronized(this) {
                val database = Room.databaseBuilder(contextApplication, UserDatabaseHelper::class.java, "user")
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
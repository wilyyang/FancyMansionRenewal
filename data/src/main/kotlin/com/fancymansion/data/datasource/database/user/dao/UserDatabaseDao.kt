package com.fancymansion.data.datasource.database.user.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.fancymansion.data.datasource.database.user.model.UserInfoLocalData
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDatabaseDao {

    @Upsert
    suspend fun upsertUserInfo(userInfo: UserInfoLocalData)

    @Query("DELETE FROM UserInfoLocalData")
    suspend fun clearUserInfo()

    @Transaction
    suspend fun replaceUserInfo(userInfo: UserInfoLocalData) {
        clearUserInfo()
        upsertUserInfo(userInfo)
    }

    @Query("SELECT * FROM UserInfoLocalData LIMIT 1")
    suspend fun getUserInfoData(): UserInfoLocalData?

    @Query("SELECT * FROM UserInfoLocalData LIMIT 1")
    fun observeUserInfoData(): Flow<UserInfoLocalData?>
}
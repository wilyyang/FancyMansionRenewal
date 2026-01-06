package com.fancymansion.data.datasource.dataStore.app

interface AppStoreSource {
    suspend fun isFirstLaunch(): Boolean
    suspend fun markLaunched()
}
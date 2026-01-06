package com.fancymansion.domain.interfaceRepository

interface AppRepository {

    suspend fun isFirstLaunch(): Boolean
    suspend fun markLaunched()
}
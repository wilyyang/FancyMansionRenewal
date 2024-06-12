package com.fancymansion.domain.interfaceRepository

import com.fancymansion.domain.model.log.LogModel

interface LogRepository {

    suspend fun getLogAll() : List<LogModel>

    suspend fun insertLog(tag: String, type: Int, message: String)

    suspend fun deleteLog()

    suspend fun deleteLogAll()
}
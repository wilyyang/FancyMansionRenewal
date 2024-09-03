package com.fancymansion.data.repository

import com.fancymansion.data.datasource.database.log.dao.LogDatabaseDao
import com.fancymansion.data.datasource.database.log.model.LogData
import com.fancymansion.data.datasource.database.log.model.asModel
import com.fancymansion.domain.interfaceRepository.LogRepository
import com.fancymansion.domain.model.log.LogModel
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LogRepositoryImpl @Inject constructor(
    private val logDatabaseDao: LogDatabaseDao
) : LogRepository {
    override suspend fun getLogAll(): List<LogModel> {
        return logDatabaseDao.getLogAll().map { it.asModel() }
    }

    override suspend fun insertLog(tag: String, type: Int, message: String) {
        logDatabaseDao.insertLog(LogData(tag = tag, type = type, message = message))
    }

    override suspend fun deleteLog() {
        logDatabaseDao.deleteLog()
    }

    override suspend fun deleteLogAll() {
        logDatabaseDao.deleteLogAll()
    }
}
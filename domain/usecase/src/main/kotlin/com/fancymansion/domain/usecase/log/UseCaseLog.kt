package com.fancymansion.domain.usecase.log

import android.content.Context
import com.fancymansion.core.common.di.DispatcherIO
import com.fancymansion.domain.interfaceRepository.LogRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.BufferedWriter
import java.io.File
import java.io.FileWriter
import javax.inject.Inject

class UseCaseMakeFileFromLog @Inject constructor(
    @param:ApplicationContext private val context : Context,
    private val logRepository: LogRepository
) {
    suspend operator fun invoke() = withContext(Dispatchers.IO) {
        val logs = logRepository.getLogAll()
        val fileName = "fancy_mansion_logs.txt"

        val file = File(context.filesDir, "/$fileName").apply {
            createNewFile()
        }
        BufferedWriter(FileWriter(file)).use { writer ->
            logs.forEach { log ->
                val type = when(log.type){
                    2 -> "V"
                    3 -> "D"
                    4 -> "I"
                    5 -> "W"
                    6 -> "E"
                    7 -> "A"
                    else -> " "
                }
                writer.write("${log.timeSaved}[${type}]${log.tag} : ${log.message}\n")
            }
        }
        file
    }
}

class UseCaseInsertLog @Inject constructor(
    @param:DispatcherIO private val dispatcher : CoroutineDispatcher,
    private val logRepository: LogRepository
) {
    suspend operator fun invoke(tag: String, type : Int, message: String) = withContext(dispatcher) {
        logRepository.insertLog(tag = tag, type = type, message = message)
    }
}


class UseCaseDeleteOldLog @Inject constructor(
    @param:DispatcherIO private val dispatcher : CoroutineDispatcher,
    private val logRepository: LogRepository
) {
    suspend operator fun invoke() = withContext(dispatcher) {
        logRepository.deleteLog()
    }
}
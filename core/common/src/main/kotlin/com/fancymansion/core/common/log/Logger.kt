package com.fancymansion.core.common.log

import android.util.Log
import androidx.annotation.WorkerThread
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

object Logger {
    const val BASIC_TAG_NAME = "FancyMansion"
    private var saveLogHandler : SaveLogHandler? = null
    fun setSaveLogHandler(saveLogHandler : SaveLogHandler){
        Logger.saveLogHandler = saveLogHandler
    }

    /**
     * 로그 호출 코드의 위치를 정확히하기 위해서 일부러 메소드를 중복하여 작성 (수정하지 말 것)
     */
    fun v(message: String) {
        Log.println(Log.VERBOSE, BASIC_TAG_NAME, changeMessage(message))
        saveLogHandler?.saveLogWithLaunch(message = message, type = Log.VERBOSE, tag = BASIC_TAG_NAME)
    }
    fun v(message: String, tag: String) {
        Log.println(Log.VERBOSE, tag, changeMessage(message))
        saveLogHandler?.saveLogWithLaunch(message = message, type = Log.VERBOSE, tag = tag)
    }
    fun d(message: String) {
        Log.println(Log.DEBUG, BASIC_TAG_NAME, changeMessage(message))
        saveLogHandler?.saveLogWithLaunch(message = message, type = Log.DEBUG, tag = BASIC_TAG_NAME)
    }
    fun d(message: String, tag: String) {
        Log.println(Log.DEBUG, tag, changeMessage(message))
        saveLogHandler?.saveLogWithLaunch(message = message, type = Log.DEBUG, tag = tag)
    }
    fun i(message: String) {
        Log.println(Log.INFO, BASIC_TAG_NAME, changeMessage(message))
        saveLogHandler?.saveLogWithLaunch(message = message, type = Log.INFO, tag = BASIC_TAG_NAME)
    }
    fun i(message: String, tag: String) {
        Log.println(Log.INFO, tag, changeMessage(message))
        saveLogHandler?.saveLogWithLaunch(message = message, type = Log.INFO, tag = tag)
    }
    fun w(message: String) {
        Log.println(Log.WARN, BASIC_TAG_NAME, changeMessage(message))
        saveLogHandler?.saveLogWithLaunch(message = message, type = Log.WARN, tag = BASIC_TAG_NAME)
    }
    fun w(message: String, tag: String) {
        Log.println(Log.WARN, tag, changeMessage(message))
        saveLogHandler?.saveLogWithLaunch(message = message, type = Log.WARN, tag = tag)
    }
    fun e(message: String) {
        Log.println(Log.ERROR, BASIC_TAG_NAME, changeMessage(message))
        saveLogHandler?.saveLogWithLaunch(message = message, type = Log.ERROR, tag = BASIC_TAG_NAME)
    }
    fun e(message: String, tag: String) {
        Log.println(Log.ERROR, tag, changeMessage(message))
        saveLogHandler?.saveLogWithLaunch(message = message, type = Log.ERROR, tag = tag)
    }

    fun print(message: String, tag: String, type: Int = Log.INFO) {
        Log.println(type, tag, message)
        saveLogHandler?.saveLogWithLaunch(message = message, type = type, tag = tag)
    }

    fun print(message: String, value: Any, tag: String, type: Int = Log.INFO) {
        val log: String = buildString {
            // 클래스 이름 추가
            append("$message ${value::class.simpleName} : ")

            // 속성 탐색
            value::class.members
                .filterIsInstance<kotlin.reflect.KProperty1<Any, *>>()
                .forEach { property ->
                    // 속성 이름
                    append("${property.name}= ")

                    // 속성 값
                    val propertyValue = try {
                        property.get(value)
                    } catch (e: Exception) {
                        "Error"
                    }

                    when (propertyValue) {
                        // 기본 자료형은 값 출력
                        is Boolean, is String, is Int, is Long, is Double, is Float -> {
                            append(propertyValue)
                        }
                        // 나머지는 클래스 이름 출력
                        is Any -> {
                            append(propertyValue::class.simpleName)
                        }
                        else -> {
                            append("null")
                        }
                    }
                    append(", ")
                }
        }.removeSuffix(", ")

        // 로그 출력 및 로그 저장
        Log.println(type, tag, log)
        saveLogHandler?.saveLogWithLaunch(message = log, type = type, tag = tag)
    }

    private fun changeMessage(message: String) : String {
        val msgBuilder = StringBuilder()
            .append(Thread.currentThread().stackTrace[4].methodName).append("()").append(" : ")
            .append('\t').append(message)
            .append(" (").append(Thread.currentThread().stackTrace[4].fileName).append(":")
            .append(Thread.currentThread().stackTrace[4].lineNumber).append(")");
        return msgBuilder.toString()
    }
}

abstract class SaveLogHandler{
    private val scopeApplication by lazy { CoroutineScope(Dispatchers.IO + SupervisorJob()) }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    fun saveLogWithLaunch(message : String, type: Int, tag : String){
        scopeApplication.launch {
            saveLog(message = message, type = type, tag = tag)
        }
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    protected abstract suspend fun saveLog(message : String, type : Int, tag : String)
}

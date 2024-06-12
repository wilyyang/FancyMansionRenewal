package com.fancymansion.core.common.throwable

import android.util.Log
import com.fancymansion.core.common.log.Logger
import java.lang.Exception

object ThrowableManager {
    private var exceptionReporter : ExceptionReporter? = null
    fun handleError(throwable: Throwable, reportInfo: HashMap<String, String>? = null) {
        Logger.e(message = throwable.stackTraceToString(), tag = Logger.BASIC_TAG_NAME)
        if(throwable is ApiCallException){
            Logger.print(message = "Exception UseCase name : ${throwable.result.id}", tag = Logger.BASIC_TAG_NAME, type = Log.ERROR)
        }

        when(throwable){
            is ScreenDataInitFailException -> {
                exceptionReporter?.sendException(throwable, reportInfo)
            }
        }
    }

    fun sendLogToRemote(message : String, tag : String = Logger.BASIC_TAG_NAME){
        exceptionReporter?.sendLog(message, tag)
    }

    fun setExceptionReporter(reporter : ExceptionReporter){
        exceptionReporter = reporter
    }
}

interface ExceptionReporter{
    fun sendException(exception: Exception, map:Map<String, String>? = null)
    fun sendLog(message : String, tag : String)
}
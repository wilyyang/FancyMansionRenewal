package com.fancymansion.core.common.throwable

import android.util.Log
import com.fancymansion.core.common.log.Logger
import java.lang.Exception

const val EVENT_DEFAULT_TAG = "default_event"
const val EVENT_EXCEPTION_TAG = "exception_event"
const val MESSAGE_KEY = "message"
const val EXCEPTION_KEY = "exception"

object ThrowableManager {
    private var exceptionReporter : ExceptionReporter? = null
    fun setExceptionReporter(reporter : ExceptionReporter){
        exceptionReporter = reporter
    }

    fun handleError(throwable: Throwable, reportInfo: HashMap<String, String>? = null) {
        logException(throwable)
        handleSpecificException(throwable, reportInfo)
    }

    private fun logException(throwable: Throwable) {
        Logger.e(message = throwable.stackTraceToString(), tag = Logger.BASIC_TAG_NAME)
        if (throwable is ApiCallException) {
            Logger.print(
                message = "Exception UseCase name : ${throwable.result.id}",
                tag = Logger.BASIC_TAG_NAME,
                type = Log.ERROR
            )
        }
    }

    private fun handleSpecificException(throwable: Throwable, reportInfo: HashMap<String, String>?) {
        if (throwable is ScreenDataInitFailException) {
            exceptionReporter?.logEventWithParams(tag = EVENT_EXCEPTION_TAG, exception = throwable, map = reportInfo ?: emptyMap())
        }
    }

    fun sendMessageToReporter(map: Map<String, String>, tag : String = EVENT_DEFAULT_TAG){
        exceptionReporter?.logEventWithParams(map = map, tag = tag)
    }

    fun sendMessageToReporter(message : String, tag : String = EVENT_DEFAULT_TAG){
        exceptionReporter?.logEventWithParams(map = mapOf(MESSAGE_KEY to message), tag = tag)
    }
}

interface ExceptionReporter {
    fun logEventWithParams(tag: String = EVENT_DEFAULT_TAG, exception: Exception? = null, map: Map<String, String> = emptyMap())
}
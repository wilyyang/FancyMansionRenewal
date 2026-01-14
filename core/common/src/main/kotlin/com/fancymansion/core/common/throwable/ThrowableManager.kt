package com.fancymansion.core.common.throwable

import com.fancymansion.core.common.log.Logger
import java.lang.Exception

const val EVENT_MESSAGE_TAG = "event_message"
const val EVENT_EXCEPTION_TAG = "event_exception"
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
    }

    private fun handleSpecificException(throwable: Throwable, reportInfo: HashMap<String, String>?) {
    }

    fun sendMessageToReporter(message : String, tag : String = EVENT_MESSAGE_TAG){
        exceptionReporter?.logEventWithParams(map = mapOf(MESSAGE_KEY to message), tag = tag)
    }
}

interface ExceptionReporter {
    fun logEventWithParams(tag: String = EVENT_MESSAGE_TAG, exception: Exception? = null, map: Map<String, String> = emptyMap())
}
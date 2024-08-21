package com.fancymansion.app

import android.app.Application
import android.os.Bundle
import com.fancymansion.core.common.log.Logger
import com.fancymansion.core.common.log.SaveLogHandler
import com.fancymansion.core.common.throwable.EXCEPTION_KEY
import com.fancymansion.core.common.throwable.ExceptionReporter
import com.fancymansion.core.common.throwable.ThrowableManager
import com.fancymansion.domain.usecase.log.UseCaseDeleteOldLog
import com.fancymansion.domain.usecase.log.UseCaseInsertLog
import com.google.firebase.analytics.FirebaseAnalytics
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

fun Map<String, String>.toBundle(): Bundle {
    val bundle = Bundle()
    this.forEach { (key, value) ->
        bundle.putString(key, value)
    }
    return bundle
}

@HiltAndroidApp
class FancyMansionApplication : Application() {
    @Inject
    lateinit var useCaseInsertLog: UseCaseInsertLog

    @Inject
    lateinit var useCaseDeleteOldLog: UseCaseDeleteOldLog

    override fun onCreate() {
        super.onCreate()

        Logger.setSaveLogHandler(saveLogHandler = object : SaveLogHandler() {
            override suspend fun saveLog(message: String, type : Int, tag: String) {
                useCaseInsertLog(message = message, type = type, tag = tag)
            }
        })

        FirebaseAnalytics.getInstance(this).let { analytics ->
            ThrowableManager.setExceptionReporter(object : ExceptionReporter {
                override fun logEventWithParams(
                    tag: String,
                    exception: Exception?,
                    map: Map<String, String>
                ) {
                    val newMap = if(exception != null){
                        map + (EXCEPTION_KEY to exception.javaClass.simpleName)
                    }else{
                        map
                    }
                    analytics.logEvent(tag, newMap.toBundle())
                }
            })
        }

        CoroutineScope(Dispatchers.IO).launch {
            useCaseDeleteOldLog()
        }
    }
}
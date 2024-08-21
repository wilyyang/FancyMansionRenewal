package com.fancymansion.app

import android.app.Application
import android.os.Bundle
import com.fancymansion.core.common.throwable.EXCEPTION_KEY
import com.fancymansion.core.common.throwable.ExceptionReporter
import com.fancymansion.core.common.throwable.ThrowableManager
import com.google.firebase.analytics.FirebaseAnalytics
import dagger.hilt.android.HiltAndroidApp
import java.lang.Exception

fun Map<String, String>.toBundle(): Bundle {
    val bundle = Bundle()
    this.forEach { (key, value) ->
        bundle.putString(key, value)
    }
    return bundle
}

@HiltAndroidApp
class FancyMansionApplication : Application() {
    override fun onCreate() {
        super.onCreate()

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
    }
}
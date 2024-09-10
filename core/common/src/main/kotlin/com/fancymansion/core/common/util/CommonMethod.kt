package com.fancymansion.core.common.util

import android.content.Context
import java.lang.reflect.Type
import com.google.gson.reflect.TypeToken

inline fun <reified T> type(): Type = object : TypeToken<T>() {}.type

fun Context.readModuleRawFile(id: Int): String {
    return resources.openRawResource(id).bufferedReader().use { it.readText() }
}
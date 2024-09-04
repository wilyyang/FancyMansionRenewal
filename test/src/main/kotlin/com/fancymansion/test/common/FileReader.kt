package com.fancymansion.test.common

import android.content.Context

fun Context.readJson(fileName: String): String {
    val resourceName = if (!fileName.endsWith(".json")) "$fileName.json" else fileName
    return assets.open(resourceName).bufferedReader().use { it.readText() }
}

fun Context.readRaw(id: Int): String {
    return resources.openRawResource(id).bufferedReader().use { it.readText() }
}
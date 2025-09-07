package com.fancymansion.core.common.util

import android.content.Context
import android.net.Uri
import android.webkit.MimeTypeMap

fun Context.getFileExtension(uri: Uri): String? {
    val mimeType = contentResolver.getType(uri) ?: return null
    return MimeTypeMap.getSingleton().getExtensionFromMimeType(mimeType)
}

fun String.ellipsis(maxLength: Int): String {
    return if (this.length > maxLength) {
        this.take(maxLength) + "..."
    } else {
        this
    }
}
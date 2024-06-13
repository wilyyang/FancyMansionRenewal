package com.fancymansion.core.common.resource

import android.content.Context
import androidx.annotation.StringRes
sealed class StringValue {

    data object Empty : StringValue()
    data class DynamicString(val value: String) : StringValue()
    class StringResource(
        @StringRes val resId: Int,
        vararg val args: Any
    ) : StringValue()

    fun asString(context: Context?): String {
        return when (this) {
            is Empty -> ""
            is DynamicString -> value
            is StringResource -> context?.getString(resId, *args).orEmpty()
        }
    }
}
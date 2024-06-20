package com.fancymansion.core.common.resource

import android.content.Context
import androidx.annotation.StringRes
sealed class StringValue {

    data object Empty : StringValue()
    data class StringWrapper(val value: String) : StringValue()
    data class StringResource(
        @StringRes val resId: Int,
        val args: List<Any> = emptyList()
    ) : StringValue()

    fun asString(context: Context? = null): String {
        return when (this) {
            is Empty -> ""
            is StringWrapper -> value
            is StringResource -> {
                context!!.getString(resId, *args.toTypedArray())
            }
        }
    }
}
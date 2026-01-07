package com.fancymansion.core.common.resource

import android.content.Context
import androidx.annotation.StringRes

sealed class StringValue {

    data object Empty : StringValue(){
        override fun toString() = ""
    }

    data class StringWrapper(val value: String) : StringValue(){
        override fun toString() = value
    }

    data class StringResource(
        @param:StringRes val resId: Int,
        val args: List<Any> = emptyList()
    ) : StringValue()

    fun asString(context: Context): String {
        return when (this) {
            is StringResource -> {
                context.getString(resId, *args.toTypedArray())
            }
            else -> this.toString()
        }
    }
}
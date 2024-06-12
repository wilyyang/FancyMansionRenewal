package com.fancymansion.core.common.resource

import android.content.Context

// 년 월 일
fun getDateToStringResourceWithText(context: Context, date:String, splitText:String) : String {
    return try {
        val receivedDateSplit = date.split(splitText)
        val year = receivedDateSplit[0]
        val month = receivedDateSplit[1]
        val day = receivedDateSplit[2]

        context.getString(com.fancymansion.core.common.R.string.date_ymd,
            year.toInt(),month.toInt(),day.toInt())

    } catch (e:IndexOutOfBoundsException) {
        ""
    }
}

// 2024.02.27
fun getDateToStringResourceWithDot(context: Context, date:String, splitText:String) : String {
    return try {
        val receivedDateSplit = date.split(splitText)
        val year = receivedDateSplit[0]
        val month = receivedDateSplit[1]
        val day = receivedDateSplit[2]

        context.getString(com.fancymansion.core.common.R.string.date_dot,
            year.toInt(),month.toInt(),day.toInt())

    } catch (e:IndexOutOfBoundsException) {
        ""
    }
}
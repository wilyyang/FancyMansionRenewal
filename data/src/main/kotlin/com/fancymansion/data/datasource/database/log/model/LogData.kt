package com.fancymansion.data.datasource.database.log.model

import androidx.annotation.Keep
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.fancymansion.domain.model.log.LogModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Entity(tableName = "LogData")
@Keep
data class LogData(
    val tag: String,
    val type: Int,
    val message: String,
    val timeSaved: Long = System.currentTimeMillis()
) {
    @PrimaryKey(autoGenerate = true) var idx: Long = 0
}

fun LogData.asModel() = LogModel(
    tag = tag,
    type = type,
    message = message,
    timeSaved = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date(timeSaved))
)
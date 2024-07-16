package com.fancymansion.data.datasource.database.source.book.model

import androidx.room.Entity
import com.fancymansion.core.common.const.PageColor
import com.fancymansion.core.common.const.PageLineHeight
import com.fancymansion.core.common.const.PageMarginHorizontal
import com.fancymansion.core.common.const.PageTextSize
import com.fancymansion.core.common.const.SelectorColor
import com.fancymansion.domain.model.book.PageContentSettingModel
import com.fancymansion.domain.model.book.PageSettingModel
import com.fancymansion.domain.model.book.SelectorSettingModel

@Entity(
    tableName = "PageSettingData",
    primaryKeys = ["userId", "mode", "bookId"]
)
data class PageSettingData(
    val userId: String,
    val mode: String,
    val bookId: String,

    val contentBackgroundColor: String,
    val contentTextSize: String,
    val contentLineHeight: String,
    val contentTextMarginHorizontal: String,
    val contentImageMarginHorizontal: String,

    val selectorBackgroundColor: String,
    val selectorTextSize: String,
    val selectorLineHeight: String
)


fun PageSettingData.asModel() = PageSettingModel(

    pageContentSetting = PageContentSettingModel(
        backgroundColor = PageColor.valueOf(contentBackgroundColor),
        textSize = PageTextSize.valueOf(contentTextSize),
        lineHeight = PageLineHeight.valueOf(contentLineHeight),
        textMarginHorizontal = PageMarginHorizontal.valueOf(contentTextMarginHorizontal),
        imageMarginHorizontal = PageMarginHorizontal.valueOf(contentImageMarginHorizontal)
    ),
    selectorSetting = SelectorSettingModel(
        backgroundColor = SelectorColor.valueOf(selectorBackgroundColor),
        textSize = PageTextSize.valueOf(selectorTextSize),
        lineHeight = PageLineHeight.valueOf(selectorLineHeight)
    )
)

fun PageSettingModel.asData(userId : String, mode: String, bookId: String) = PageSettingData(
    userId = userId,
    mode = mode,
    bookId = bookId,

    contentBackgroundColor = pageContentSetting.backgroundColor.name,
    contentTextSize = pageContentSetting.textSize.name,
    contentLineHeight = pageContentSetting.lineHeight.name,
    contentTextMarginHorizontal = pageContentSetting.textMarginHorizontal.name,
    contentImageMarginHorizontal = pageContentSetting.imageMarginHorizontal.name,

    selectorBackgroundColor = selectorSetting.backgroundColor.name,
    selectorTextSize = selectorSetting.textSize.name,
    selectorLineHeight = selectorSetting.lineHeight.name
)
package com.fancymansion.domain.model.book

import com.fancymansion.core.common.const.PageColor
import com.fancymansion.core.common.const.PageLineHeight
import com.fancymansion.core.common.const.PageMarginHorizontal
import com.fancymansion.core.common.const.PageTextSize
import com.fancymansion.core.common.const.SelectorColor

data class PageContentSettingModel(
    val backgroundColor: PageColor = PageColor.WHITE,
    val textSize: PageTextSize = PageTextSize.SIZE_5,
    val lineHeight: PageLineHeight = PageLineHeight.SIZE_5,
    val textMarginHorizontal: PageMarginHorizontal = PageMarginHorizontal.SIZE_2,
    val imageMarginHorizontal: PageMarginHorizontal = PageMarginHorizontal.SIZE_0
)

data class SelectorSettingModel(
    val backgroundColor: SelectorColor = SelectorColor.GRAY_MEDIUM,
    val textSize: PageTextSize = PageTextSize.SIZE_6,
    val lineHeight: PageLineHeight = PageLineHeight.SIZE_6,
)

data class PageSettingModel(
    val pageContentSetting: PageContentSettingModel = PageContentSettingModel(),
    val selectorSetting: SelectorSettingModel = SelectorSettingModel()
)
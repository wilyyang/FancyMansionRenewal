package com.fancymansion.domain.model.book

import com.fancymansion.core.common.const.PageLineHeight
import com.fancymansion.core.common.const.PageMarginHorizontal
import com.fancymansion.core.common.const.PageTextSize
import com.fancymansion.core.common.const.PageTheme

data class PageContentSettingModel(
    val textSize: PageTextSize = PageTextSize.TEXT_SIZE_6,
    val lineHeight: PageLineHeight = PageLineHeight.LINE_HEIGHT_4,
    val textMarginHorizontal: PageMarginHorizontal = PageMarginHorizontal.MARGIN_2,
    val imageMarginHorizontal: PageMarginHorizontal = PageMarginHorizontal.MARGIN_1
)

data class SelectorSettingModel(
    val textSize: PageTextSize = PageTextSize.TEXT_SIZE_6,
    val lineHeight: PageLineHeight = PageLineHeight.LINE_HEIGHT_4,
)

data class PageSettingModel(
    val pageTheme: PageTheme = PageTheme.THEME_WHITE,
    val pageContentSetting: PageContentSettingModel = PageContentSettingModel(),
    val selectorSetting: SelectorSettingModel = SelectorSettingModel()
)
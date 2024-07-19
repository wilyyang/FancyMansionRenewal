package com.fancymansion.domain.model.book

import com.fancymansion.core.common.const.PageLineHeight
import com.fancymansion.core.common.const.PageMarginHorizontal
import com.fancymansion.core.common.const.PageTextSize
import com.fancymansion.core.common.const.PageTheme
import com.fancymansion.core.common.const.SelectorPaddingVertical

data class PageContentSettingModel(
    val textSize: PageTextSize = PageTextSize.TEXT_SIZE_6,
    val lineHeight: PageLineHeight = PageLineHeight.LINE_HEIGHT_4,
    val textMarginHorizontal: PageMarginHorizontal = PageMarginHorizontal.MARGIN_3,
    val imageMarginHorizontal: PageMarginHorizontal = PageMarginHorizontal.MARGIN_1
)

data class SelectorSettingModel(
    val textSize: PageTextSize = PageTextSize.TEXT_SIZE_6,
    val paddingVertical: SelectorPaddingVertical = SelectorPaddingVertical.PADDING_4,
)

data class PageSettingModel(
    val pageTheme: PageTheme = PageTheme.THEME_WHITE,
    val pageContentSetting: PageContentSettingModel = PageContentSettingModel(),
    val selectorSetting: SelectorSettingModel = SelectorSettingModel()
)
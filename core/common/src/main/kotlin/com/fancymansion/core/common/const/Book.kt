package com.fancymansion.core.common.const

import com.fancymansion.core.common.resource.StringValue

/**
 * Viewer Config
 */
enum class PageTheme(val pageColor: PageColor, val textColor: TextColor, val selectorColor: SelectorColor, val selectorTextColor: TextColor, val isDarkTheme: Boolean = false) {
    THEME_WHITE(pageColor = PageColor.WHITE, textColor = TextColor.BLACK, selectorColor = SelectorColor.WHITE_OFF, selectorTextColor = TextColor.BLACK),
    THEME_DARK_GRAY(pageColor = PageColor.DARK_GRAY, textColor = TextColor.WHITE, selectorColor = SelectorColor.DARK_GRAY, selectorTextColor = TextColor.WHITE, isDarkTheme = true),
    THEME_IVORY(pageColor = PageColor.LIGHT_IVORY, textColor = TextColor.BLACK, selectorColor = SelectorColor.DARK_IVORY, selectorTextColor = TextColor.BLACK)
}

enum class PageColor(val colorCode: Long) {
    WHITE(0xff_ffffff),
    DARK_GRAY(0xff_1d1d1d),
    LIGHT_IVORY(0xff_f8f0e3)
}

enum class TextColor(val colorCode: Long) {
    BLACK(0xff_1d1d1d),
    WHITE(0xff_fefefc)
}

enum class SelectorColor(val colorCode: Long) {
    WHITE_OFF(0xff_eeeeee),
    DARK_GRAY(0xff_404040),
    DARK_IVORY(0xff_ebe5d4)
}

enum class PageTextSize(val dpSize: Float) {
    TEXT_SIZE_1(13f),
    TEXT_SIZE_2(14f),
    TEXT_SIZE_3(15f),
    TEXT_SIZE_4(16f),
    TEXT_SIZE_5(17f),
    TEXT_SIZE_6(18f), // DEFAULT
    TEXT_SIZE_7(19f),
    TEXT_SIZE_8(20f),
    TEXT_SIZE_9(21f),
    TEXT_SIZE_10(22f),
    TEXT_SIZE_11(23f),
    TEXT_SIZE_12(24f),
    TEXT_SIZE_13(25f),
    TEXT_SIZE_14(26f),
    TEXT_SIZE_15(27f);

    companion object {
        const val OFFSET = 1
        val values = entries.toTypedArray()
    }
}

enum class PageLineHeight(val dpSize: Float) {
    LINE_HEIGHT_1(8f),
    LINE_HEIGHT_2(10f),
    LINE_HEIGHT_3(12f),
    LINE_HEIGHT_4(14f), // DEFAULT
    LINE_HEIGHT_5(16f),
    LINE_HEIGHT_6(18f),
    LINE_HEIGHT_7(20f);

    companion object {
        const val OFFSET = 1
        val values = entries.toTypedArray()
    }
}

enum class SelectorPaddingVertical(val dpSize: Float) {
    PADDING_1(3f),
    PADDING_2(5f),
    PADDING_3(7f),
    PADDING_4(9f),   // DEFAULT
    PADDING_5(11f),
    PADDING_6(13f),
    PADDING_7(15f);

    companion object {
        const val OFFSET = 1
        val values = entries.toTypedArray()
    }
}

enum class PageMarginHorizontal(val dpSize: Float) {
    MARGIN_1(0f),
    MARGIN_2(8f),
    MARGIN_3(16f),  // DEFAULT
    MARGIN_4(24f),
    MARGIN_5(32f),
    MARGIN_6(40f),
    MARGIN_7(48f);

    companion object {
        const val OFFSET = 0
        val values = entries.toTypedArray()
    }
}

/**
 * Book
 */
data class EpisodeRef(
    val userId: String,
    val mode: ReadMode,
    val bookId: String,
    val episodeId: String
)

enum class ReadMode {
    EDIT, READ
}


/**
 * Logic
 */

const val ROUTE_PAGE_ID_NOT_ASSIGNED = -4000L
const val ACTION_ID_NOT_ASSIGNED = 0L

enum class PageType(
    val localizedName: StringValue.StringResource
) {
    START(localizedName = StringValue.StringResource(com.fancymansion.core.common.R.string.page_type_start)),
    NORMAL(localizedName = StringValue.StringResource(com.fancymansion.core.common.R.string.page_type_normal)),
    ENDING(localizedName = StringValue.StringResource(com.fancymansion.core.common.R.string.page_type_ending))
}

enum class RelationOp(
    val localizedName: StringValue.StringResource,
    val check: (Int, Int) -> Boolean
) {
    EQUAL(
        localizedName = StringValue.StringResource(com.fancymansion.core.common.R.string.relation_equal),
        check = { n1, n2 -> n1 == n2 }),
    NOT_EQUAL(
        localizedName = StringValue.StringResource(com.fancymansion.core.common.R.string.relation_not_equal),
        check = { n1, n2 -> n1 != n2 }),
    LESS_THAN(
        localizedName = StringValue.StringResource(com.fancymansion.core.common.R.string.relation_less_than),
        check = { n1, n2 -> n1 < n2 }),
    GREATER_THAN(
        localizedName = StringValue.StringResource(com.fancymansion.core.common.R.string.relation_greater_than),
        check = { n1, n2 -> n1 > n2 });
}

enum class LogicalOp(
    val localizedName: StringValue.StringResource,
    val check: (Boolean, Boolean) -> Boolean
) {
    AND(
        localizedName = StringValue.StringResource(com.fancymansion.core.common.R.string.logical_and),
        check = { n1, n2 -> n1 && n2 }),
    OR(
        localizedName = StringValue.StringResource(com.fancymansion.core.common.R.string.logical_or),
        check = { n1, n2 -> n1 || n2 });
}
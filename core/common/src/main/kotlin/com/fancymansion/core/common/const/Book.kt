package com.fancymansion.core.common.const

import com.fancymansion.core.common.resource.StringValue

/**
 * Viewer Config
 */
enum class PageTheme(val pageColor: PageColor, val textColor: TextColor, val selectorColor: SelectorColor, val selectorTextColor: TextColor) {
    THEME_WHITE(pageColor = PageColor.WHITE, textColor = TextColor.BLACK, selectorColor = SelectorColor.WHITE_OFF, selectorTextColor = TextColor.BLACK),
    THEME_DARK(pageColor = PageColor.BLACK, textColor = TextColor.WHITE, selectorColor = SelectorColor.GRAY_DARK, selectorTextColor = TextColor.RED)
}

enum class PageColor(val colorCode: Long) {
    WHITE(0xff_ffffff),
    BLACK(0xff_000000)
}

enum class TextColor(val colorCode: Long) {
    BLACK(0xff_000000),
    WHITE(0xff_ffffff),
    RED(0xff_ff0000)
}

enum class SelectorColor(val colorCode: Long) {
    WHITE_OFF(0xff_eeeeee),
    GRAY_DARK(0xff_555555)
}

enum class PageTextSize(val dpSize: Float) {
    TEXT_SIZE_1(14f),
    TEXT_SIZE_2(15f),
    TEXT_SIZE_3(16f),
    TEXT_SIZE_4(17f),
    TEXT_SIZE_5(18f),
    TEXT_SIZE_6(19f),
    TEXT_SIZE_7(20f),
    TEXT_SIZE_8(21f),
    TEXT_SIZE_9(22f),
    TEXT_SIZE_10(23f),
    TEXT_SIZE_11(24f),
    TEXT_SIZE_12(25f),
    TEXT_SIZE_13(26f),
    TEXT_SIZE_14(27f),
    TEXT_SIZE_15(28f)
}

enum class PageLineHeight(val dpSize: Float) {
    LINE_HEIGHT_1(3f),
    LINE_HEIGHT_2(5f),
    LINE_HEIGHT_3(7f),
    LINE_HEIGHT_4(9f),
    LINE_HEIGHT_5(11f),
    LINE_HEIGHT_6(13f),
    LINE_HEIGHT_7(15f)
}

enum class SelectorPaddingVertical(val dpSize: Float) {
    PADDING_1(3f),
    PADDING_2(5f),
    PADDING_3(7f),
    PADDING_4(9f),
    PADDING_5(11f),
    PADDING_6(13f),
    PADDING_7(15f)
}

enum class PageMarginHorizontal(val dpSize: Float) {
    MARGIN_1(0f),
    MARGIN_2(2f),
    MARGIN_3(4f),
    MARGIN_4(6f),
    MARGIN_5(8f),
    MARGIN_6(10f),
    MARGIN_7(12f)
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
package com.fancymansion.core.common.const

import com.fancymansion.core.common.resource.StringValue

/**
 * Viewer Config
 */
enum class PageColor(val colorCode: Long) {
    WHITE(0xff_ffffff),
    LIGHT_IVORY(0xff_f3efe3),
    LIGHT_GRAY(0xff_d6d3cb),
    GRAY(0xff_727272)
}

enum class SelectorColor(val colorCode: Long) {
    GRAY_MEDIUM(0xff_a6a6a6),
    BROWN_LIGHT(0xff_d3bbab),
    GRAY_DARK(0xff_555555),
    WHITE_OFF(0xff_eeeeee)
}

enum class PageTextSize(val dpSize: Float) {
    SIZE_1(10f),
    SIZE_2(11f),
    SIZE_3(12f),
    SIZE_4(13f),
    SIZE_5(14f),
    SIZE_6(15f),
    SIZE_7(16f),
    SIZE_8(17f),
}

enum class PageLineHeight(val dpSize: Float) {
    SIZE_1(12f),
    SIZE_2(13f),
    SIZE_3(14f),
    SIZE_4(15f),
    SIZE_5(16f),
    SIZE_6(17f),
    SIZE_7(18f),
    SIZE_8(19f),
}

enum class PageMarginHorizontal(val dpSize: Float) {
    SIZE_0(0f),
    SIZE_1(2f),
    SIZE_2(4f),
    SIZE_3(6f),
    SIZE_4(8f),
    SIZE_5(10f),
    SIZE_6(12f),
    SIZE_7(14f),
}

/**
 * Book
 */
data class BookRef(
    val userId: String,
    val mode: ReadMode,
    val bookId: String
)

enum class ReadMode {
    EDIT, READ
}


/**
 * Logic
 */

const val ROUTE_PAGE_ID_NOT_ASSIGNED = -4000L
const val COMPARE_ACTION_ID_NOT_ASSIGNED = -4000L

enum class PageType(
    val localizedName: StringValue.StringResource
) {
    START(localizedName = StringValue.StringResource(com.fancymansion.core.common.R.string.page_type_start)),
    NORMAL(localizedName = StringValue.StringResource(com.fancymansion.core.common.R.string.page_type_normal)),
    ENDING(localizedName = StringValue.StringResource(com.fancymansion.core.common.R.string.page_type_ending))
}

enum class ConditionType(
    val localizedName: StringValue.StringResource
) {
    COUNT(localizedName = StringValue.StringResource(com.fancymansion.core.common.R.string.compare_count)),
    TARGET_VIEWS(localizedName = StringValue.StringResource(com.fancymansion.core.common.R.string.compare_target_views))
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
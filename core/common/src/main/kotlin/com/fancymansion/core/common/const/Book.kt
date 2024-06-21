package com.fancymansion.core.common.const

import com.fancymansion.core.common.resource.StringValue

/**
 * Book
 */
@JvmInline
value class BookId(val id: Long)

@JvmInline
value class PageId(val id: Long)

enum class ReadMode {
    EDIT, READ
}

/**s
 * Content
 */
sealed class Source {
    data class Text(val description: String) : Source()
//    data class Image(val filePath: String) : Source()

    // 임시
    data class Image(val resId: Int) : Source()
}

/**
 * Logic
 */
@JvmInline
value class SelectorId(val id: Long)

@JvmInline
value class RouteId(val id: Long)

@JvmInline
value class ConditionId(val id: Long)

const val ROUTE_PAGE_ID_NOT_ASSIGNED = -4000L
const val COMPARE_ID_NOT_ASSIGNED = -4000L

enum class PageType(
    val localizedName: StringValue.StringResource
) {
    START(localizedName = StringValue.StringResource(com.fancymansion.core.common.R.string.page_type_start)),
    NORMAL(localizedName = StringValue.StringResource(com.fancymansion.core.common.R.string.page_type_normal)),
    ENDING(localizedName = StringValue.StringResource(com.fancymansion.core.common.R.string.page_type_ending))
}

enum class RelationOp(
    val localizedName: StringValue.StringResource,
    val check: (Boolean, Boolean) -> Boolean
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
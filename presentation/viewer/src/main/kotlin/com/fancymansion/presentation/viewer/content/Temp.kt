package com.fancymansion.presentation.viewer.content

import com.fancymansion.core.common.resource.StringValue

enum class ReadMode {
    Edit, Read
}

enum class Keyword(val category: StringValue, val keywords : List<StringValue>) {
    Romance(category = StringValue.StringWrapper("romance"), keywords = listOf(
        StringValue.StringWrapper("school"),
        StringValue.StringWrapper("office")
    ))
}

//data class Book(val config: Config, var logic: Logic, val content: Content)

data class Config(
    val id: String = "",
    val version: Long = 0L,
    val createTime: Long = System.currentTimeMillis(),
    val editTime: Long = System.currentTimeMillis(),
    val readMode: ReadMode = ReadMode.Edit,
)

data class Introduce(
    val title: String = "",
    val description: String = "",
    val introduceCoverList: List<String> = listOf(),
    val keywordList: MutableList<String> = mutableListOf()
)

data class Community(
    val publishCode: String,
    val publishTime: Long,
    val updateTime: Long,
    var downloads: Int,
    var userGoodCount: Int
)

data class Editor(
    var email: String = "",
    var user: String = "",
    var uid: String = "",
    var writer: String = "",
    var illustrator: String = "",
)

// (2)
//data class Logic(val bookId: Long, var logics: MutableList<SlideLogic> = mutableListOf())
//
//// (2.2)
//
//enum class PageType {
//    START,
//    NORMAL,
//    END
//}
//
//data class PageLogic(
//    val id: Long,
//    val title: String,
//    val type: PageType = Const.SLIDE_TYPE_NORMAL,
//    val choiceItems: MutableList<ChoiceItem> = mutableListOf()
//)

data class Selector(
    val id: Long,
    val text: String,
    val showConditions: List<Condition> = listOf(),
    val routes: List<Route> = listOf()
)

const val ROUTE_PAGE_ID_NOT_ASSIGNED = -3000L

data class Route(
    val id: Long,
    val routePageId: Long = ROUTE_PAGE_ID_NOT_ASSIGNED,
    val routeConditions: List<Condition> = listOf()
)


/**
 * Condition
 */
@JvmInline
value class ConditionId(val value: Long)
val CONDITION_TARGET_ID_NOT_ASSIGNED = ConditionId(-4000L)

enum class RelationOp(
    val localizedName: StringValue,
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


    companion object {
        fun from(name: String): RelationOp = entries.find { it.name == name } ?: EQUAL
    }
}

enum class LogicalOp(
    val localizedName: StringValue,
    val check: (Boolean, Boolean) -> Boolean
) {
    AND(
        localizedName = StringValue.StringResource(com.fancymansion.core.common.R.string.logical_and),
        check = { n1, n2 -> n1 && n2 }),
    OR(
        localizedName = StringValue.StringResource(com.fancymansion.core.common.R.string.logical_or),
        check = { n1, n2 -> n1 || n2 });

    companion object {
        fun from(name: String): LogicalOp = entries.find { it.name == name } ?: AND
    }
}

data class Condition(
    val id: ConditionId,
    val targetId: ConditionId = CONDITION_TARGET_ID_NOT_ASSIGNED,
    val compareId: ConditionId = CONDITION_TARGET_ID_NOT_ASSIGNED,
    val count: Int = 0,
    val relationOp: RelationOp = RelationOp.EQUAL,
    val logicalOp: LogicalOp = LogicalOp.AND
)


/**
 * Content
 */
sealed class Source {
    data class Text(val description: String) : Source()
//    data class Image(val filePath: String) : Source()

    // 임시
    data class Image(val resId: Int) : Source()
}

data class Content(
    val pages: List<Page>
)

data class Page(
    val id: Long,
    val title: String,
    val sources: List<Source>
)

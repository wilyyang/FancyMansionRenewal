package com.fancymansion.presentation.editor.conditionContent

import com.fancymansion.core.common.const.ACTION_ID_NOT_ASSIGNED
import com.fancymansion.core.common.const.LogicalOp
import com.fancymansion.core.common.const.RelationOp
import com.fancymansion.core.common.resource.StringValue
import com.fancymansion.core.presentation.base.ViewEvent
import com.fancymansion.core.presentation.base.ViewSideEffect
import com.fancymansion.core.presentation.base.ViewState
import com.fancymansion.domain.model.book.ActionIdModel
import com.fancymansion.domain.model.book.ConditionRuleModel
import com.fancymansion.domain.model.book.LogicModel
import com.fancymansion.presentation.editor.R
import com.fancymansion.presentation.editor.common.SelectItemWrapper

sealed class ConditionRuleWrapper(
    open val selfActionId: ActionIdWrapper,
    open val relationOp: RelationOp = RelationOp.EQUAL,
    open val logicalOp: LogicalOp = LogicalOp.AND
) {
    data class CountConditionRuleWrapper(
        override val selfActionId: ActionIdWrapper,
        override val relationOp: RelationOp = RelationOp.EQUAL,
        override val logicalOp: LogicalOp = LogicalOp.AND,
        val count: Int
    ) : ConditionRuleWrapper(selfActionId, relationOp, logicalOp)

    data class TargetConditionRuleWrapper(
        override val selfActionId: ActionIdWrapper,
        override val relationOp: RelationOp = RelationOp.EQUAL,
        override val logicalOp: LogicalOp = LogicalOp.AND,
        val targetActionId: ActionIdWrapper
    ) : ConditionRuleWrapper(selfActionId, relationOp, logicalOp)


    fun withUpdatedSelfPageInfo(
        pageId: Long,
        pageTitle: String?
    ): ConditionRuleWrapper = when (this) {
        is CountConditionRuleWrapper -> copy(
            selfActionId = selfActionId.copy(pageId = pageId, pageTitle = pageTitle, selectorId = ACTION_ID_NOT_ASSIGNED, selectorText = null)
        )
        is TargetConditionRuleWrapper -> copy(
            selfActionId = selfActionId.copy(pageId = pageId, pageTitle = pageTitle, selectorId = ACTION_ID_NOT_ASSIGNED, selectorText = null)
        )
    }

    fun withUpdatedSelfSelectorInfo(
        selectorId: Long,
        selectorText: String?
    ): ConditionRuleWrapper = when (this) {
        is CountConditionRuleWrapper -> copy(
            selfActionId = selfActionId.copy(selectorId = selectorId, selectorText = selectorText)
        )
        is TargetConditionRuleWrapper -> copy(
            selfActionId = selfActionId.copy(selectorId = selectorId, selectorText = selectorText)
        )
    }
}

fun ConditionRuleWrapper.toModel(): ConditionRuleModel = when (this) {
    is ConditionRuleWrapper.CountConditionRuleWrapper ->
        ConditionRuleModel.CountConditionRuleModel(
            selfActionId = selfActionId.toModel(),
            relationOp = relationOp,
            logicalOp = logicalOp,
            count = count
        )

    is ConditionRuleWrapper.TargetConditionRuleWrapper ->
        ConditionRuleModel.TargetConditionRuleModel(
            selfActionId = selfActionId.toModel(),
            relationOp = relationOp,
            logicalOp = logicalOp,
            targetActionId = targetActionId.toModel()
        )
}

fun ConditionRuleModel.toWrapper(logic: LogicModel): ConditionRuleWrapper = when (this) {
    is ConditionRuleModel.CountConditionRuleModel ->
        ConditionRuleWrapper.CountConditionRuleWrapper(
            selfActionId = selfActionId.toWrapper(logic),
            relationOp = relationOp,
            logicalOp = logicalOp,
            count = count
        )

    is ConditionRuleModel.TargetConditionRuleModel ->
        ConditionRuleWrapper.TargetConditionRuleWrapper(
            selfActionId = selfActionId.toWrapper(logic),
            relationOp = relationOp,
            logicalOp = logicalOp,
            targetActionId = targetActionId.toWrapper(logic)
        )
}

data class ActionIdWrapper(
    val pageId: Long = ACTION_ID_NOT_ASSIGNED,
    val pageTitle: String? = null,
    val selectorId: Long = ACTION_ID_NOT_ASSIGNED,
    val selectorText: String? = null,
    val routeId: Long = ACTION_ID_NOT_ASSIGNED
)

fun ActionIdWrapper.toModel(): ActionIdModel =
    ActionIdModel(
        pageId = pageId,
        selectorId = selectorId,
        routeId = routeId
    )

fun ActionIdModel.toWrapper(logic: LogicModel): ActionIdWrapper {
    val pageLogic = logic.logics.firstOrNull { it.pageId == pageId }
    val pageTitle = pageLogic?.title
    val selectorText = pageLogic
        ?.selectors
        ?.firstOrNull { it.selectorId == selectorId }
        ?.text

    return ActionIdWrapper(
        pageId = pageId,
        pageTitle = pageTitle,
        selectorId = selectorId,
        selectorText = selectorText,
        routeId = routeId
    )
}

enum class CompareType(
    val localizedName: StringValue.StringResource
) {
    Count(localizedName = StringValue.StringResource(R.string.edit_condition_content_compare_type_count)),
    TargetActionId(localizedName = StringValue.StringResource(R.string.edit_condition_content_compare_type_target))
}

class EditorConditionContentContract {
    companion object {
        const val NAME = "editor_condition_content"
    }

    data class State(
        val isInitSuccess : Boolean = false,
        val bookTitle : String = "",
        val pageTitle : String = "",
        val selectorText : String = "",
        val barTitleResId : Int = R.string.topbar_editor_title_condition_content,
        val barSubTitle : String = "",
        val conditionRule: ConditionRuleWrapper = ConditionRuleWrapper.CountConditionRuleWrapper(
            selfActionId = ActionIdWrapper(),
            count = 0
        ),
        val pageItemList: List<SelectItemWrapper> = listOf(),
        val selectorItemMap: Map<Long, List<SelectItemWrapper>> = emptyMap()
    ) : ViewState

    sealed class Event : ViewEvent {
        data object ConditionSaveToFile : Event()

        data class SelectSelfPage(val itemId : Long) : Event()
        data class SelectSelfSelector(val itemId : Long) : Event()

        data class SelectCompareType(val type : CompareType) : Event()
        data class SelectTargetCount(val count : Int) : Event()
        data class SelectTargetPage(val itemId : Long) : Event()
        data class SelectTargetSelector(val itemId : Long) : Event()

        data class SelectRelationOperator(val relationOp : RelationOp) : Event()
        data class SelectNextLogicalOperator(val logicalOp : LogicalOp) : Event()
    }

    sealed class Effect : ViewSideEffect {
        sealed class Navigation : Effect()
    }
}
package com.fancymansion.presentation.editor.conditionContent

import com.fancymansion.core.common.const.CONDITION_ID_NOT_ASSIGNED
import com.fancymansion.core.common.const.PAGE_ID_NOT_ASSIGNED
import com.fancymansion.core.common.const.SELECTOR_ID_NOT_ASSIGNED
import com.fancymansion.core.presentation.base.ViewEvent
import com.fancymansion.core.presentation.base.ViewSideEffect
import com.fancymansion.core.presentation.base.ViewState
import com.fancymansion.domain.model.book.ActionIdModel
import com.fancymansion.presentation.editor.R
import com.fancymansion.presentation.editor.common.ActionInfo.CountInfo
import com.fancymansion.presentation.editor.common.ActionInfo.PageInfo
import com.fancymansion.presentation.editor.common.ConditionGroup.ShowSelectorCondition
import com.fancymansion.presentation.editor.common.ConditionWrapper
import com.fancymansion.presentation.editor.common.TargetPageWrapper

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
        val condition: ConditionWrapper = ConditionWrapper(
            conditionId = CONDITION_ID_NOT_ASSIGNED,
            conditionGroup = ShowSelectorCondition(
                pageId = PAGE_ID_NOT_ASSIGNED,
                selectorId = SELECTOR_ID_NOT_ASSIGNED
            ),
            selfActionInfo = PageInfo(pageTitle = "", actionId = ActionIdModel()),
            targetActionInfo = CountInfo(0)
        )
    ) : ViewState

    sealed class Event : ViewEvent

    sealed class Effect : ViewSideEffect {
        sealed class Navigation : Effect()
    }
}
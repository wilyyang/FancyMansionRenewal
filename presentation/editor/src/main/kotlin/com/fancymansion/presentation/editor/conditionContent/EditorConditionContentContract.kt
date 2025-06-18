package com.fancymansion.presentation.editor.conditionContent

import com.fancymansion.core.presentation.base.ViewEvent
import com.fancymansion.core.presentation.base.ViewSideEffect
import com.fancymansion.core.presentation.base.ViewState
import com.fancymansion.domain.model.book.ActionIdModel
import com.fancymansion.domain.model.book.ConditionRuleModel
import com.fancymansion.domain.model.book.ConditionRuleModel.CountConditionRuleModel
import com.fancymansion.presentation.editor.R
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
        val conditionRule: ConditionRuleModel = CountConditionRuleModel(
            selfActionId = ActionIdModel(),
            count = 0
        ),
        val targetPageList: List<TargetPageWrapper> = listOf()
    ) : ViewState

    sealed class Event : ViewEvent

    sealed class Effect : ViewSideEffect {
        sealed class Navigation : Effect()
    }
}
package com.fancymansion.presentation.editor.conditionContent

import androidx.lifecycle.SavedStateHandle
import com.fancymansion.core.common.const.ACTION_ID_NOT_ASSIGNED
import com.fancymansion.core.common.const.ArgName.NAME_BOOK_ID
import com.fancymansion.core.common.const.ArgName.NAME_BOOK_TITLE
import com.fancymansion.core.common.const.ArgName.NAME_CONDITION_ID
import com.fancymansion.core.common.const.ArgName.NAME_EPISODE_ID
import com.fancymansion.core.common.const.ArgName.NAME_PAGE_ID
import com.fancymansion.core.common.const.ArgName.NAME_READ_MODE
import com.fancymansion.core.common.const.ArgName.NAME_ROUTE_ID
import com.fancymansion.core.common.const.ArgName.NAME_SELECTOR_ID
import com.fancymansion.core.common.const.ArgName.NAME_USER_ID
import com.fancymansion.core.common.const.EpisodeRef
import com.fancymansion.core.common.const.ROUTE_ID_NOT_ASSIGNED
import com.fancymansion.core.common.const.ReadMode
import com.fancymansion.core.common.resource.StringValue
import com.fancymansion.core.common.util.ellipsis
import com.fancymansion.core.presentation.base.BaseViewModel
import com.fancymansion.core.presentation.base.CommonEvent
import com.fancymansion.core.presentation.base.LoadState
import com.fancymansion.domain.model.book.ConditionModel
import com.fancymansion.domain.model.book.LogicModel
import com.fancymansion.domain.usecase.book.UseCaseLoadBook
import com.fancymansion.domain.usecase.book.UseCaseMakeBook
import com.fancymansion.domain.usecase.util.UseCaseGetResource
import com.fancymansion.presentation.editor.R
import com.fancymansion.presentation.editor.common.ConditionGroup
import com.fancymansion.presentation.editor.common.ConditionGroup.RouteCondition
import com.fancymansion.presentation.editor.common.ConditionGroup.ShowSelectorCondition
import com.fancymansion.presentation.editor.common.ITEM_ID_NOT_ASSIGNED
import com.fancymansion.presentation.editor.common.SelectItemWrapper
import com.fancymansion.presentation.editor.conditionContent.ConditionRuleWrapper.CountConditionRuleWrapper
import com.fancymansion.presentation.editor.conditionContent.ConditionRuleWrapper.TargetConditionRuleWrapper
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class EditorConditionContentViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val useCaseLoadBook: UseCaseLoadBook,
    private val useCaseMakeBook: UseCaseMakeBook,
    private val useCaseGetResource: UseCaseGetResource
) : BaseViewModel<EditorConditionContentContract.State, EditorConditionContentContract.Event, EditorConditionContentContract.Effect>() {

    private var isUpdateResume = false

    private val episodeRef: EpisodeRef = savedStateHandle.run {
        EpisodeRef(
            requireNotNull(get<String>(NAME_USER_ID)),
            requireNotNull(get<ReadMode>(NAME_READ_MODE)),
            requireNotNull(get<String>(NAME_BOOK_ID)),
            requireNotNull(get<String>(NAME_EPISODE_ID))
        )
    }

    private val conditionGroup: ConditionGroup by lazy {
        val pageId = requireNotNull(savedStateHandle.get<Long>(NAME_PAGE_ID))
        val selectorId = requireNotNull(savedStateHandle.get<Long>(NAME_SELECTOR_ID))
        val routeId = requireNotNull(savedStateHandle.get<Long>(NAME_ROUTE_ID))

        if (routeId == ROUTE_ID_NOT_ASSIGNED) {
            ShowSelectorCondition(pageId, selectorId)
        } else {
            RouteCondition(pageId, selectorId, routeId)
        }
    }

    private val conditionId: Long by lazy { requireNotNull(savedStateHandle.get<Long>(NAME_CONDITION_ID)) }
    private lateinit var logic: LogicModel
    private lateinit var originCondition: ConditionModel

    init {
        initializeState()
    }

    override fun setInitialState() = EditorConditionContentContract.State()

    override fun handleEvents(event: EditorConditionContentContract.Event) {
        when(event) {
            is EditorConditionContentContract.Event.SelectSelfPage -> selectSelfPageId(event.itemId)
            is EditorConditionContentContract.Event.SelectSelfSelector -> selectSelfSelectorId(event.itemId)
            is EditorConditionContentContract.Event.SelectCompareType -> selectCompareType(event.type)
            is EditorConditionContentContract.Event.SelectTargetPage -> selectTargetPageId(event.itemId)
            is EditorConditionContentContract.Event.SelectTargetSelector -> selectTargetSelectorId(event.itemId)
        }
    }

    override fun handleCommonEvents(event: CommonEvent) {
        when(event){
            is CommonEvent.OnResume -> handleOnResume()
            is CommonEvent.CloseEvent -> {
                if (uiState.value.isInitSuccess) {
                    checkConditionEdited {
                        super.handleCommonEvents(CommonEvent.CloseEvent)
                    }
                } else {
                    super.handleCommonEvents(CommonEvent.CloseEvent)
                }
            }
            else -> super.handleCommonEvents(event)
        }
    }

    private fun initializeState() {
        launchWithInit {
            updateCondition(conditionId)

            val bookTitle = requireNotNull(savedStateHandle.get<String>(NAME_BOOK_TITLE))
            val (pageTitle, selectorText) = logic.logics.firstOrNull { it.pageId == conditionGroup.pageId }!!
                .let { pageLogic ->
                    (pageLogic.title to pageLogic.selectors.firstOrNull { it.selectorId == conditionGroup.selectorId }?.text.orEmpty())
                }

            val barTitleResId = if(conditionGroup is RouteCondition){
                R.string.topbar_editor_title_route_condition_content
            }else{
                R.string.topbar_editor_title_show_condition_content
            }

            val MAX_SUB_TITLE_PART_LENGTH = 10
            val barSubTitle = if (conditionGroup is RouteCondition) {
                val routeTargetPageId = useCaseLoadBook.getRoute(
                    logic,
                    conditionGroup.pageId,
                    conditionGroup.selectorId,
                    (conditionGroup as RouteCondition).routeId
                ).routeTargetPageId

                val targetPageTitle = logic.logics.firstOrNull { it.pageId == routeTargetPageId }?.title.orEmpty()
                useCaseGetResource.string(
                    R.string.topbar_editor_sub_title_route_condition_content,
                    selectorText.ellipsis(MAX_SUB_TITLE_PART_LENGTH),
                    targetPageTitle.ellipsis(MAX_SUB_TITLE_PART_LENGTH)
                )
            } else {
                useCaseGetResource.string(
                    R.string.topbar_editor_sub_title_show_condition_content,
                    pageTitle.ellipsis(MAX_SUB_TITLE_PART_LENGTH),
                    selectorText.ellipsis(MAX_SUB_TITLE_PART_LENGTH)
                )
            }

            setState {
                copy(
                    isInitSuccess = true,
                    bookTitle = bookTitle,
                    pageTitle = pageTitle,
                    selectorText = selectorText,
                    barTitleResId = barTitleResId,
                    barSubTitle = barSubTitle
                )
            }
        }
    }

    // EditorConditionEvent
    private fun selectSelfPageId(itemId : Long) {
        val (pageId, pageTitle) = if(itemId != ITEM_ID_NOT_ASSIGNED){
            (itemId to uiState.value.pageItemList.firstOrNull { it.itemId == itemId }?.itemText)
        }else{
            (ACTION_ID_NOT_ASSIGNED to null)
        }
        setState {
            copy(
                conditionRule = conditionRule.withUpdatedSelfPageInfo(
                    pageId = pageId,
                    pageTitle = pageTitle
                )
            )
        }
    }

    private fun selectSelfSelectorId(itemId : Long) {
        val (selectorId, selectorText) = if(itemId != ITEM_ID_NOT_ASSIGNED){
            (itemId to uiState.value.selectorItemMap[uiState.value.conditionRule.selfActionId.pageId]?.first { it.itemId == itemId }?.itemText)
        }else{
            (ACTION_ID_NOT_ASSIGNED to null)
        }
        setState {
            copy(
                conditionRule = conditionRule.withUpdatedSelfSelectorInfo(
                    selectorId = selectorId,
                    selectorText = selectorText
                )
            )
        }
    }

    private fun selectCompareType(type : CompareType) {
        val conditionRule = uiState.value.conditionRule
        val newConditionRule = when{
            conditionRule is CountConditionRuleWrapper && type != CompareType.Count-> {
                TargetConditionRuleWrapper(
                    selfActionId = conditionRule.selfActionId,
                    relationOp = conditionRule.relationOp,
                    logicalOp = conditionRule.logicalOp,
                    targetActionId = ActionIdWrapper()
                )
            }
            conditionRule is TargetConditionRuleWrapper && type != CompareType.TargetActionId -> {
                CountConditionRuleWrapper(
                    selfActionId = conditionRule.selfActionId,
                    relationOp = conditionRule.relationOp,
                    logicalOp = conditionRule.logicalOp,
                    count = 0
                )
            }
            else -> conditionRule
        }

        setState {
            copy(
                conditionRule = newConditionRule
            )
        }
    }

    private fun selectTargetPageId(itemId : Long) {
        val conditionRule = uiState.value.conditionRule
        if(conditionRule is TargetConditionRuleWrapper){
            val (pageId, pageTitle) = if(itemId != ITEM_ID_NOT_ASSIGNED){
                (itemId to uiState.value.pageItemList.firstOrNull { it.itemId == itemId }?.itemText)
            }else{
                (ACTION_ID_NOT_ASSIGNED to null)
            }

            setState {
                copy(
                    conditionRule = conditionRule.copy(
                        targetActionId = conditionRule.targetActionId.copy(
                            pageId = pageId,
                            pageTitle = pageTitle,
                            selectorId = ACTION_ID_NOT_ASSIGNED,
                            selectorText = null
                        )
                    )
                )
            }
        }
    }

    private fun selectTargetSelectorId(itemId : Long) {
        val conditionRule = uiState.value.conditionRule
        if (conditionRule is TargetConditionRuleWrapper) {
            val (selectorId, selectorText) = if (itemId != ITEM_ID_NOT_ASSIGNED) {
                (itemId to uiState.value.selectorItemMap[conditionRule.targetActionId.pageId]?.first { it.itemId == itemId }?.itemText)
            } else {
                (ACTION_ID_NOT_ASSIGNED to null)
            }

            setState {
                copy(
                    conditionRule = conditionRule.copy(
                        targetActionId = conditionRule.targetActionId.copy(
                            selectorId = selectorId,
                            selectorText = selectorText
                        )
                    )
                )
            }
        }
    }

    // CommonEvent
    private suspend fun updateCondition(conditionId: Long) {
        logic = useCaseLoadBook.loadLogic(episodeRef)
        originCondition = if(conditionGroup is RouteCondition){
            useCaseLoadBook.getRouteCondition(logic, conditionGroup.pageId, conditionGroup.selectorId, (conditionGroup as RouteCondition).routeId, conditionId)
        }else{
            useCaseLoadBook.getShowSelectorCondition(logic, conditionGroup.pageId, conditionGroup.selectorId, conditionId)
        }

        val pageItemList = logic.logics.map {
            SelectItemWrapper(
                itemId = it.pageId,
                itemText = it.title
            )
        }

        val selectorItemMap = logic.logics.associate { logicPage ->
            logicPage.pageId to listOf(
                SelectItemWrapper(itemText = useCaseGetResource.string(R.string.edit_condition_content_select_action_selector_not_found))
            ) + logicPage.selectors.map { selector ->
                SelectItemWrapper(selector.selectorId, selector.text)
            }
        }

        setState {
            copy(
                conditionRule = originCondition.conditionRule.toWrapper(logic),
                pageItemList = pageItemList,
                selectorItemMap = selectorItemMap
            )
        }
        // TODO 06.12 get condition and setState
    }

    private suspend fun saveEditedConditionAndReload() : Boolean{
        // TODO 06.12 save condition
        updateCondition(conditionId)
        return true
    }

    private fun handleOnResume() {
        if (isUpdateResume) {
            isUpdateResume = false
            launchWithLoading { updateCondition(conditionId) }
        }
    }

    private fun checkConditionEdited(onCheckComplete: () -> Unit) {
        // TODO 06.12 edited condition to model
        if (false) {
            setLoadState(
                LoadState.AlarmDialog(
                    title = StringValue.StringResource(com.fancymansion.core.common.R.string.book_file_edited_info_title),
                    message = StringValue.StringResource(R.string.dialog_save_edited_book_info),
                    confirmText = StringValue.StringResource(com.fancymansion.core.common.R.string.confirm),
                    onConfirm = {
                        //수정 중인 정보 파일 저장
                        launchWithLoading {
                            saveEditedConditionAndReload()
                            setLoadStateIdle()
                            onCheckComplete()
                        }
                    },
                    dismissText = StringValue.StringResource(com.fancymansion.core.common.R.string.cancel),
                    onDismiss = {
                        //수정 중인 정보 삭제
                        launchWithLoading {
                            updateCondition(conditionId)
                            setLoadStateIdle()
                            onCheckComplete()
                        }
                    }
                )
            )
        } else {
            onCheckComplete()
        }
    }
}
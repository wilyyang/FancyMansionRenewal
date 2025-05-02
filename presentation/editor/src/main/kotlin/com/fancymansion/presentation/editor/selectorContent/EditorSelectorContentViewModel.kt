package com.fancymansion.presentation.editor.selectorContent

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.SavedStateHandle
import com.fancymansion.core.common.const.ArgName.NAME_BOOK_ID
import com.fancymansion.core.common.const.ArgName.NAME_BOOK_TITLE
import com.fancymansion.core.common.const.ArgName.NAME_EPISODE_ID
import com.fancymansion.core.common.const.ArgName.NAME_PAGE_ID
import com.fancymansion.core.common.const.ArgName.NAME_READ_MODE
import com.fancymansion.core.common.const.ArgName.NAME_SELECTOR_ID
import com.fancymansion.core.common.const.ArgName.NAME_USER_ID
import com.fancymansion.core.common.const.EpisodeRef
import com.fancymansion.core.common.const.ROUTE_PAGE_ID_NOT_ASSIGNED
import com.fancymansion.core.common.const.ReadMode
import com.fancymansion.core.common.util.BookIDManager
import com.fancymansion.core.presentation.base.BaseViewModel
import com.fancymansion.core.presentation.base.CommonEvent
import com.fancymansion.domain.model.book.ActionIdModel
import com.fancymansion.domain.model.book.LogicModel
import com.fancymansion.domain.model.book.SelectorModel
import com.fancymansion.domain.usecase.book.UseCaseLoadBook
import com.fancymansion.domain.usecase.book.UseCaseMakeBook
import com.fancymansion.domain.usecase.util.UseCaseGetResource
import com.fancymansion.presentation.editor.common.ActionInfo
import com.fancymansion.presentation.editor.common.ActionInfo.CountInfo
import com.fancymansion.presentation.editor.common.ConditionGroup
import com.fancymansion.presentation.editor.common.ConditionState
import com.fancymansion.presentation.editor.common.ConditionWrapper
import com.fancymansion.presentation.editor.common.toWrapper
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class EditorSelectorContentViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val useCaseLoadBook: UseCaseLoadBook,
    private val useCaseMakeBook: UseCaseMakeBook,
    private val useCaseGetResource: UseCaseGetResource
) : BaseViewModel<EditorSelectorContentContract.State, EditorSelectorContentContract.Event, EditorSelectorContentContract.Effect>() {

    val showConditionStates: SnapshotStateList<ConditionState> = mutableStateListOf()
    val routeStates: SnapshotStateList<RouteState> = mutableStateListOf()

    private var isUpdateResume = false
    private val pageId: Long by lazy { requireNotNull(savedStateHandle.get<Long>(NAME_PAGE_ID)) }
    private val selectorId: Long by lazy { requireNotNull(savedStateHandle.get<Long>(NAME_SELECTOR_ID)) }

    private val episodeRef: EpisodeRef = savedStateHandle.run {
        EpisodeRef(
            requireNotNull(get<String>(NAME_USER_ID)),
            requireNotNull(get<ReadMode>(NAME_READ_MODE)),
            requireNotNull(get<String>(NAME_BOOK_ID)),
            requireNotNull(get<String>(NAME_EPISODE_ID))
        )
    }
    private lateinit var logic: LogicModel
    private lateinit var originSelector: SelectorModel

    init {
        initializeState()
    }

    override fun setInitialState() = EditorSelectorContentContract.State()

    override fun handleEvents(event: EditorSelectorContentContract.Event) {
        when(event) {
            EditorSelectorContentContract.Event.SelectorSaveToFile -> {
                // TODO Handle 04.01
            }

            is EditorSelectorContentContract.Event.EditSelectorContentText -> updateSelectorText(event.text)

            // Condition Holder Event
            EditorSelectorContentContract.Event.AddShowConditionClicked -> addShowCondition()
            is EditorSelectorContentContract.Event.ShowConditionHolderDeleteClicked -> deleteShowCondition(event.conditionId)
            is EditorSelectorContentContract.Event.ShowConditionHolderNavigateClicked -> navigateToEditCondition(event.conditionId)
            is EditorSelectorContentContract.Event.MoveShowConditionHolderPosition -> moveShowCondition(event.fromIndex, event.toIndex)

            // Route Holder Event
            EditorSelectorContentContract.Event.AddRouteClicked -> addRoute()
            is EditorSelectorContentContract.Event.RouteHolderDeleteClicked -> deleteRoute(event.routeId)
            is EditorSelectorContentContract.Event.RouteHolderNavigateClicked -> navigateToEditRoute(event.routeId)
            is EditorSelectorContentContract.Event.MoveRouteHolderPosition -> moveRoute(event.fromIndex, event.toIndex)
        }
    }

    override fun handleCommonEvents(event: CommonEvent) {
        when(event){
            is CommonEvent.OnResume -> handleOnResume()
            is CommonEvent.CloseEvent -> {
                // TODO Check edit 04.01
                super.handleCommonEvents(CommonEvent.CloseEvent)
            }
            else -> super.handleCommonEvents(event)
        }
    }

    private fun initializeState() {
        launchWithInit {
            updateSelectorAndStateList(pageId, selectorId)

            val bookTitle = requireNotNull(savedStateHandle.get<String>(NAME_BOOK_TITLE))
            val pageTitle = logic.logics.firstOrNull { it.pageId == pageId }?.title.orEmpty()
            val selectorText = originSelector.text

            setState {
                copy(
                    isInitSuccess = true,
                    bookTitle = bookTitle,
                    pageTitle = pageTitle,
                    selectorText = selectorText
                )
            }
        }
    }

    private fun updateSelectorText(text: String) {
        setState {
            copy(
                selectorText = text
            )
        }
    }

    // Condition Holder Event
    private fun addShowCondition() = launchWithLoading {
        // TODO 04.10
        val editedIndex = showConditionStates.size
        val conditionId = BookIDManager.generateId(showConditionStates.map { it.condition.conditionId })

        val condition = ConditionWrapper(
            conditionId = conditionId,
            conditionGroup = ConditionGroup.ShowSelectorCondition,
            selfActionInfo = ActionInfo.PageInfo(
                pageTitle = uiState.value.pageTitle,
                actionId = ActionIdModel(pageId = pageId)
            ),
            targetActionInfo = CountInfo(count = 0)
        )

        showConditionStates.add(ConditionState(editIndex = editedIndex, condition = condition))
    }

    private fun deleteShowCondition(conditionId: Long) {
        showConditionStates.removeIf {
            it.condition.conditionId == conditionId
        }
    }

    private fun navigateToEditCondition(conditionId: Long) {
        // TODO 04.10
        isUpdateResume = true
        setEffect {
            EditorSelectorContentContract.Effect.Navigation.NavigateEditorConditionScreen(
                episodeRef = episodeRef,
                bookTitle = uiState.value.bookTitle,
                pageTitle = uiState.value.pageTitle,
                pageId = pageId,
                selectorId = selectorId,
                conditionId = conditionId
            )
        }
    }

    private fun moveShowCondition(fromIndex: Int, toIndex: Int) {
        showConditionStates.apply {
            val item = removeAt(fromIndex)
            add(toIndex, item)
        }
    }

    // Route Holder Event
    private fun addRoute() = launchWithLoading {
        // TODO 05.02
        val editedIndex = routeStates.size
        val routeId = BookIDManager.generateId(routeStates.map { it.route.routeId })

        val route = RouteWrapper(
            routeId = routeId,
            targetPageId = ROUTE_PAGE_ID_NOT_ASSIGNED,
            targetPageTitle = "",
            routeConditionSize = 0
        )

        routeStates.add(RouteState(editIndex = editedIndex, route = route))
    }

    private fun deleteRoute(routeId: Long) {
        routeStates.removeIf {
            it.route.routeId == routeId
        }
    }

    private fun navigateToEditRoute(routeId: Long) {
        // TODO 05.02
        isUpdateResume = true
        setEffect {
            EditorSelectorContentContract.Effect.Navigation.NavigateEditorRouteScreen(
                episodeRef = episodeRef,
                bookTitle = uiState.value.bookTitle,
                pageTitle = uiState.value.pageTitle,
                pageId = pageId,
                selectorId = selectorId,
                routeId = routeId
            )
        }
    }

    private fun moveRoute(fromIndex: Int, toIndex: Int) {
        routeStates.apply {
            val item = removeAt(fromIndex)
            add(toIndex, item)
        }
    }

    // CommonEvent
    private suspend fun updateSelectorAndStateList(pageId: Long, selectorId: Long) {
        logic = useCaseLoadBook.loadLogic(episodeRef)
        originSelector = useCaseLoadBook.getSelector(logic, pageId, selectorId)

        showConditionStates.clear()
        originSelector.showConditions.forEachIndexed { index, condition ->
            showConditionStates.add(ConditionState(editIndex = index, condition = condition.toWrapper(logic)))
        }
        originSelector.routes.forEachIndexed { index, route ->
            routeStates.add(RouteState(editIndex = index, route = route.toWrapper(logic)))
        }
    }

    private fun handleOnResume() {
        if (isUpdateResume) {
            isUpdateResume = false
            // TODO Update 04.01
        }
    }
}
package com.fancymansion.presentation.editor.routeContent

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.SavedStateHandle
import com.fancymansion.core.common.const.ArgName.NAME_BOOK_ID
import com.fancymansion.core.common.const.ArgName.NAME_BOOK_TITLE
import com.fancymansion.core.common.const.ArgName.NAME_EPISODE_ID
import com.fancymansion.core.common.const.ArgName.NAME_PAGE_ID
import com.fancymansion.core.common.const.ArgName.NAME_READ_MODE
import com.fancymansion.core.common.const.ArgName.NAME_ROUTE_ID
import com.fancymansion.core.common.const.ArgName.NAME_SELECTOR_ID
import com.fancymansion.core.common.const.ArgName.NAME_USER_ID
import com.fancymansion.core.common.const.EpisodeRef
import com.fancymansion.core.common.const.ReadMode
import com.fancymansion.core.common.resource.StringValue
import com.fancymansion.core.common.util.BookIDManager
import com.fancymansion.core.presentation.base.BaseViewModel
import com.fancymansion.core.presentation.base.CommonEvent
import com.fancymansion.core.presentation.base.LoadState
import com.fancymansion.domain.model.book.ActionIdModel
import com.fancymansion.domain.model.book.ConditionModel
import com.fancymansion.domain.model.book.LogicModel
import com.fancymansion.domain.model.book.RouteModel
import com.fancymansion.domain.usecase.book.UseCaseLoadBook
import com.fancymansion.domain.usecase.book.UseCaseMakeBook
import com.fancymansion.domain.usecase.util.UseCaseGetResource
import com.fancymansion.presentation.editor.R
import com.fancymansion.presentation.editor.common.ActionInfo
import com.fancymansion.presentation.editor.common.ActionInfo.CountInfo
import com.fancymansion.presentation.editor.common.ConditionGroup
import com.fancymansion.presentation.editor.common.ConditionState
import com.fancymansion.presentation.editor.common.ConditionWrapper
import com.fancymansion.presentation.editor.common.TargetPageWrapper
import com.fancymansion.presentation.editor.common.toModel
import com.fancymansion.presentation.editor.common.toWrapper
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class EditorRouteContentViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val useCaseLoadBook: UseCaseLoadBook,
    private val useCaseMakeBook: UseCaseMakeBook,
    private val useCaseGetResource: UseCaseGetResource
) : BaseViewModel<EditorRouteContentContract.State, EditorRouteContentContract.Event, EditorRouteContentContract.Effect>() {

    val routeConditionStates: SnapshotStateList<ConditionState> = mutableStateListOf()

    private var isUpdateResume = false
    private val pageId: Long by lazy { requireNotNull(savedStateHandle.get<Long>(NAME_PAGE_ID)) }
    private val selectorId: Long by lazy { requireNotNull(savedStateHandle.get<Long>(NAME_SELECTOR_ID)) }
    private val routeId: Long by lazy { requireNotNull(savedStateHandle.get<Long>(NAME_ROUTE_ID)) }

    private val episodeRef: EpisodeRef = savedStateHandle.run {
        EpisodeRef(
            requireNotNull(get<String>(NAME_USER_ID)),
            requireNotNull(get<ReadMode>(NAME_READ_MODE)),
            requireNotNull(get<String>(NAME_BOOK_ID)),
            requireNotNull(get<String>(NAME_EPISODE_ID))
        )
    }
    private lateinit var logic: LogicModel
    private lateinit var originRoute: RouteModel

    init {
        initializeState()
    }

    override fun setInitialState() = EditorRouteContentContract.State()

    override fun handleEvents(event: EditorRouteContentContract.Event) {
        when(event) {
            EditorRouteContentContract.Event.RouteSaveToFile -> handleRouteSaveToFile()
            is EditorRouteContentContract.Event.SelectTargetPage -> selectPageId(event.pageId)
            EditorRouteContentContract.Event.ReadPagePreviewClicked -> handleReadPagePreview()

            // Condition Holder Event
            EditorRouteContentContract.Event.AddRouteConditionClicked -> addRouteCondition()
            is EditorRouteContentContract.Event.RouteConditionHolderDeleteClicked -> deleteRouteCondition(event.conditionId)
            is EditorRouteContentContract.Event.RouteConditionHolderNavigateClicked -> navigateToEditCondition(event.conditionId)
            is EditorRouteContentContract.Event.MoveRouteConditionHolderPosition -> moveRouteCondition(event.fromIndex, event.toIndex)
        }
    }

    override fun handleCommonEvents(event: CommonEvent) {
        when(event){
            is CommonEvent.OnResume -> handleOnResume()
            is CommonEvent.CloseEvent -> {
                if (uiState.value.isInitSuccess) {
                    checkRouteEdited {
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
            updateRouteAndStateList(pageId, selectorId, routeId)

            val bookTitle = requireNotNull(savedStateHandle.get<String>(NAME_BOOK_TITLE))
            val pageTitle = logic.logics.firstOrNull { it.pageId == pageId }?.title.orEmpty()
            val selectorText = logic.logics.firstOrNull { it.pageId == pageId }?.selectors?.firstOrNull { it.selectorId == selectorId }?.text.orEmpty()

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

    private fun handleRouteSaveToFile() = launchWithLoading(endLoadState = null) {
        val isComplete = saveEditedRouteAndReload()
        setLoadState(
            loadState = LoadState.AlarmDialog(
                title = StringValue.StringResource(com.fancymansion.core.common.R.string.book_file_save_result_title),
                message = StringValue.StringResource(if (isComplete) R.string.dialog_save_complete_book_info else R.string.dialog_save_fail_book_info),
                dismissText = null,
                confirmText = StringValue.StringResource(com.fancymansion.core.common.R.string.confirm),
                onConfirm = ::setLoadStateIdle
            )
        )
    }

    private fun handleReadPagePreview() {
        checkRouteEdited {
            setEffect {
                EditorRouteContentContract.Effect.Navigation.NavigateViewerContentScreen(
                    episodeRef = episodeRef,
                    bookTitle = uiState.value.bookTitle,
                    episodeTitle = "",
                    pageId = pageId
                )
            }
        }
    }

    // EditorRouteEvent
    private fun selectPageId(pageId : Long) {
        setState {
            copy(
                targetPage = targetPageList.firstOrNull { it.pageId == pageId }!!
            )
        }
    }

    // Condition Holder Event
    private fun addRouteCondition() = launchWithLoading {
        val editedIndex = routeConditionStates.size
        val conditionId = BookIDManager.generateId(routeConditionStates.map { it.condition.conditionId })

        val condition = ConditionWrapper(
            conditionId = conditionId,
            conditionGroup = ConditionGroup.RouteCondition(
                pageId = pageId,
                selectorId = selectorId,
                routeId = routeId,
            ),
            selfActionInfo = ActionInfo.PageInfo(
                pageTitle = uiState.value.pageTitle,
                actionId = ActionIdModel(pageId = pageId)
            ),
            targetActionInfo = CountInfo(count = 0)
        )

        routeConditionStates.add(ConditionState(editIndex = editedIndex, condition = condition))
    }

    private fun deleteRouteCondition(conditionId: Long) {
        routeConditionStates.removeIf {
            it.condition.conditionId == conditionId
        }
    }

    private fun navigateToEditCondition(conditionId: Long) {
        checkRouteEdited {
            isUpdateResume = true
            setEffect {
                EditorRouteContentContract.Effect.Navigation.NavigateEditorConditionScreen(
                    episodeRef = episodeRef,
                    bookTitle = uiState.value.bookTitle,
                    pageTitle = uiState.value.pageTitle,
                    pageId = pageId,
                    selectorId = selectorId,
                    routeId = routeId,
                    conditionId = conditionId
                )
            }
        }
    }

    private fun moveRouteCondition(fromIndex: Int, toIndex: Int) {
        routeConditionStates.apply {
            val item = removeAt(fromIndex)
            add(toIndex, item)
        }
    }

    // CommonEvent
    private suspend fun updateRouteAndStateList(pageId: Long, selectorId: Long, routeId: Long) {
        logic = useCaseLoadBook.loadLogic(episodeRef)
        originRoute = useCaseLoadBook.getRoute(logic, pageId, selectorId, routeId)

        routeConditionStates.clear()
        originRoute.routeConditions.forEachIndexed { index, condition ->
            routeConditionStates.add(ConditionState(editIndex = index, condition = condition.toWrapper(logic)))
        }

        val targetPageList = logic.logics.map {
            TargetPageWrapper(
                pageId = it.pageId,
                pageTitle = it.title
            )
        }

        setState {
            copy(
                targetPageList = targetPageList,
                targetPage = targetPageList.firstOrNull { it.pageId == originRoute.routeTargetPageId }!!
            )
        }
    }

    private suspend fun saveEditedRouteAndReload() : Boolean{
        val result = useCaseMakeBook.saveEditedRoute(
            episodeRef = episodeRef,
            pageId = pageId,
            selectorId = selectorId,
            route = originRoute.copy(
                routeTargetPageId = uiState.value.targetPage.pageId,
                routeConditions = routeConditionStates.map { it.condition.toModel() }.filterIsInstance<ConditionModel.RouteConditionModel>()
            )
        )

        updateRouteAndStateList(pageId, selectorId, routeId)
        return result
    }

    private fun handleOnResume() {
        if (isUpdateResume) {
            isUpdateResume = false
            launchWithLoading { updateRouteAndStateList(pageId, selectorId, routeId) }
        }
    }

    private fun checkRouteEdited(onCheckComplete: () -> Unit) {
        val newRoute = originRoute.copy(
            routeTargetPageId = uiState.value.targetPage.pageId,
            routeConditions = routeConditionStates.map { it.condition.toModel() }.filterIsInstance<ConditionModel.RouteConditionModel>()
        )

        if (originRoute != newRoute) {
            setLoadState(
                LoadState.AlarmDialog(
                    title = StringValue.StringResource(com.fancymansion.core.common.R.string.book_file_edited_info_title),
                    message = StringValue.StringResource(R.string.dialog_save_edited_book_info),
                    confirmText = StringValue.StringResource(com.fancymansion.core.common.R.string.confirm),
                    onConfirm = {
                        //수정 중인 정보 파일 저장
                        launchWithLoading {
                            saveEditedRouteAndReload()
                            setLoadStateIdle()
                            onCheckComplete()
                        }
                    },
                    dismissText = StringValue.StringResource(com.fancymansion.core.common.R.string.cancel),
                    onDismiss = {
                        //수정 중인 정보 삭제
                        launchWithLoading {
                            updateRouteAndStateList(pageId, selectorId, routeId)
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
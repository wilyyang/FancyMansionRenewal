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
import com.fancymansion.core.presentation.base.BaseViewModel
import com.fancymansion.domain.model.book.LogicModel
import com.fancymansion.domain.model.book.RouteModel
import com.fancymansion.domain.usecase.book.UseCaseLoadBook
import com.fancymansion.domain.usecase.book.UseCaseMakeBook
import com.fancymansion.domain.usecase.util.UseCaseGetResource
import com.fancymansion.presentation.editor.common.ConditionState
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
            is EditorRouteContentContract.Event.SelectTargetPage -> selectPageId(event.pageId)
            else -> {}
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

    // EditorRouteEvent
    private fun selectPageId(pageId : Long) {
        setState {
            copy(
                targetPage = targetPageList.firstOrNull { it.pageId == pageId }!!
            )
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
}
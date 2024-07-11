package com.fancymansion.domain.usecase.book

import com.fancymansion.core.common.const.EpisodeRef
import com.fancymansion.core.common.const.ConditionType
import com.fancymansion.core.common.const.LogicalOp
import com.fancymansion.core.common.di.DispatcherIO
import com.fancymansion.domain.interfaceRepository.BookLocalRepository
import com.fancymansion.domain.model.book.ActionIdModel
import com.fancymansion.domain.model.book.ConditionModel
import com.fancymansion.domain.model.book.LogicModel
import com.fancymansion.domain.model.book.RouteModel
import com.fancymansion.domain.model.book.SelectorModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject


class UseCaseBookLogic @Inject constructor(
    @DispatcherIO private val dispatcher: CoroutineDispatcher,
    private val bookLocalRepository: BookLocalRepository
) {
    suspend fun resetEpisodeData(episodeRef: EpisodeRef) =
        withContext(dispatcher)
        {
            bookLocalRepository.deleteReadingProgressByEpisode(episodeRef)
            bookLocalRepository.deleteActionCountByEpisode(episodeRef)
        }

    suspend fun updateReadingProgressPageId(episodeRef: EpisodeRef, newPageId: Long) =
        withContext(dispatcher)
        {
            val beforePageId = bookLocalRepository.getReadingProgressPageId(episodeRef)
            if(beforePageId == null){
                bookLocalRepository.insertReadingProgress(episodeRef, newPageId)
            }else{
                bookLocalRepository.updateReadingProgressPageId(episodeRef, newPageId)
            }
        }

    suspend fun getReadingProgressPageId(episodeRef: EpisodeRef) =
        withContext(dispatcher)
        {
            bookLocalRepository.getReadingProgressPageId(episodeRef)
        }

    suspend fun getVisibleSelectors(
        episodeRef: EpisodeRef,
        logic: LogicModel,
        pageId: Long
    ): List<SelectorModel> = withContext(dispatcher) {
        logic.logics.first { it.pageId == pageId }.selectors.let { selectors ->
            selectors.filter { selector -> checkConditions(episodeRef = episodeRef, conditions = selector.showConditions) }
        }
    }

    suspend fun incrementActionCount(episodeRef: EpisodeRef, pageId: Long) =
        incrementActionCount(episodeRef, ActionIdModel(pageId = pageId, pageId))

    suspend fun incrementActionCount(episodeRef: EpisodeRef, pageId: Long, selectorId: Long) =
        incrementActionCount(episodeRef, ActionIdModel(pageId = pageId, selectorId = selectorId))

    suspend fun incrementActionCount(episodeRef: EpisodeRef, actionId: ActionIdModel) =
        withContext(dispatcher) {
            val count = bookLocalRepository.getActionCount(episodeRef, actionId)

            if(count != null){
                bookLocalRepository.updateActionCount(episodeRef, actionId, count + 1)
            }else{
                bookLocalRepository.insertActionCount(episodeRef, actionId)
            }
        }

    suspend fun getNextRoutePageId(
        episodeRef: EpisodeRef,
        routes: List<RouteModel>
    ): Long = withContext(dispatcher) {
        routes.first { route -> checkConditions(episodeRef = episodeRef, conditions = route.routeConditions) }.routeTargetPageId
    }

    private suspend fun checkConditions(
        episodeRef: EpisodeRef,
        conditions: List<ConditionModel>
    ): Boolean {
        var result = true
        var logicalOp = LogicalOp.AND
        for (condition in conditions) {
            // 서브 조건을 검사 한다
            val conditionResult = checkCondition(episodeRef = episodeRef, condition = condition)
            // 이전 결과와 논리 결과를 도출 한다
            result = logicalOp.check(result, conditionResult)
            // 서브 조건으로부터 논리 연산자를 가져 온다
            logicalOp = condition.logicalOp
            // 관계 결과가 true 이고 다음 관계가 OR인 경우 최종 반환 한다
            if (result && logicalOp == LogicalOp.OR) break
        }
        return result
    }

    private suspend fun checkCondition(
        episodeRef: EpisodeRef,
        condition: ConditionModel
    ): Boolean {
        val selfActionCount = bookLocalRepository.getActionCount(episodeRef, actionId = condition.selfActionId)?:0
        val targetCount = when (condition.type) {
            ConditionType.COUNT -> {
                condition.count
            }

            ConditionType.TARGET_VIEWS -> {
                bookLocalRepository.getActionCount(episodeRef, actionId = condition.targetActionId)
            }
        }?:0
        return condition.relationOp.check(selfActionCount, targetCount)
    }
}
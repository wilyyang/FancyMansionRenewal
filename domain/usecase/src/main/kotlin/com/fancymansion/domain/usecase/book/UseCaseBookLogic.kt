package com.fancymansion.domain.usecase.book

import com.fancymansion.core.common.const.BookRef
import com.fancymansion.core.common.const.ConditionType
import com.fancymansion.core.common.const.LogicalOp
import com.fancymansion.core.common.di.DispatcherIO
import com.fancymansion.domain.interfaceRepository.BookLocalRepository
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
    suspend fun resetBookData(bookRef: BookRef) =
        withContext(dispatcher)
        {
            bookLocalRepository.deleteReadingProgressByBook(bookRef)
            bookLocalRepository.deleteActionCountByBook(bookRef)
        }

    suspend fun updateReadingProgressPageId(bookRef: BookRef, newPageId: Long) =
        withContext(dispatcher)
        {
            val beforePageId = bookLocalRepository.getReadingProgressPageId(bookRef)
            if(beforePageId == null){
                bookLocalRepository.insertReadingProgress(bookRef, newPageId)
            }else{
                bookLocalRepository.updateReadingProgressPageId(bookRef, newPageId)
            }
        }

    suspend fun getReadingProgressPageId(bookRef: BookRef) =
        withContext(dispatcher)
        {
            bookLocalRepository.getReadingProgressPageId(bookRef)
        }

    suspend fun getVisibleSelectors(
        bookRef: BookRef,
        logic: LogicModel,
        pageId: Long
    ): List<SelectorModel> = withContext(dispatcher) {
        logic.logics.first { it.id == pageId }.selectors.let {selectors ->
            selectors.filter { selector -> checkConditions(bookRef = bookRef, conditions = selector.showConditions) }
        }
    }

    suspend fun incrementActionCount(bookRef: BookRef, actionId: Long) =
        withContext(dispatcher) {
            val count = bookLocalRepository.getActionCount(bookRef, actionId)

            if(count != null){
                bookLocalRepository.updateActionCount(bookRef, actionId, count + 1)
            }else{
                bookLocalRepository.insertActionCount(bookRef, actionId)
            }
        }

    suspend fun getNextRoutePageId(
        bookRef: BookRef,
        routes: List<RouteModel>
    ): Long = withContext(dispatcher) {
        routes.first { route -> checkConditions(bookRef = bookRef, conditions = route.routeConditions) }.routePageId
    }

    private suspend fun checkConditions(
        bookRef: BookRef,
        conditions: List<ConditionModel>
    ): Boolean {
        var result = true
        var logicalOp = LogicalOp.AND
        for (condition in conditions) {
            // 서브 조건을 검사 한다
            val conditionResult = checkCondition(bookRef = bookRef, condition = condition)
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
        bookRef: BookRef,
        condition: ConditionModel
    ): Boolean {
        val selfActionCount = bookLocalRepository.getActionCount(bookRef, actionId = condition.selfActionId)?:0
        val targetCount = when (condition.type) {
            ConditionType.COUNT -> {
                condition.count
            }

            ConditionType.TARGET_VIEWS -> {
                bookLocalRepository.getActionCount(bookRef, actionId = condition.targetActionId)
            }
        }?:0
        return condition.relationOp.check(selfActionCount, targetCount)
    }
}
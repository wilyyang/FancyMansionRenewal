package com.fancymansion.domain.usecase.book

import com.fancymansion.core.common.const.BookRef
import com.fancymansion.core.common.const.ConditionType
import com.fancymansion.core.common.const.LogicalOp
import com.fancymansion.core.common.di.DispatcherIO
import com.fancymansion.domain.interfaceRepository.BookLocalRepository
import com.fancymansion.domain.model.book.Condition
import com.fancymansion.domain.model.book.Logic
import com.fancymansion.domain.model.book.Route
import com.fancymansion.domain.model.book.Selector
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject


class UseCaseBookLogic @Inject constructor(
    @DispatcherIO private val dispatcher: CoroutineDispatcher,
    private val bookLocalRepository: BookLocalRepository
) {
    suspend fun resetBookCount(bookRef: BookRef) =
        withContext(dispatcher)
        {
            bookLocalRepository.deleteBookActionCount(bookRef)
        }

    suspend fun getVisibleSelectors(
        bookRef: BookRef,
        logic: Logic,
        pageId: Long
    ): List<Selector> = withContext(dispatcher) {
        logic.logics.first { it.id == pageId }.selectors.let {selectors ->
            selectors.filter { selector -> checkConditions(bookRef = bookRef, conditions = selector.showConditions) }
        }
    }

    suspend fun incrementCount(bookRef: BookRef, countId: Long) =
        withContext(dispatcher) {
            bookLocalRepository.incrementActionCount(bookRef, countId)
        }

    suspend fun getNextRoutePageId(
        bookRef: BookRef,
        routes: List<Route>
    ): Long = withContext(dispatcher) {
        routes.first { route -> checkConditions(bookRef = bookRef, conditions = route.routeConditions) }.routePageId
    }

    private suspend fun checkConditions(
        bookRef: BookRef,
        conditions: List<Condition>
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
        condition: Condition
    ): Boolean {
        val selfCount = bookLocalRepository.getActionCount(bookRef, actionId = condition.selfViewsId)
        val targetCount = when (condition.type) {
            ConditionType.COUNT -> {
                condition.count
            }

            ConditionType.TARGET_VIEWS -> {
                bookLocalRepository.getActionCount(bookRef, actionId = condition.targetViewsId)
            }
        }
        return condition.relationOp.check(selfCount, targetCount)
    }
}
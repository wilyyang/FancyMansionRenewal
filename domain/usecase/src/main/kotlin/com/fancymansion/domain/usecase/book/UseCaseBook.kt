package com.fancymansion.domain.usecase.book

import com.fancymansion.core.common.const.ConditionType
import com.fancymansion.core.common.const.LogicalOp
import com.fancymansion.core.common.const.ReadMode
import com.fancymansion.core.common.di.DispatcherIO
import com.fancymansion.domain.model.book.Condition
import com.fancymansion.domain.model.book.Logic
import com.fancymansion.domain.model.book.Page
import com.fancymansion.domain.model.book.Route
import com.fancymansion.domain.model.book.Selector
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class UseCaseBook @Inject constructor(
    @DispatcherIO private val dispatcher: CoroutineDispatcher
) {
    private var countMap = mutableMapOf<Long, Int>()
    fun loadLogic(userId: String, mode: ReadMode, bookId: String): Logic = logic

    fun resetCount(userId: String, mode: ReadMode, bookId: String) {
        countMap = mutableMapOf<Long, Int>()
    }

    fun loadPage(userId: String, mode: ReadMode, bookId: String, pageId: Long): Page =
        content.pages.first { it.id == pageId }

    fun getVisibleSelectors(
        userId: String,
        mode: ReadMode,
        bookId: String,
        logic: Logic,
        pageId: Long
    ): List<Selector> =
        getVisibleSelectors(userId, mode, bookId, logic.logics.first { it.id == pageId }.selectors)


    fun getVisibleSelectors(
        userId: String,
        mode: ReadMode,
        bookId: String,
        selectors: List<Selector>
    ): List<Selector> =
        selectors.filter { selector -> checkConditions(selector.showConditions, countMap) }

    fun incrementCount(userId: String, mode: ReadMode, bookId: String, countId: Long) {
        if(countMap[countId] != null){
            countMap[countId] = countMap[countId]!! + 1
        }else{
            countMap[countId] = 1
        }
    }

    fun getNextPageId(userId: String, mode: ReadMode, bookId: String, routes: List<Route>): Long {
        return routes.first { route -> checkConditions(route.routeConditions, countMap) }.routePageId
    }

    private fun checkConditions(
        conditions: List<Condition>,
        countMap: MutableMap<Long, Int>
    ): Boolean {
        var result = true
        var logicalOp = LogicalOp.AND
        for (condition in conditions) {
            // 서브 조건을 검사 한다
            val conditionResult = checkCondition(condition, countMap)
            // 이전 결과와 논리 결과를 도출 한다
            result = logicalOp.check(result, conditionResult)
            // 서브 조건으로부터 논리 연산자를 가져 온다
            logicalOp = condition.logicalOp
            // 관계 결과가 true 이고 다음 관계가 OR인 경우 최종 반환 한다
            if (result && logicalOp == LogicalOp.OR) break
        }
        return result
    }

    private fun checkCondition(condition: Condition, countMap: MutableMap<Long, Int>): Boolean {
        val selfCount = countMap[condition.selfViewsId] ?: 0
        val targetCount = when (condition.type) {
            ConditionType.COUNT -> {
                condition.count
            }

            ConditionType.TARGET_VIEWS -> {
                countMap[condition.targetViewsId]
            }
        } ?: 0
        return condition.relationOp.check(selfCount, targetCount)
    }
}
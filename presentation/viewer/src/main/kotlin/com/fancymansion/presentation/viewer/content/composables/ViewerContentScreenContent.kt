package com.fancymansion.presentation.viewer.content.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fancymansion.core.common.const.ConditionType
import com.fancymansion.core.common.const.LogicalOp
import com.fancymansion.core.common.const.MOBILE_PREVIEW_SPEC
import com.fancymansion.core.common.log.Logger
import com.fancymansion.core.presentation.theme.ColorSet
import com.fancymansion.core.presentation.theme.FancyMansionTheme
import com.fancymansion.domain.model.book.Condition
import com.fancymansion.domain.model.book.Selector
import com.fancymansion.domain.model.book.Source
import com.fancymansion.presentation.viewer.content.ViewerContentContract
import com.fancymansion.presentation.viewer.content.content
import com.fancymansion.presentation.viewer.content.logic

@Composable
fun ViewerContentScreenContent(
    modifier: Modifier = Modifier,
    uiState: ViewerContentContract.State,
    onEventSent: (event: ViewerContentContract.Event) -> Unit
) {
    val countMap = remember {
        mutableMapOf<Long, Int>()
    }
    val idx = remember {
        mutableIntStateOf(0)
    }

    val onClickSelector : (Selector) -> Unit = { selector ->
        if(countMap[selector.id] != null){
            countMap[selector.id] = countMap[selector.id]!! + 1
        }else{
            countMap[selector.id] = 1
        }
        Logger.e("FancyMansion Selector count : ${selector.id} ${countMap[selector.id]}")

        selector.routes.first { route -> checkConditions(route.routeConditions, countMap) }.routePageId.let { nextRouteId ->
            if(countMap[nextRouteId] != null){
                countMap[nextRouteId] = countMap[nextRouteId]!! + 1
            }else{
                countMap[nextRouteId] = 1
            }
            Logger.e("FancyMansion Page count : $nextRouteId ${countMap[nextRouteId]}")

            val index = content.pages.indexOfFirst { it.id == nextRouteId }
            idx.intValue  = index
        }
    }
    LaunchedEffect(key1 = Unit) {
        countMap[content.pages[0].id] = 1
    }

    ViewerContentScreenPageContent(idx.intValue, onClickSelector)
}


@Composable
fun ViewerContentScreenPageContent(idx: Int, onClickSelector : (Selector)-> Unit) {
    val contentTextStyle = MaterialTheme.typography.bodyLarge.copy(fontSize = 18.sp, lineHeight = 18.sp * 1.2)
    val listState = rememberLazyListState()

    LaunchedEffect(key1 = idx) {
        listState.scrollToItem(0)
    }

    LazyColumn(
        state = listState
    ) {
        item {
            Text(
                modifier = Modifier
                    .padding(vertical = 20.dp)
                    .padding(horizontal = 5.dp),
                text = "[" +logic.logics[idx].type + "] "+content.pages[idx].title, style = MaterialTheme.typography.titleLarge
            )
        }
        items(content.pages[idx].sources) {
            when (it) {
                is Source.Text -> {
                    Text(modifier = Modifier.padding(horizontal = 5.dp), text = it.description, style = contentTextStyle)
                }

                is Source.Image -> {
                    Image(modifier = Modifier.fillMaxWidth(), contentScale = ContentScale.FillWidth, painter = painterResource(id = it.resId), contentDescription = "")
                }
            }

        }
        item {
            Spacer(modifier = Modifier.height(20.dp))
        }
        items(logic.logics[idx].selectors) { selector ->
            Row(
                modifier = Modifier
                    .padding(vertical = 10.dp, horizontal = 15.dp)
                    .fillMaxWidth()
                    .clip(
                        shape = MaterialTheme.shapes.small
                    )
                    .background(color = ColorSet.sky_c1ebfe)
                    .clickable {
                        onClickSelector(selector)
                    }
                    .padding(horizontal = 15.dp, vertical = 10.dp)
            ) {
                Text(text = selector.text, style = contentTextStyle)
            }
        }
        item {
            Spacer(modifier = Modifier.height(20.dp))
        }
    }

}

fun checkConditions(conditions: List<Condition>, countMap: MutableMap<Long, Int>) : Boolean{
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

fun checkCondition(condition: Condition, countMap: MutableMap<Long, Int>) : Boolean{
    val selfCount = countMap[condition.selfViewsId]?:0
    val targetCount = when(condition.type){
        ConditionType.COUNT -> {
            condition.count
        }
        ConditionType.TARGET_VIEWS -> {
            countMap[condition.targetViewsId]
        }
    }?:0
    Logger.e("FancyMansion check : $selfCount $targetCount")
    return condition.relationOp.check(selfCount, targetCount)
}


@Preview(device = MOBILE_PREVIEW_SPEC)
@Composable
fun ViewerContentScreenPageContentPreview() {
    FancyMansionTheme {

    }
}
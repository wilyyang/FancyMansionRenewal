package com.fancymansion.presentation.viewer.content.composables.layer

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.fancymansion.core.common.const.MOBILE_PREVIEW_SPEC
import com.fancymansion.core.common.const.PageTextSize
import com.fancymansion.core.presentation.base.clickSingle
import com.fancymansion.core.presentation.base.scaleOnPress
import com.fancymansion.core.presentation.frame.topBarDpMobile
import com.fancymansion.core.presentation.theme.FancyMansionTheme
import com.fancymansion.core.presentation.theme.onSurfaceDimmed
import com.fancymansion.core.presentation.util.borderLine
import com.fancymansion.domain.model.book.PageSettingModel
import com.fancymansion.presentation.viewer.content.ViewerContentContract
import com.fancymansion.presentation.viewer.content.composables.SettingCategory
import com.fancymansion.presentation.viewer.content.composables.SettingItem

@Composable
fun ViewerContentSettingPanel(
    modifier: Modifier,
    visible: Boolean,
    title: String,
    setting: PageSettingModel,
    settingItems: List<SettingCategory>,
    onEventSent: (event: ViewerContentContract.Event) -> Unit,
    onClickBack:() -> Unit
) {
    val backInteractionSource = remember { MutableInteractionSource() }
    Box(modifier = modifier) {
        AnimatedVisibility(
            modifier = Modifier.align(Alignment.TopStart),
            visible = visible,
            enter = slideInVertically { -it },
            exit = slideOutVertically { -it },
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(topBarDpMobile)
                    .background(MaterialTheme.colorScheme.surface)
                    .borderLine(
                        density = LocalDensity.current,
                        color = MaterialTheme.colorScheme.outline,
                        bottom = 1.dp
                    )
                    .clickSingle { }
                ) {

                Row(
                    modifier = Modifier
                        .fillMaxHeight()
                        .align(Alignment.CenterStart)
                        .padding(vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        modifier = Modifier
                            .fillMaxHeight()
                            .clickSingle(
                                onClick = onClickBack,
                                interactionSource = backInteractionSource
                            )
                            .scaleOnPress(
                                interactionSource = backInteractionSource,
                                pressScale = 0.9f
                            ),
                        painter = painterResource(id = com.fancymansion.core.presentation.R.drawable.ic_back),
                        tint = onSurfaceDimmed,
                        contentDescription = "Back"
                    )
                    Text(
                        modifier = Modifier.padding(horizontal = 5.dp),
                        text = title,
                        style = MaterialTheme.typography.titleLarge.copy(color = onSurfaceDimmed),
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 1
                    )
                }
            }
        }


        AnimatedVisibility(
            modifier = Modifier
                .align(Alignment.BottomStart),
            visible = visible,
            enter = slideInVertically { it },
            exit = slideOutVertically { it },
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
                    .background(MaterialTheme.colorScheme.surface)
                    .borderLine(
                        density = LocalDensity.current,
                        color = MaterialTheme.colorScheme.outline,
                        top = 1.dp
                    )
                    .clickSingle { }) {
                SettingFullList(
                    setting,
                    settingItems
                )
            }
        }
    }
}

@Composable
fun SettingFullList(
    setting: PageSettingModel,
    settingItems: List<SettingCategory>
) {

    val settingValueList = listOf(PageTextSize.values.indexOf(setting.pageContentSetting.textSize))

    LazyColumn {
        items(settingItems) { category ->
            SettingCategoryList(category, settingValueList)
        }
    }
}

@Composable
fun SettingCategoryList(category: SettingCategory, settingValueList: List<Int>) {
    Column(modifier = Modifier
        .fillMaxWidth()
        .background(MaterialTheme.colorScheme.background)) {
        Text(
            text = stringResource(id = category.categoryName),
            modifier = Modifier.padding(5.dp),
            style = MaterialTheme.typography.labelMedium.copy(color = MaterialTheme.colorScheme.onBackground)
        )
        category.items.forEach { item ->
            SettingItemRow(item, settingValueList[0])
        }
    }
}

@Composable
fun SettingItemRow(item: SettingItem, index: Int) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = MaterialTheme.colorScheme.surface)
            .borderLine(
                density = LocalDensity.current,
                color = MaterialTheme.colorScheme.outline,
                bottom = 1.dp
            )
            .padding(5.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            modifier = Modifier.size(25.dp),
            painter = painterResource(id = item.icon),
            contentDescription = ""
        )
        Text(modifier = Modifier
            .padding(start = 10.dp)
            .weight(1f), text = stringResource(id = item.title))
        SettingCounter(count = index, onClickPlus = {item.onClickPlus()}, onClickMinus = {item.onClickMinus()})
    }
}

@Composable
fun SettingCounter(modifier : Modifier = Modifier, count: Int, onClickPlus: () -> Unit, onClickMinus: () -> Unit) {
    Row(modifier = modifier, verticalAlignment = Alignment.CenterVertically) {
        Text(modifier = Modifier.padding(horizontal = 10.dp), text = "$count")

        Row(
            modifier = Modifier
                .border(1.dp, MaterialTheme.colorScheme.primary, MaterialTheme.shapes.large)
                .height(IntrinsicSize.Max),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Filled.KeyboardArrowDown,
                contentDescription = "Minus",
                modifier = Modifier
                    .size(30.dp)
                    .padding(4.dp)
                    .clickSingle { onClickMinus() },
                tint = MaterialTheme.colorScheme.primary
            )

            Box(modifier = Modifier
                .width(1.dp)
                .fillMaxHeight()
                .background(color = MaterialTheme.colorScheme.primary))

            Icon(
                imageVector = Icons.Filled.KeyboardArrowUp,
                contentDescription = "Plus",
                modifier = Modifier
                    .size(30.dp)
                    .padding(4.dp)
                    .clickSingle { onClickPlus() },
                tint = MaterialTheme.colorScheme.primary
            )
        }
    }
}
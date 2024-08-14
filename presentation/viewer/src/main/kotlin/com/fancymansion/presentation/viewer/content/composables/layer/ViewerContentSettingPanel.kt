package com.fancymansion.presentation.viewer.content.composables.layer

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.fancymansion.core.common.const.PageTheme
import com.fancymansion.core.presentation.base.clickSingle
import com.fancymansion.core.presentation.base.scaleOnPress
import com.fancymansion.core.presentation.frame.topBarDpMobile
import com.fancymansion.core.presentation.theme.onSurfaceDimmed
import com.fancymansion.core.presentation.util.RoundedHorizontalCornerShape
import com.fancymansion.core.presentation.util.borderLine
import com.fancymansion.presentation.viewer.R
import com.fancymansion.presentation.viewer.content.composables.SettingCategory
import com.fancymansion.presentation.viewer.content.composables.SettingItem
import com.fancymansion.presentation.viewer.content.composables.SettingUiValue

@Composable
fun ViewerContentSettingPanel(
    modifier: Modifier,
    visible: Boolean,
    title: String,
    currentTheme: PageTheme,
    onClickTheme: (PageTheme) -> Unit,
    settingItems: List<SettingCategory>,
    settingTotalValues: List<List<SettingUiValue>>,
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
                    .heightIn(min = 100.dp, max = 400.dp)
                    .clickSingle { }) {
                SettingTotalList(
                    currentTheme = currentTheme,
                    onClickTheme = onClickTheme,
                    settingItems = settingItems,
                    settingTotalValues = settingTotalValues
                )
            }
        }
    }
}

@Composable
fun SettingTotalList(
    currentTheme: PageTheme = PageTheme.THEME_IVORY,
    onClickTheme: (PageTheme) -> Unit,
    settingItems: List<SettingCategory>,
    settingTotalValues: List<List<SettingUiValue>>,
) {

    LazyColumn {
        item {
            SettingPageTheme(currentTheme = currentTheme, onClickTheme = onClickTheme)
        }
        itemsIndexed(settingItems){ idx, category ->
            SettingCategoryList(category = category, settingValues = settingTotalValues[idx])
        }
    }
}

@Composable
fun SettingPageTheme(currentTheme: PageTheme, onClickTheme: (PageTheme) -> Unit){
    Column(modifier = Modifier
        .fillMaxWidth()
        .background(MaterialTheme.colorScheme.background)) {

        Text(
            text = stringResource(id = R.string.viewer_setting_theme),
            modifier = Modifier.padding(vertical = 5.dp, horizontal = 10.dp),
            style = MaterialTheme.typography.labelMedium.copy(color = MaterialTheme.colorScheme.onBackground)
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = MaterialTheme.colorScheme.surface)
                .borderLine(
                    density = LocalDensity.current,
                    color = MaterialTheme.colorScheme.outline,
                    bottom = 1.dp
                )
                .padding(vertical = 5.dp, horizontal = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                modifier = Modifier.size(25.dp),
                painter = painterResource(id = R.drawable.ic_page_theme),
                contentDescription = ""
            )

            LazyRow(modifier = Modifier.padding(start = 10.dp)){
                items(PageTheme.values){ theme ->
                    Box(
                        modifier = Modifier
                            .padding(5.dp)
                            .size(35.dp)
                            .clip(shape = CircleShape)
                            .border(
                                width = 1.dp,
                                color = if(theme == currentTheme) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline,
                                shape = CircleShape
                            )
                            .background(color = Color(theme.pageColor.colorCode))
                            .clickSingle(
                                enabled = theme != currentTheme,
                                clickInterval = 300
                            ) {
                                onClickTheme(theme)
                            }
                    )
                }
            }
        }
    }
}

@Composable
fun SettingCategoryList(category: SettingCategory, settingValues: List<SettingUiValue>){
    Column(modifier = Modifier
        .fillMaxWidth()
        .background(MaterialTheme.colorScheme.background)) {
        Text(
            text = stringResource(id = category.categoryName),
            modifier = Modifier.padding(vertical = 5.dp, horizontal = 10.dp),
            style = MaterialTheme.typography.labelMedium.copy(color = MaterialTheme.colorScheme.onBackground)
        )
        category.items.forEachIndexed { index, item ->
            SettingItemRow(item = item, value = settingValues[index])
        }
    }
}

@Composable
fun SettingItemRow(item: SettingItem, value: SettingUiValue) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = MaterialTheme.colorScheme.surface)
            .borderLine(
                density = LocalDensity.current,
                color = MaterialTheme.colorScheme.outline,
                bottom = 1.dp
            )
            .padding(vertical = 5.dp, horizontal = 10.dp),
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
        SettingCounter(value = value, onClickPlus = {item.onClickPlus()}, onClickMinus = {item.onClickMinus()})
    }
}

@Composable
fun SettingCounter(modifier : Modifier = Modifier, value: SettingUiValue, onClickPlus: () -> Unit, onClickMinus: () -> Unit) {
    Row(modifier = modifier, verticalAlignment = Alignment.CenterVertically) {
        Text(
            modifier = Modifier.padding(horizontal = 10.dp),
            text = value.order,
            style = MaterialTheme.typography.labelLarge.copy(
                color = onSurfaceDimmed,
                fontWeight = FontWeight.SemiBold
            )
        )

        Row(
            modifier = Modifier.height(IntrinsicSize.Max),
            verticalAlignment = Alignment.CenterVertically
        ) {
            val plusColor  = if (value.isMax) MaterialTheme.colorScheme.outline else MaterialTheme.colorScheme.primary
            val minusColor = if (value.isMin) MaterialTheme.colorScheme.outline else MaterialTheme.colorScheme.primary
            val roundDp = 30.dp

            Icon(
                painter = painterResource(id = R.drawable.ic_minus),
                contentDescription = "Minus",
                modifier = Modifier
                    .size(30.dp)
                    .clip(shape = RoundedHorizontalCornerShape(cornerRadius = roundDp))
                    .border(
                        1.dp,
                        minusColor,
                        shape = RoundedHorizontalCornerShape(cornerRadius = roundDp)
                    )
                    .clickSingle(
                        enabled = !value.isMin,
                        indication = LocalIndication.current,
                        clickInterval = 200
                    ) { onClickMinus() }
                    .padding(start = 6.dp, end = 4.dp, top = 4.dp, bottom = 4.dp),
                tint = minusColor
            )

            Box(modifier = Modifier.width(1.dp).fillMaxHeight().background(color = MaterialTheme.colorScheme.primary))

            Icon(
                painter = painterResource(id = R.drawable.ic_plus),
                contentDescription = "Plus",
                modifier = Modifier
                    .size(30.dp)
                    .clip(shape = RoundedHorizontalCornerShape(cornerRadius = roundDp, isLeftDirection = false))
                    .border(
                        1.dp,
                        plusColor,
                        shape = RoundedHorizontalCornerShape(cornerRadius = roundDp,isLeftDirection = false)
                    )
                    .clickSingle(
                        enabled = !value.isMax,
                        indication = LocalIndication.current,
                        clickInterval = 200
                    ) { onClickPlus() }
                    .padding(start = 4.dp, end = 6.dp, top = 4.dp, bottom = 4.dp),
                tint = plusColor
            )
        }
    }
}
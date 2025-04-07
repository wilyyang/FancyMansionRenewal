package com.fancymansion.presentation.editor.common.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontStyle.Companion.Italic
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fancymansion.core.presentation.compose.shape.borderLine
import com.fancymansion.core.presentation.compose.theme.ColorSet
import com.fancymansion.core.presentation.compose.theme.Paddings
import com.fancymansion.core.presentation.compose.theme.onSurfaceSub
import com.fancymansion.presentation.editor.R
import com.fancymansion.presentation.editor.common.ActionInfo
import com.fancymansion.presentation.editor.common.ConditionGroup
import com.fancymansion.presentation.editor.common.ConditionState
import com.fancymansion.presentation.editor.common.ConditionWrapper

@Composable
fun CommonEditInfoTitle(
    modifier: Modifier = Modifier,
    verticalPadding : Dp = Paddings.Basic.vertical,
    title: String,
    style : TextStyle = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold)
){
    Text(
        modifier = modifier.padding(vertical = verticalPadding),
        text = title,
        style = style
    )
}

@Composable
fun ConditionHolder(
    modifier: Modifier = Modifier,
    state: ConditionState,
    onConditionHolderClicked : (Long) -> Unit
){
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(85.dp)
            .background(color = MaterialTheme.colorScheme.surface)
            .clickable {
                onConditionHolderClicked(state.condition.conditionId)
            }
            .padding(horizontal = 8.dp)
            .borderLine(
                density = LocalDensity.current,
                color = onSurfaceSub,
                bottom = 0.3.dp
            )
            .padding(horizontal = 10.dp)
    ) {
        Column(modifier = Modifier
            .align(Alignment.CenterStart)
            .fillMaxWidth(0.75f)) {

            Text(
                text = stringResource(
                    id = when (state.condition.conditionGroup) {
                        ConditionGroup.ShowSelectorCondition -> R.string.edit_condition_holder_show_condition_number
                        ConditionGroup.RouteCondition -> R.string.edit_condition_holder_route_condition_number
                    }, state.condition.conditionId
                ),
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(5.dp))

            StyledConditionText(conditionWrapper = state.condition)

            Spacer(modifier = Modifier.height(3.dp))
        }

        Box(
            modifier = Modifier.align(Alignment.CenterEnd)
        ){
            Text(
                modifier = Modifier
                    .clip(shape = MaterialTheme.shapes.large)
                    .padding(0.5.dp)
                    .border(
                        width = 0.5.dp,
                        color = onSurfaceSub,
                        shape = MaterialTheme.shapes.large
                    )
                    .padding(horizontal = 6.dp, vertical = 4.dp),
                text = "삭제",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
fun StyledConditionText(
    conditionWrapper: ConditionWrapper
) {
    val styleTotal = SpanStyle(
        color = MaterialTheme.colorScheme.onSurface,
        fontSize = MaterialTheme.typography.bodySmall.fontSize
    )
    val styleRelation = SpanStyle(
        color = ColorSet.red_dc3232,
        fontSize = MaterialTheme.typography.bodyMedium.fontSize
    )

    val styleLogical = SpanStyle(
        color = MaterialTheme.colorScheme.onSurface,
        fontSize = MaterialTheme.typography.bodyMedium.fontSize,
        fontStyle = Italic
    )

    val partSelf = buildStyledActionText(
        conditionWrapper.selfActionInfo, styleMain = SpanStyle(
            color = MaterialTheme.colorScheme.primary,
            fontSize = MaterialTheme.typography.bodyMedium.fontSize,
            fontWeight = FontWeight.Bold
        )
    )
    val partTarget = buildStyledActionText(
        conditionWrapper.targetActionInfo, styleMain = SpanStyle(
            color = MaterialTheme.colorScheme.tertiary,
            fontSize = MaterialTheme.typography.bodyMedium.fontSize,
            fontWeight = FontWeight.Bold
        )
    )
    val partLogicOp = buildAnnotatedString {
        withStyle(styleRelation) {
            append(stringResource(conditionWrapper.relationOp.localizedName.resId))
        }

        withStyle(styleLogical) {
            append(" ${stringResource(conditionWrapper.logicalOp.localizedName.resId)}")
        }
    }

    val conditionTemplate = stringResource(R.string.condition_holder_text_format)
    val rawParts = conditionTemplate.split("%1\$s", "%2\$s", "%3\$s")

    val annotatedText = buildAnnotatedString {
        withStyle(styleTotal) {
            append(rawParts[0])
        }
        append(partSelf)
        withStyle(styleTotal) {
            append(rawParts[1])
        }
        append(partTarget)
        withStyle(styleTotal) {
            append(rawParts[2])
        }
        append(partLogicOp)
    }

    Text(text = annotatedText)
}

@Composable
fun buildStyledActionText(
    actionInfo: ActionInfo,
    styleBase: SpanStyle = SpanStyle(
        color = MaterialTheme.colorScheme.onSurface,
        fontSize = MaterialTheme.typography.bodyMedium.fontSize
    ),
    styleMain: SpanStyle = SpanStyle(
        color = MaterialTheme.colorScheme.onSurface,
        fontSize = MaterialTheme.typography.bodyMedium.fontSize,
        fontWeight = FontWeight.Bold
    )
): AnnotatedString {

    return buildAnnotatedString {
        when (actionInfo) {
            is ActionInfo.PageInfo -> {
                val formatted =
                    stringResource(R.string.condition_holder_text_page_unit, actionInfo.pageTitle)
                withStyle(styleBase) {
                    val prefix = formatted.substringBefore(actionInfo.pageTitle)
                    val suffix = formatted.substringAfter(actionInfo.pageTitle)
                    append(prefix)
                    withStyle(styleMain) { append(actionInfo.pageTitle) }
                    append(suffix)
                }
            }

            is ActionInfo.SelectorInfo -> {
                val formatted = stringResource(
                    R.string.condition_holder_text_selector_unit,
                    actionInfo.pageTitle,
                    actionInfo.selectorText
                )
                withStyle(styleBase) {
                    var current = 0
                    val index1 = formatted.indexOf(actionInfo.pageTitle, current)
                    append(formatted.substring(current, index1))
                    withStyle(styleMain) { append(actionInfo.pageTitle) }
                    current = index1 + actionInfo.pageTitle.length

                    val index2 = formatted.indexOf(actionInfo.selectorText, current)
                    append(formatted.substring(current, index2))
                    withStyle(styleMain) { append(actionInfo.selectorText) }
                    current = index2 + actionInfo.selectorText.length

                    append(formatted.substring(current))
                }
            }

            is ActionInfo.CountInfo -> {
                withStyle(styleMain) {
                    append("${actionInfo.count}")
                }
            }
        }
    }
}
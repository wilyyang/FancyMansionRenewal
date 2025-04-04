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
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.fancymansion.core.presentation.compose.shape.borderLine
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
    val partSelf = buildAnnotatedString {
        when(val self = conditionWrapper.selfActionInfo){
            is ActionInfo.PageInfo -> {
                withStyle(SpanStyle(color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)) {
                    append("${self.pageTitle} ")
                }

                withStyle(SpanStyle(color = MaterialTheme.colorScheme.onSurface, fontWeight = FontWeight.Normal)) {
                    append(stringResource(R.string.condition_holder_text_page))
                }
            }
            is ActionInfo.SelectorInfo -> {
                withStyle(SpanStyle(color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)) {
                    append("${self.pageTitle} ")
                }

                withStyle(SpanStyle(color = MaterialTheme.colorScheme.onSurface, fontWeight = FontWeight.Normal)) {
                    append(stringResource(R.string.condition_holder_text_page))
                }

                withStyle(SpanStyle(color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)) {
                    append("${self.selectorText} ")
                }

                withStyle(SpanStyle(color = MaterialTheme.colorScheme.onSurface, fontWeight = FontWeight.Normal)) {
                    append(stringResource(R.string.condition_holder_text_selector))
                }
            }
            else -> {}
        }


    }

    val partTarget = buildAnnotatedString {
        when(val target = conditionWrapper.targetActionInfo){
            is ActionInfo.PageInfo -> {
                withStyle(SpanStyle(color = MaterialTheme.colorScheme.secondary, fontWeight = FontWeight.Bold)) {
                    append("${target.pageTitle} ")
                }

                withStyle(SpanStyle(color = MaterialTheme.colorScheme.onSurface, fontWeight = FontWeight.Normal)) {
                    append(stringResource(R.string.condition_holder_text_page))
                }

            }
            is ActionInfo.SelectorInfo -> {
                withStyle(SpanStyle(color = MaterialTheme.colorScheme.secondary, fontWeight = FontWeight.Bold)) {
                    append("${target.pageTitle} ")
                }

                withStyle(SpanStyle(color = MaterialTheme.colorScheme.onSurface, fontWeight = FontWeight.Normal)) {
                    append(stringResource(R.string.condition_holder_text_page))
                }

                withStyle(SpanStyle(color = MaterialTheme.colorScheme.secondary, fontWeight = FontWeight.Bold)) {
                    append("${target.selectorText} ")
                }

                withStyle(SpanStyle(color = MaterialTheme.colorScheme.onSurface, fontWeight = FontWeight.Normal)) {
                    append(stringResource(R.string.condition_holder_text_selector))
                }
            }
            is ActionInfo.CountInfo -> {
                withStyle(SpanStyle(color = MaterialTheme.colorScheme.secondary, fontWeight = FontWeight.Bold)) {
                    append("${target.count} ")
                }
            }
        }
    }

    val partLogicOp = buildAnnotatedString {
        withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
            append(stringResource(conditionWrapper.relationOp.localizedName.resId))
        }
    }

    val conditionTemplate = stringResource(R.string.condition_holder_text_format)
    val rawParts = conditionTemplate.split("%1\$s", "%2\$s", "%3\$s")

    val annotatedText = buildAnnotatedString {
        append(rawParts[0])
        append(partSelf)
        append(rawParts[1])
        append(partTarget)
        append(rawParts[2])
        append(partLogicOp)
    }

    Text(text = annotatedText)
}
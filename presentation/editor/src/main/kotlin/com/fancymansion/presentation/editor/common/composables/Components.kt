package com.fancymansion.presentation.editor.common.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.fancymansion.core.presentation.compose.shape.borderLine
import com.fancymansion.core.presentation.compose.theme.Paddings
import com.fancymansion.core.presentation.compose.theme.onSurfaceSub
import com.fancymansion.presentation.editor.R
import com.fancymansion.presentation.editor.common.ConditionState

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

            Row(
                verticalAlignment = Alignment.CenterVertically
            ){
                Text(
                    text = state.condition.conditionRule.logicalOp.name,
                    style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold),
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }


            Spacer(modifier = Modifier.height(5.dp))
            Text(
                text = stringResource(id = R.string.edit_selector_holder_selector_number, state.condition.conditionId),
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
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
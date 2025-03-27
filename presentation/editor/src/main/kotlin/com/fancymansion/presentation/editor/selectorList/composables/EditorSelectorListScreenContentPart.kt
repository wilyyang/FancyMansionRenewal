package com.fancymansion.presentation.editor.selectorList.composables

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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.fancymansion.core.presentation.compose.modifier.clickSingle
import com.fancymansion.core.presentation.compose.shape.borderLine
import com.fancymansion.core.presentation.compose.theme.Paddings
import com.fancymansion.core.presentation.compose.theme.onSurfaceSub
import com.fancymansion.presentation.editor.R
import com.fancymansion.presentation.editor.common.composables.CommonEditInfoTitle
import com.fancymansion.presentation.editor.selectorList.SelectorState
import com.fancymansion.presentation.editor.selectorList.SelectorSortOrder

@Composable
fun SelectorListHeader(
    modifier: Modifier = Modifier,
    isEditMode : Boolean,
    selectorSortOrder : SelectorSortOrder,
    selectorSize : Int,
    onListModeChangeClicked : () -> Unit,
    onSelectorSortOrderLastEdited : () -> Unit,
    onSelectorSortOrderTextAscending : () -> Unit,
    onSelectAllHolders : () -> Unit,
    onDeselectAllHolders : () -> Unit,
    onAddSelectorButtonClicked : () -> Unit,
    onDeleteSelectedHolders : () -> Unit
){
    val itemPaddingEnd = 12.dp
    Box(
        modifier = modifier
            .fillMaxWidth()
            .borderLine(density = LocalDensity.current, color = onSurfaceSub, bottom = 0.3.dp)
            .padding(
                vertical = Paddings.Basic.vertical,
                horizontal = Paddings.Basic.horizontal
            )
    ) {
        CommonEditInfoTitle(
            title = stringResource(id = R.string.edit_selector_list_header_selector_number, selectorSize)
        )

        Row(
            modifier = Modifier
                .align(Alignment.CenterEnd)
        ) {

            if(isEditMode){
                Text(
                    modifier = Modifier
                        .padding(end = itemPaddingEnd)
                        .clickSingle {
                            onSelectAllHolders()
                        },
                    text = stringResource(id = R.string.edit_selector_list_header_item_edit_total),
                    style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Medium)
                )

                Text(
                    modifier = Modifier
                        .padding(end = itemPaddingEnd)
                        .clickSingle {
                            onDeselectAllHolders()
                        },
                    text = stringResource(id = R.string.edit_selector_list_header_item_edit_cancel),
                    style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Medium)
                )

                Text(
                    modifier = Modifier
                        .padding(end = itemPaddingEnd)
                        .clickSingle {
                            onAddSelectorButtonClicked()
                        },
                    text = stringResource(id = R.string.edit_selector_list_header_item_edit_add),
                    style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Medium)
                )

                Text(
                    modifier = Modifier
                        .padding(end = itemPaddingEnd)
                        .clickSingle {
                            onDeleteSelectedHolders()
                        },
                    text = stringResource(id = R.string.edit_selector_list_header_item_edit_delete),
                    style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Medium)
                )

                Text(
                    modifier = Modifier.clickSingle {
                        onListModeChangeClicked()
                    },
                    text = stringResource(id = R.string.edit_selector_list_header_item_mode_complete),
                    style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Medium)
                )


            }else{
                Text(
                    modifier = Modifier
                        .padding(end = itemPaddingEnd)
                        .clickSingle {
                            onSelectorSortOrderLastEdited()
                        },
                    text = stringResource(id = R.string.edit_selector_list_header_item_order_edit),
                    color = if (selectorSortOrder == SelectorSortOrder.LAST_EDITED) MaterialTheme.colorScheme.primary
                    else MaterialTheme.colorScheme.onSurfaceVariant,
                    style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Medium)
                )

                Text(
                    modifier = Modifier
                        .padding(end = itemPaddingEnd)
                        .clickSingle {
                            onSelectorSortOrderTextAscending()
                        },
                    text = stringResource(id = R.string.edit_selector_list_header_item_order_text_ascending),
                    color = if (selectorSortOrder == SelectorSortOrder.TEXT_ASCENDING) MaterialTheme.colorScheme.primary
                    else MaterialTheme.colorScheme.onSurfaceVariant,
                    style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Medium)
                )

                Text(
                    modifier = Modifier.clickSingle() {
                        onListModeChangeClicked()
                    },
                    text = stringResource(id = R.string.edit_selector_list_header_item_mode_edit),
                    style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Medium)
                )
            }
        }
    }
}

@Composable
fun SelectorHolder(
    modifier: Modifier = Modifier,
    isEditMode : Boolean,
    state: SelectorState,
    onSelectorContentButtonClicked : (Long) -> Unit
){
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(85.dp)
            .background(color = MaterialTheme.colorScheme.surface)
            .clickable {
                onSelectorContentButtonClicked(state.selector.selectorId)
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
                    text = state.selector.text,
                    style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold),
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }


            Spacer(modifier = Modifier.height(5.dp))
            Text(
                text = stringResource(id = R.string.edit_selector_holder_selector_number, state.selector.selectorId),
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(3.dp))
        }

        Box(
            modifier = Modifier.align(Alignment.CenterEnd)
        ){
            if(isEditMode){
                Box(modifier = Modifier
                    .clip(shape = CircleShape)
                    .padding(0.5.dp)
                    .border(
                        width = 0.5.dp,
                        color = onSurfaceSub,
                        shape = CircleShape
                    )
                    .padding(4.dp)){

                    Box(
                        modifier = Modifier
                            .size(13.dp)
                            .clip(shape = CircleShape)
                            .background(color = if (state.selected.value) onSurfaceSub else Color.Transparent)
                    )
                }

            }else{
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
                    text = stringResource(id = R.string.edit_selector_holder_route_count, state.selector.routes.size),
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}
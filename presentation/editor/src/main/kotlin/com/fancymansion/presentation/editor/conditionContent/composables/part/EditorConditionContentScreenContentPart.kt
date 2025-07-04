package com.fancymansion.presentation.editor.conditionContent.composables.part

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.fancymansion.core.common.const.RelationOp
import com.fancymansion.core.presentation.compose.component.EnumDropdown
import com.fancymansion.core.presentation.compose.component.ListDropdown
import com.fancymansion.core.presentation.compose.theme.Paddings
import com.fancymansion.presentation.editor.R
import com.fancymansion.presentation.editor.common.composables.CommonEditInfoTitle
import com.fancymansion.presentation.editor.common.composables.SelectItem
import com.fancymansion.presentation.editor.common.itemMarginHeight
import com.fancymansion.presentation.editor.conditionContent.CompareType

@Composable
fun SelectActionTarget(
    modifier : Modifier = Modifier,
    title: String,
    mainItemText: String?,
    subItemText: String? = null,
    onClickMainItemText: () -> Unit,
    onClickSubItemText: () -> Unit = {}
){
    Column(modifier = modifier) {
        CommonEditInfoTitle(
            title = title
        )

        SelectItem(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    vertical = Paddings.Basic.vertical
                ),
            isSelect = mainItemText != null,
            textVerticalPadding = 12.dp,
            itemText = mainItemText ?: stringResource(R.string.edit_condition_content_select_action_page_not_found)
        ) {
            onClickMainItemText()
        }

        if(mainItemText != null){

            SelectItem(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        vertical = Paddings.Basic.vertical
                    ),
                isSelect = subItemText != null,
                checkIconTint = MaterialTheme.colorScheme.secondary,
                textVerticalPadding = 12.dp,
                itemText = subItemText ?: stringResource(R.string.edit_condition_content_select_action_selector_not_found)
            ) {
                onClickSubItemText()
            }
        }
        Spacer(modifier = Modifier.height(itemMarginHeight))
    }
}

@Composable
fun SelectRelationOperator(
    modifier : Modifier = Modifier,
    selectedType: RelationOp,
    onClickDropdownTitle: () -> Unit,
    onItemSelected: (RelationOp) -> Unit
) {
    val context = LocalContext.current
    Column(modifier = modifier) {
        CommonEditInfoTitle(
            title = stringResource(id = R.string.edit_condition_content_label_relation_operator)
        )

        EnumDropdown(
            modifier = Modifier
                .width(100.dp)
                .padding(vertical = Paddings.Basic.vertical),
            options = RelationOp.entries.toTypedArray(),
            selectedOption = selectedType,
            onClickDropdownTitle = onClickDropdownTitle,
            getDisplayName = {
                context.getString(it.localizedName.resId)
            }
        ) {
            onItemSelected(it)
        }

        Spacer(modifier = Modifier.height(itemMarginHeight))
    }
}

@Composable
fun SelectCompareType(
    modifier : Modifier = Modifier,
    selectedType: CompareType,
    onClickDropdownTitle: () -> Unit,
    onItemSelected: (CompareType) -> Unit
) {
    val context = LocalContext.current
    Column(modifier = modifier) {
        CommonEditInfoTitle(
            title = stringResource(id = R.string.edit_condition_content_label_compare_type_title)
        )

        EnumDropdown(
            modifier = Modifier
                .width(100.dp)
                .padding(vertical = Paddings.Basic.vertical),
            options = CompareType.entries.toTypedArray(),
            selectedOption = selectedType,
            onClickDropdownTitle = onClickDropdownTitle,
            getDisplayName = {
                context.getString(it.localizedName.resId)
            }
        ) {
            onItemSelected(it)
        }

        Spacer(modifier = Modifier.height(itemMarginHeight))
    }
}

@Composable
fun SelectTargetCount(
    modifier : Modifier = Modifier,
    options: List<Int>,
    count: Int,
    onClickDropdownTitle: () -> Unit,
    onItemSelected: (Int) -> Unit
) {
    Column(modifier = modifier) {
        CommonEditInfoTitle(
            title = stringResource(id = R.string.edit_condition_content_select_count_target_title)
        )

        ListDropdown (
            modifier = Modifier
                .width(100.dp)
                .padding(vertical = Paddings.Basic.vertical),
            options = options,
            selectedIndex = options.indexOf(count),
            onClickDropdownTitle = onClickDropdownTitle
        ) {
            onItemSelected(options.getOrNull(it)?: 0)
        }

        Spacer(modifier = Modifier.height(itemMarginHeight))
    }
}
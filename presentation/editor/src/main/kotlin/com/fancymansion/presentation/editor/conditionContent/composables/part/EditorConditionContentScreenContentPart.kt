package com.fancymansion.presentation.editor.conditionContent.composables.part

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.fancymansion.core.presentation.compose.theme.Paddings
import com.fancymansion.presentation.editor.R
import com.fancymansion.presentation.editor.common.composables.CommonEditInfoTitle
import com.fancymansion.presentation.editor.common.composables.SelectItem
import com.fancymansion.presentation.editor.common.itemMarginHeight

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
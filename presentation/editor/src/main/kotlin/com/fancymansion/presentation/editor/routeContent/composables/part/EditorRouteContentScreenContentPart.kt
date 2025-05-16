package com.fancymansion.presentation.editor.routeContent.composables.part

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
fun SelectRouteTargetPage(
    modifier : Modifier = Modifier,
    itemText: String,
    onClickItemText: () -> Unit
){
    Column(modifier = modifier) {
        CommonEditInfoTitle(
            title = stringResource(id = R.string.edit_route_content_label_target_page)
        )

        SelectItem(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    vertical = Paddings.Basic.vertical
                ),
            textVerticalPadding = 12.dp,
            itemText = itemText
        ) {
            onClickItemText()
        }

        Spacer(modifier = Modifier.height(itemMarginHeight))
    }
}
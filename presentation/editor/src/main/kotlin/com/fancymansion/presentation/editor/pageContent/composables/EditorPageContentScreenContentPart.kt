package com.fancymansion.presentation.editor.pageContent.composables

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.fancymansion.core.presentation.compose.modifier.clickSingle
import com.fancymansion.core.presentation.compose.shape.borderLine
import com.fancymansion.core.presentation.compose.theme.Paddings
import com.fancymansion.core.presentation.compose.theme.onSurfaceSub
import com.fancymansion.presentation.editor.R
import com.fancymansion.presentation.editor.common.composables.CommonEditInfoTitle
import com.fancymansion.presentation.editor.pageList.PageSortOrder

@Composable
fun PageContentHeader(
    modifier: Modifier = Modifier,
    contentBlockSize : Int,
    onShowSelectorList : () -> Unit,
    onAddContentBlockClicked : () -> Unit
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
            title = stringResource(id = R.string.edit_page_content_header_block_number, contentBlockSize)
        )

        Row(
            modifier = Modifier
                .align(Alignment.CenterEnd)
        ) {
            Text(
                modifier = Modifier
                    .padding(end = itemPaddingEnd)
                    .clickSingle {
                        onShowSelectorList()
                    },
                text = stringResource(id = R.string.edit_page_content_header_item_show_selector),
                style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Medium)
            )

            Text(
                modifier = Modifier
                    .clickSingle {
                        onAddContentBlockClicked()
                    },
                text = stringResource(id = R.string.edit_page_content_header_item_add_content_block),
                style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Medium)
            )
        }
    }
}
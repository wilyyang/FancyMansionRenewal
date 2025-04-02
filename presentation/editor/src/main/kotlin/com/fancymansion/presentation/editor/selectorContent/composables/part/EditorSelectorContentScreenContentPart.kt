package com.fancymansion.presentation.editor.selectorContent.composables.part

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.fancymansion.core.presentation.compose.component.RoundedTextField
import com.fancymansion.core.presentation.compose.modifier.clickSingle
import com.fancymansion.core.presentation.compose.shape.borderLine
import com.fancymansion.core.presentation.compose.theme.Paddings
import com.fancymansion.core.presentation.compose.theme.onSurfaceSub
import com.fancymansion.presentation.editor.R
import com.fancymansion.presentation.editor.common.composables.CommonEditInfoTitle
import com.fancymansion.presentation.editor.common.itemMarginHeight

@Composable
fun SelectorContentHeader(
    modifier: Modifier = Modifier,
    routeSize: Int,
    onShowRouteList: () -> Unit
) {
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
            modifier = Modifier.clickSingle {
                onShowRouteList()
            },
            title = stringResource(id = R.string.edit_selector_content_header_route_size, routeSize),
            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.primary)
        )
    }
}

@Composable
fun EditSelectorText(
    modifier : Modifier = Modifier,
    text: String,
    updateSelectorText: (String) -> Unit
){
    Column(modifier = modifier) {
        CommonEditInfoTitle(
            title = stringResource(id = R.string.edit_selector_content_top_label_selector_text)
        )

        RoundedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    vertical = Paddings.Basic.vertical
                ),
            value = text,
            maxLine = 2,
            hint = stringResource(id = R.string.edit_selector_content_edit_hint_selector_text),
            isCancelable = true
        ) {
            updateSelectorText(it)
        }

        Spacer(modifier = Modifier.height(itemMarginHeight))
    }
}
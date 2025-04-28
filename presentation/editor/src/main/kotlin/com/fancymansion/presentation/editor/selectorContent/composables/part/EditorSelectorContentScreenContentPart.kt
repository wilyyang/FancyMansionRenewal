package com.fancymansion.presentation.editor.selectorContent.composables.part

import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
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
import com.fancymansion.presentation.editor.selectorContent.RouteState

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

@Composable
fun RouteHolder(
    modifier: Modifier = Modifier,
    state: RouteState,
    isEnd: Boolean = false,
    onRouteHolderDelete: (Long) -> Unit,
    onRouteHolderClicked : (Long) -> Unit
){
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(85.dp)
            .background(color = MaterialTheme.colorScheme.surface)
            .clickable {
                onRouteHolderClicked(state.route.routeId)
            }
            .padding(horizontal = 8.dp)
            .then(
                if (isEnd) Modifier else Modifier.borderLine(
                    density = LocalDensity.current,
                    color = onSurfaceSub,
                    bottom = 0.3.dp
                )
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            modifier = Modifier.width(25.dp),
            painter = painterResource(com.fancymansion.core.presentation.R.drawable.ic_holder_drag),
            contentScale = ContentScale.FillWidth,
            contentDescription = "Drag Holder Route"
        )

        Column(modifier = Modifier
            .padding(start = 7.dp)
            .padding(vertical = 3.dp)
            .weight(1f)) {

            Text(
                text = stringResource(R.string.edit_route_holder_route_number, state.route.routeId),
                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Medium),
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(3.dp))

            // TODO 04.28
            Text(state.route.targetPageTitle)

        }

        Text(
            modifier = Modifier
                .clip(shape = CircleShape)
                .clickable{
                    onRouteHolderDelete(state.route.routeId)
                }
                .padding(10.dp)
                .border(
                    width = 0.5.dp,
                    color = onSurfaceSub,
                    shape = MaterialTheme.shapes.large
                )
                .padding(horizontal = 9.dp, vertical = 8.dp),
            text = stringResource(R.string.edit_route_holder_item_delete),
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}
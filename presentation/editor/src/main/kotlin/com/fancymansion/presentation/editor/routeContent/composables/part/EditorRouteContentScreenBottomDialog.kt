package com.fancymansion.presentation.editor.routeContent.composables.part

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.fancymansion.core.presentation.compose.theme.Paddings
import com.fancymansion.core.presentation.compose.theme.onSurfaceSub
import com.fancymansion.presentation.editor.R
import com.fancymansion.presentation.editor.common.composables.CommonEditInfoTitle
import com.fancymansion.presentation.editor.routeContent.TargetPageWrapper
import com.fancymansion.presentation.editor.selectorContent.composables.part.detailPanelShape

@Composable
fun BottomTargetPageListDialog(
    targetPageId: Long,
    pageList: List<TargetPageWrapper>,
    onSelectTargetPage: (pageId: Long) -> Unit,
) {
    val listState = rememberLazyListState()
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.6f)
            .clip(detailPanelShape)
            .background(MaterialTheme.colorScheme.surface)
            .padding(top = 10.dp)
    ) {
        CommonEditInfoTitle(
            modifier = Modifier
                .padding(vertical = Paddings.Basic.vertical)
                .padding(start = Paddings.Basic.horizontal),
            title = stringResource(id = R.string.edit_route_content_label_target_page_list)
        )

        HorizontalDivider(modifier = Modifier.fillMaxWidth(), thickness = 0.3.dp, color = onSurfaceSub)

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            state = listState
        ) {

            items(items = pageList) {
                // TODO 05.16
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(85.dp)
                        .background(color = MaterialTheme.colorScheme.surface)
                        .padding(horizontal = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Text(
                        modifier = Modifier.weight(1f),
                        text = it.pageTitle,
                        style = MaterialTheme.typography.bodyLarge,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    if(targetPageId == it.pageId){
                        Icon(
                            modifier = Modifier
                                .height(MaterialTheme.typography.bodyLarge.lineHeight.value.dp),
                            painter = painterResource(id = com.fancymansion.core.presentation.R.drawable.ic_check_bold_600),
                            tint = MaterialTheme.colorScheme.primary,
                            contentDescription = null
                        )
                    }
                }
            }

            item {
                HorizontalDivider(modifier = Modifier.fillMaxWidth(), thickness = 0.3.dp, color = onSurfaceSub)
                Spacer(modifier = Modifier.height(30.dp))
            }
        }
    }
}
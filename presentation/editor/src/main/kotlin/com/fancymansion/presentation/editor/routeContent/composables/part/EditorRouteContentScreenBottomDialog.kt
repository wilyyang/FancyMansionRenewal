package com.fancymansion.presentation.editor.routeContent.composables.part

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
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
import androidx.compose.ui.text.font.FontWeight
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

            itemsIndexed(items = pageList) { index, item ->
                Column (
                    modifier = Modifier.padding(horizontal = 7.dp)
                ){
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(color = MaterialTheme.colorScheme.surface)
                            .clickable{
                                onSelectTargetPage(item.pageId)
                            }
                            .padding(horizontal = 7.dp)
                            .padding(vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Text(
                            modifier = Modifier.weight(1f),
                            text = item.pageTitle,
                            style = if(targetPageId == item.pageId){
                                MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold)
                            }else{
                                MaterialTheme.typography.bodyLarge
                            },
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )

                        if(targetPageId == item.pageId){
                            Icon(
                                modifier = Modifier
                                    .height(MaterialTheme.typography.bodyLarge.lineHeight.value.dp),
                                painter = painterResource(id = com.fancymansion.core.presentation.R.drawable.ic_check_bold_600),
                                tint = MaterialTheme.colorScheme.primary,
                                contentDescription = null
                            )
                        }
                    }

                    if(index < pageList.size - 1){
                        HorizontalDivider(modifier = Modifier.fillMaxWidth(), thickness = 0.3.dp, color = onSurfaceSub)
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
package com.fancymansion.presentation.editor.common.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.fancymansion.core.presentation.R
import com.fancymansion.core.presentation.compose.theme.Paddings
import com.fancymansion.core.presentation.compose.theme.onSurfaceSub
import com.fancymansion.presentation.editor.common.SelectItemWrapper
import com.fancymansion.presentation.editor.selectorContent.composables.part.detailPanelShape

@Composable
fun SelectItem(
    modifier: Modifier = Modifier,
    selectBackground: Color = MaterialTheme.colorScheme.surface,
    notSelectBackground: Color = MaterialTheme.colorScheme.background,
    isSelect: Boolean = true,
    checkIconTint: Color = MaterialTheme.colorScheme.primary,
    textStyle: TextStyle = MaterialTheme.typography.bodyLarge,
    textHorizontalPadding: Dp = 16.dp,
    textVerticalPadding: Dp = 8.dp,
    borderColor: Color = MaterialTheme.colorScheme.outline,
    borderShape: Shape = MaterialTheme.shapes.small,
    itemText: String,
    onClickItemText: () -> Unit
){
    Row(
        modifier = modifier
            .border(
                width = 1.dp,
                color = borderColor,
                shape = borderShape
            )
            .padding(0.5.dp)
            .clip(borderShape)
            .background(color = if(isSelect) selectBackground else notSelectBackground)
            .clickable{
                onClickItemText()
            }
            .padding(horizontal = textHorizontalPadding, vertical = textVerticalPadding),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Text(
            modifier = Modifier.weight(1f),
            text = itemText,
            style = textStyle,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )

        if(isSelect){
            Icon(
                modifier = Modifier
                    .height(textStyle.lineHeight.value.dp),
                painter = painterResource(id = R.drawable.ic_check_bold_600),
                tint = checkIconTint,
                contentDescription = null
            )
        }
    }
}

@Composable
fun BottomSelectListDialog(
    title: String,
    selectedId: Long,
    checkIconTint: Color = MaterialTheme.colorScheme.primary,
    itemList: List<SelectItemWrapper>,
    onSelectItem: (itemId: Long) -> Unit,
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
            title = title
        )

        HorizontalDivider(modifier = Modifier.fillMaxWidth(), thickness = 0.3.dp, color = onSurfaceSub)

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            state = listState
        ) {

            itemsIndexed(items = itemList) { index, item ->
                Column (
                    modifier = Modifier.padding(horizontal = 7.dp)
                ){
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(color = MaterialTheme.colorScheme.surface)
                            .clickable{
                                onSelectItem(item.itemId)
                            }
                            .padding(horizontal = 7.dp)
                            .padding(vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Text(
                            modifier = Modifier.weight(1f),
                            text = item.itemText,
                            style = if(selectedId == item.itemId){
                                MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold)
                            }else{
                                MaterialTheme.typography.bodyLarge
                            },
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )

                        if(selectedId == item.itemId){
                            Icon(
                                modifier = Modifier
                                    .height(MaterialTheme.typography.bodyLarge.lineHeight.value.dp),
                                painter = painterResource(id = R.drawable.ic_check_bold_600),
                                tint = checkIconTint,
                                contentDescription = null
                            )
                        }
                    }

                    if(index < itemList.size - 1){
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
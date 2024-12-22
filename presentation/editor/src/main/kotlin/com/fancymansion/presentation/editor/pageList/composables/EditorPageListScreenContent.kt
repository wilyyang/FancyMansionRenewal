package com.fancymansion.presentation.editor.pageList.composables

import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.fancymansion.core.common.const.LOGIC_ID_NOT_ASSIGNED
import com.fancymansion.core.common.const.PageType
import com.fancymansion.core.common.resource.StringValue
import com.fancymansion.core.presentation.base.CommonEvent
import com.fancymansion.core.presentation.compose.modifier.clickSingle
import com.fancymansion.core.presentation.compose.screen.NoDataScreen
import com.fancymansion.core.presentation.compose.theme.ColorSet
import com.fancymansion.core.presentation.compose.theme.Paddings
import com.fancymansion.core.presentation.compose.theme.onSurfaceSub
import com.fancymansion.domain.model.book.PageLogicModel
import com.fancymansion.presentation.editor.R
import com.fancymansion.presentation.editor.common.composables.CommonEditInfoTitle
import com.fancymansion.presentation.editor.common.itemMarginHeight
import com.fancymansion.presentation.editor.pageList.EditorPageListContract

@Composable
fun EditorPageListScreenContent(
    modifier: Modifier = Modifier,
    uiState: EditorPageListContract.State,
    onEventSent: (event: EditorPageListContract.Event) -> Unit,
    onCommonEventSent: (event: CommonEvent) -> Unit
) {
    if (uiState.logic.id == LOGIC_ID_NOT_ASSIGNED || uiState.logic.logics.isEmpty()) {
        Box(
            modifier = modifier
                .background(color = MaterialTheme.colorScheme.surface)
                .fillMaxSize(), contentAlignment = Alignment.Center
        ) {

            NoDataScreen(
                modifier = Modifier.padding(horizontal = 20.dp),
                option1Title = StringValue.StringResource(resId = com.fancymansion.core.presentation.R.string.button_back),
                onClickOption1 = {
                    onCommonEventSent(CommonEvent.CloseEvent)
                }
            )
        }
    } else {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(color = MaterialTheme.colorScheme.surface)
        ) {

            item{
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(color = MaterialTheme.colorScheme.surface)
                ) {
                    Box(
                        modifier = Modifier
                            .padding(vertical = Paddings.Basic.vertical, horizontal = Paddings.Basic.horizontal)
                            .fillMaxWidth()
                    ) {
                        CommonEditInfoTitle(
                            title = stringResource(id = R.string.edit_overview_top_label_book_page)
                        )

                        Text(
                            modifier = Modifier
                                .align(Alignment.CenterEnd)
                                .clickSingle {
                                    /**
                                     * TODO
                                     */
                                },
                            text = stringResource(id = R.string.edit_overview_top_edit_page),
                            style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Medium)
                        )
                    }

                    HorizontalDivider(modifier = Modifier.fillMaxWidth(), thickness = 0.3.dp, color = onSurfaceSub)

                    uiState.logic.logics.forEachIndexed { index, pageLogic ->
                        PageHolder(
                            pageLogic = pageLogic,
                            onPageContentButtonClicked = {
                                /**
                                 * TODO
                                 */
                            }
                        )

                        if (index < uiState.logic.logics.size - 1) {
                            HorizontalDivider(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 8.dp),
                                thickness = 0.3.dp,
                                color = onSurfaceSub
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(itemMarginHeight * 2))
                }
            }
        }
    }
}

@Composable
fun PageHolder(
    pageLogic: PageLogicModel,
    onPageContentButtonClicked : (Long) -> Unit
){
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(85.dp)
            .clickSingle(
                indication = LocalIndication.current
            ) {
                onPageContentButtonClicked(pageLogic.pageId)
            }
            .padding(horizontal = 18.dp)
    ) {
        Column(modifier = Modifier
            .align(Alignment.CenterStart)
            .fillMaxWidth(0.75f)) {

            Row(
                verticalAlignment = Alignment.CenterVertically
            ){
                Box(
                    modifier = Modifier
                        .clip(shape = RoundedCornerShape(2.dp))
                        .background(
                            color = when (pageLogic.type) {
                                PageType.START -> ColorSet.cyan_1ecdcd
                                PageType.ENDING -> ColorSet.navy_324155
                                else -> ColorSet.blue_1e9eff
                            }
                        )
                        .padding(horizontal = 3.dp, vertical = 1.dp)

                ) {
                    Text(
                        text = stringResource(id = pageLogic.type.localizedName.resId),
                        style = MaterialTheme.typography.labelMedium,
                        color = ColorSet.white_ffffff
                    )
                }

                Text(
                    modifier = Modifier.padding(start = 5.dp),
                    text = pageLogic.title,
                    style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold),
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1,
                    overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
                )
            }


            Spacer(modifier = Modifier.height(5.dp))
            Text(
                text = stringResource(id = R.string.edit_overview_page_holder_page_number, pageLogic.pageId),
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(3.dp))
        }

        Text(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .clip(shape = MaterialTheme.shapes.large)
                .padding(0.5.dp)
                .border(
                    width = 0.5.dp,
                    color = onSurfaceSub,
                    shape = MaterialTheme.shapes.large
                )
                .padding(horizontal = 6.dp, vertical = 4.dp),
            text = stringResource(id = R.string.edit_overview_page_holder_selector_count, pageLogic.selectors.size),
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}
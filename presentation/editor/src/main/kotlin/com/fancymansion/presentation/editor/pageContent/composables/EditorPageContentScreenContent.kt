package com.fancymansion.presentation.editor.pageContent.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.fancymansion.core.common.resource.StringValue
import com.fancymansion.core.presentation.R
import com.fancymansion.core.presentation.base.CommonEvent
import com.fancymansion.core.presentation.compose.modifier.clickSingle
import com.fancymansion.core.presentation.compose.screen.NoDataScreen
import com.fancymansion.core.presentation.compose.shape.borderLine
import com.fancymansion.presentation.editor.pageContent.EditorPageContentContract

@Composable
fun EditorPageContentScreenContent(
    modifier: Modifier = Modifier,
    uiState: EditorPageContentContract.State,
    onEventSent: (event: EditorPageContentContract.Event) -> Unit,
    onCommonEventSent: (event: CommonEvent) -> Unit,
    focusManager : FocusManager
) {
    if (!uiState.isInitSuccess) {
        Box(
            modifier = modifier
                .background(color = MaterialTheme.colorScheme.surface)
                .fillMaxSize(), contentAlignment = Alignment.Center
        ) {

            NoDataScreen(
                modifier = Modifier.padding(horizontal = 20.dp),
                option1Title = StringValue.StringResource(resId = R.string.button_back),
                onClickOption1 = {
                    onCommonEventSent(CommonEvent.CloseEvent)
                }
            )
        }
    }else{
        Box(modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.surface)){

            Column(
                modifier = Modifier.align(Alignment.TopStart)
            ) {
                PageContentHeader(
                    contentBlockSize = 0,
                    onShowSelectorList = { /*TODO*/ },
                    onAddContentBlockClicked = { /*TODO*/ }
                )
            }

            Box(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .fillMaxWidth()
                    .height(80.dp)
                    .background(MaterialTheme.colorScheme.surface)
                    .borderLine(
                        density = LocalDensity.current,
                        color = MaterialTheme.colorScheme.outline,
                        top = 1.dp
                    )
            ) {

                Box(
                    modifier = Modifier
                        .padding(vertical = 11.dp, horizontal = 14.dp)
                        .fillMaxSize()
                        .clip(
                            shape = MaterialTheme.shapes.small
                        )
                        .background(color = MaterialTheme.colorScheme.primary)
                        .clickSingle {
                            focusManager.clearFocus()
                            // TODO
                        }
                ) {
                    Text(
                        modifier = Modifier.align(Alignment.Center),
                        text = stringResource(id = com.fancymansion.presentation.editor.R.string.edit_page_content_button_page_preview),
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }
        }
    }
}
package com.fancymansion.presentation.main.tab.my.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.fancymansion.core.common.resource.StringValue
import com.fancymansion.core.presentation.base.CommonEvent
import com.fancymansion.core.presentation.compose.modifier.clickSingle
import com.fancymansion.core.presentation.compose.screen.NoDataScreen
import com.fancymansion.core.presentation.compose.shape.borderLine
import com.fancymansion.presentation.main.R
import com.fancymansion.presentation.main.tab.my.MyTabContract

@Composable
fun MyTabScreenContent(
    modifier: Modifier = Modifier,
    uiState: MyTabContract.State,
    onEventSent: (event: MyTabContract.Event) -> Unit,
    onCommonEventSent: (event: CommonEvent) -> Unit
) {
    if (!uiState.isInitSuccess) {
        Box(
            modifier = modifier
                .background(color = MaterialTheme.colorScheme.surface)
                .fillMaxSize(), contentAlignment = Alignment.Center
        ) {

            NoDataScreen(
                modifier = Modifier.padding(horizontal = 20.dp),
                option1Title = StringValue.StringResource(resId = com.fancymansion.core.presentation.R.string.button_exit),
                onClickOption1 = {
                    onCommonEventSent(CommonEvent.CloseEvent)
                }
            )
        }
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = MaterialTheme.colorScheme.surface)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .borderLine(
                        density = LocalDensity.current,
                        color = MaterialTheme.colorScheme.outline,
                        bottom = 1.dp
                    )
                    .padding(top = 13.dp, bottom = 8.dp)
                    .padding(horizontal = 15.dp)
            ) {
                Text(
                    text = stringResource(id = R.string.my_tab_bar_title),
                    style = MaterialTheme.typography.titleLarge
                )
            }


            Text(
                text = uiState.nickname,
                style = MaterialTheme.typography.bodyLarge
            )

            Text(
                text = uiState.email,
                style = MaterialTheme.typography.bodyLarge
            )

            Text(
                modifier = Modifier.clickSingle{
                    onEventSent(MyTabContract.Event.OnClickLogout)
                },
                text = stringResource(id = R.string.my_button_logout),
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}
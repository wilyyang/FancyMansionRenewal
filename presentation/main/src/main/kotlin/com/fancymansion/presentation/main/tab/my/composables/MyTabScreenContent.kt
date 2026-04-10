package com.fancymansion.presentation.main.tab.my.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.fancymansion.core.presentation.base.CommonEvent
import com.fancymansion.core.presentation.compose.modifier.clickSingle
import com.fancymansion.core.presentation.compose.shape.borderLine
import com.fancymansion.core.presentation.compose.theme.onSurfaceSub
import com.fancymansion.presentation.main.R
import com.fancymansion.presentation.main.tab.my.MyTabContract
import com.fancymansion.presentation.main.tab.my.composables.part.UserNickname

@Composable
fun MyTabScreenContent(
    modifier: Modifier = Modifier,
    uiState: MyTabContract.State,
    onEventSent: (event: MyTabContract.Event) -> Unit,
    onCommonEventSent: (event: CommonEvent) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.background)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = MaterialTheme.colorScheme.surface)
                .borderLine(
                    density = LocalDensity.current,
                    color = MaterialTheme.colorScheme.outline,
                    bottom = 1.dp
                )
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 13.dp, bottom = 8.dp)
                    .padding(horizontal = 15.dp)
            ) {
                Text(
                    modifier = Modifier.align(Alignment.Center),
                    text = stringResource(id = R.string.my_tab_bar_title),
                    style = MaterialTheme.typography.titleLarge
                )
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 18.dp, horizontal = 14.dp)
            ) {
                Column(
                    modifier = Modifier.align(Alignment.CenterStart)
                ) {
                    UserNickname(
                        modifier = Modifier,
                        nickname = uiState.nickname,
                        email = uiState.email,
                        onEditClick = {
                            onEventSent(MyTabContract.Event.OnClickEditNickname)
                        }
                    )

                    Spacer(modifier = Modifier.height(10.dp))
                }

                Text(
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .clip(shape = MaterialTheme.shapes.extraSmall)
                        .padding(0.5.dp)
                        .border(
                            width = 0.5.dp,
                            color = MaterialTheme.colorScheme.outline,
                            shape = MaterialTheme.shapes.extraSmall
                        )
                        .clickSingle {
                            onEventSent(MyTabContract.Event.OnClickLogout)
                        }
                        .padding(horizontal = 14.dp, vertical = 11.dp),
                    text = stringResource(id = R.string.my_button_logout),
                    style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Medium),
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.75f)
                )
            }
        }
    }
}
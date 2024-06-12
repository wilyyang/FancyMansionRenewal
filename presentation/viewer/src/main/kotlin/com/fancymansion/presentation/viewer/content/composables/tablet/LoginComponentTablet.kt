package com.fancymansion.presentation.viewer.content.composables.tablet

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.text.HtmlCompat
import com.fancymansion.core.presentation.base.toAnnotatedString
import com.fancymansion.core.presentation.component.FlexibleImageButton
import com.fancymansion.core.presentation.component.RoundedTextField
import com.fancymansion.core.presentation.component.TextCheckBox
import com.fancymansion.core.presentation.theme.ColorSet
import com.fancymansion.presentation.viewer.R

import com.fancymansion.presentation.viewer.content.LoginContract
import com.fancymansion.presentation.viewer.content.LoginValue

@Composable
fun LoginScreenCommonTablet(
    modifier: Modifier = Modifier,
    focusManager: FocusManager,
    uiState: LoginContract.State,
    onEventSent: (event: LoginContract.Event) -> Unit
) {
    val joinUserBtnImage =  com.fancymansion.core.presentation.R.drawable.img_long_color_sky_btn
    Column(
        modifier = modifier
    ) {
        LoginHolderTablet(
            loginValue = uiState.loginValue,
            focusManager = focusManager,
            onLoginValueChanged = { onEventSent(LoginContract.Event.LoginValueUpdate(it)) },
            isAutoLogin = uiState.isAutoLoginChecked,
            onClickAutoLoginChecked = {
                focusManager.clearFocus()
                onEventSent(LoginContract.Event.AutoLoginChecked(it))
            },
            onClickLogin = {
                focusManager.clearFocus()
                onEventSent(LoginContract.Event.LoginButtonClicked)
            },
            idTextMaxLength = uiState.idTextMaxLength
        )

        Column(
            modifier = Modifier
                .padding(horizontal = 32.dp)
                .fillMaxWidth()
        ) {

            Row(
                modifier = Modifier
                    .padding(vertical = 20.dp)
                    .height(IntrinsicSize.Min)
                    .align(Alignment.CenterHorizontally)
            ) {
                Text(
                    modifier = Modifier
                        .clip(MaterialTheme.shapes.small)
                        .clickable { onEventSent(LoginContract.Event.FindIdButtonClicked) }
                        .padding(10.dp),
                    text = stringResource(id = com.fancymansion.core.common.R.string.login_find_id),
                    style = MaterialTheme.typography.bodyLarge
                )
                Divider(
                    color = ColorSet.sky_85b5cb,
                    modifier = Modifier
                        .padding(horizontal = 16.dp, vertical = 13.dp)
                        .fillMaxHeight()
                        .width(1.dp)
                )
                Text(
                    modifier = Modifier
                        .clip(MaterialTheme.shapes.small)
                        .clickable { onEventSent(LoginContract.Event.FindPasswordButtonClicked) }
                        .padding(10.dp),
                    text = stringResource(id = com.fancymansion.core.common.R.string.login_find_password),
                    style = MaterialTheme.typography.bodyLarge
                )
            }

            FlexibleImageButton(
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(bottom = 8.dp),
                imageClickable = painterResource(id = joinUserBtnImage),
                text = stringResource(id = com.fancymansion.core.common.R.string.login_join_membership),
                textStyle = MaterialTheme.typography.titleLarge,
                pressScale = 0.95f,
                onClick = { onEventSent(LoginContract.Event.JoinMembershipButtonClicked) }
            )
        }
    }
}

@Composable
fun LoginHolderTablet(
    modifier: Modifier = Modifier,
    focusManager: FocusManager,
    loginValue: LoginValue,
    onLoginValueChanged: (LoginValue) -> Unit = {},
    isAutoLogin : Boolean,
    onClickAutoLoginChecked: (Boolean) -> Unit = {},
    onClickLogin: () -> Unit = {},
    idTextMaxLength: Int
) {
    val noticeIcon = R.drawable.img_notice_icon_tablet
    val loginButtonImage = com.fancymansion.core.presentation.R.drawable.img_long_color_blue_btn

    Column(
        modifier = modifier
            .clip(
                shape = RoundedCornerShape(23.0.dp)
            )
            .background(color = ColorSet.holder_login_color)
            .padding(horizontal = 32.dp)
            .padding(top = 27.5.dp, bottom = 31.dp)
    ) {

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                modifier = Modifier.size(22.5.dp),
                painter = painterResource(id = noticeIcon),
                contentScale = ContentScale.Fit,
                contentDescription = null
            )

            Text(
                text = HtmlCompat.fromHtml(stringResource(id = R.string.login_guide_description_tablet), 0).toAnnotatedString(),
                style = MaterialTheme.typography.bodyLarge.copy(fontSize = 16.sp),
                modifier = Modifier
                    .padding(start = 7.dp)
                    .align(Alignment.CenterVertically),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis

            )

        }

        Column(
            modifier = Modifier
                .padding(top = 20.dp)
                .height(IntrinsicSize.Min),
            verticalArrangement = Arrangement.Center
        ) {
            RoundedTextField(
                value = loginValue.userId,
                hint = stringResource(id = R.string.login_hint_id),
                imeAction = ImeAction.Next,
                borderShape  = MaterialTheme.shapes.medium,
                keyboardType = KeyboardType.Email,
                keyboardActions = KeyboardActions {
                    focusManager.moveFocus(FocusDirection.Down)
                },
                onValueChange = {
                    if(it.length <= idTextMaxLength) onLoginValueChanged(loginValue.copy(userId = it))
                }
            )

            RoundedTextField(
                modifier = Modifier.padding(top = 12.dp),
                value = loginValue.password,
                hint = stringResource(id = R.string.login_hint_password),
                imeAction = ImeAction.Send,
                borderShape  = MaterialTheme.shapes.medium,
                keyboardType = KeyboardType.Password,
                keyboardActions = KeyboardActions {
                    onClickLogin()
                },
                onValueChange= {
                    onLoginValueChanged(loginValue.copy(password = it))
                }
            )
        }

        TextCheckBox(
            modifier = Modifier.padding(top = 15.dp),
            checked = isAutoLogin,
            checkboxText = stringResource(id = R.string.login_text_auto_login),
            checkBoxSize = 22.dp,
            onClickChecked = onClickAutoLoginChecked
        )

        FlexibleImageButton(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp),
            contentPadding = PaddingValues(bottom = 8.dp),
            imageClickable = painterResource(id = loginButtonImage),
            text = stringResource(id = com.fancymansion.core.common.R.string.login_title),
            textStyle = MaterialTheme.typography.titleLarge,
            pressScale = 0.95f,
            onClick = onClickLogin
        )
    }
}
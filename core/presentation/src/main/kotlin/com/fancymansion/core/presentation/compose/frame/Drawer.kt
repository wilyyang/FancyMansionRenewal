package com.fancymansion.core.presentation.compose.frame

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerEvent
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fancymansion.core.common.const.MOBILE_PREVIEW_SPEC
import com.fancymansion.core.common.const.TABLET_PREVIEW_SPEC
import com.fancymansion.core.presentation.R
import com.fancymansion.core.presentation.base.LoadState
import com.fancymansion.core.presentation.compose.modifier.clickSingle
import com.fancymansion.core.presentation.compose.component.DefaultButton
import com.fancymansion.core.presentation.compose.component.FlexibleImageButton
import com.fancymansion.core.presentation.compose.theme.ColorSet
import com.fancymansion.core.presentation.compose.theme.FancyMansionTheme
import com.fancymansion.core.presentation.window.TypePane
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

data class MenuItem(val key : String, var title : String, val imageId : Int, val onClickMenu : () -> Unit)

@Composable
fun FancyMansionDrawer(
    modifier: Modifier = Modifier,
    onClickSetting: () -> Unit = {},
    userNickname : String? = null,
    isUserLogin : Boolean = false,
    onLoginButtonClicked: () -> Unit = {},
    onJoinMemberButtonClicked: () -> Unit = {},
    onLogoutButtonClicked: () -> Unit = {},
    onLogoLongClicked: () -> Unit = {},
    menuCommonItems: List<MenuItem>? = null,
    menuUserItems: List<MenuItem>? = null
) {
    val isTitleLongClick =  remember{ mutableStateOf(false) }
    DisposableEffect(key1 = isTitleLongClick.value) {
        val job = Job()
        if(isTitleLongClick.value) {
            CoroutineScope(Dispatchers.IO + job).launch {
                delay(7000L)
                onLogoLongClicked()
                isTitleLongClick.value = false
            }
        } else {
            job.cancel()
        }

        onDispose {
            job.cancel()
        }
    }

    Column(
        modifier = modifier
            .fillMaxHeight()
            .background(color = MaterialTheme.colorScheme.surface),
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = ColorSet.blue_20b1f9)
        ) {


            Image(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(8.6.dp)
                    .clip(CircleShape)
                    .size(25.3.dp)
                    .clickable(onClick = onClickSetting),
                painter = painterResource(id = R.drawable.ic_setting),
                contentScale = ContentScale.Fit,
                contentDescription = null
            )

            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    modifier = Modifier
                        .padding(top = 44.dp)
                        .fillMaxWidth(0.67f)
                        .pointerInput(Unit) {
                            awaitEachGesture {
                                do {
                                    val event: PointerEvent = awaitPointerEvent()
                                    event.changes.forEach { _ ->
                                        when (event.type) {
                                            PointerEventType.Press -> { isTitleLongClick.value = true }
                                            PointerEventType.Release -> { isTitleLongClick.value = false }
                                        }
                                    }
                                } while (event.changes.any { it.pressed })
                            }
                        },
                    painter = painterResource(id = R.drawable.img_bell),
                    contentDescription = null
                )

                if(!isUserLogin){
                    Text(
                        modifier = Modifier.padding(top = 13.dp),
                        text = stringResource(id = R.string.drawer_hello),
                        style = MaterialTheme.typography.titleLarge.copy(fontSize = 18.6.sp),
                        color = Color.White
                    )

                    Text(
                        modifier = Modifier.padding(top = 7.3.dp),
                        text = stringResource(id = R.string.drawer_request_login),
                        style = MaterialTheme.typography.titleLarge.copy(fontSize = 18.6.sp, fontWeight = FontWeight.Normal),
                        color = Color.White,
                        textAlign = TextAlign.Center
                    )

                    Row(modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 20.3.dp, bottom = 28.6.dp)
                        .padding(horizontal = 14.dp),
                        horizontalArrangement = Arrangement.Center
                    ){
                        FlexibleImageButton(
                            modifier = Modifier
                                .weight(1f)
                                .padding(end = 7.5.dp),
                            contentPadding = PaddingValues(bottom = 2.5.dp),
                            text = "임시 타이틀",
                            pressScale = 0.95f,
                            textStyle = MaterialTheme.typography.titleSmall,
                            textColor = ColorSet.blue_3370de,
                            onClick = onLoginButtonClicked
                        )

                        FlexibleImageButton(
                            modifier = Modifier
                                .weight(1f)
                                .padding(start = 7.5.dp),
                            contentPadding = PaddingValues(bottom = 2.5.dp),
                            text = stringResource(id = com.fancymansion.core.common.R.string.login_join_membership),
                            pressScale = 0.95f,
                            textStyle = MaterialTheme.typography.titleSmall,
                            onClick = onJoinMemberButtonClicked
                        )
                    }
                }else{
                    userNickname?.let {
                        Text(
                            modifier = Modifier.padding(top = 13.dp, bottom = 36.6.dp),
                            text = userNickname,
                            style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Normal),
                            color = Color.White
                        )
                    }
                }
            }
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .background(color = MaterialTheme.colorScheme.surface),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
           item {
               if(isUserLogin && menuUserItems != null){
                   Row(
                       modifier = Modifier
                           .padding(horizontal = 14.dp)
                           .padding(top = 21.6.dp)
                           .clip(shape = MaterialTheme.shapes.large)
                           .background(color = ColorSet.sky_f1faff)
                           .padding(horizontal = 7.dp)
                           .padding(vertical = 11.2.dp)
                   ) {
                       menuUserItems.forEach { menuItem ->
                           MenuButton(
                               modifier = Modifier
                                   .weight(1f)
                                   .padding(horizontal = 1.dp)
                                   .padding(vertical = 1.8.dp),
                               imageWidthPercent = 0.8f,
                               menuItem = menuItem,
                               spacePadding = 4.dp
                           )
                       }
                   }
               }

               menuCommonItems?.let {
                   Row(
                       modifier = Modifier
                           .padding(horizontal = 10.2.dp)
                           .padding(vertical = 15.8.dp)
                   ) {
                       menuCommonItems.forEach { menuItem ->
                           MenuButton(
                               modifier = Modifier
                                   .weight(1f)
                                   .padding(horizontal = 1.8.dp)
                                   .padding(vertical = 1.8.dp),
                               imageWidthPercent = 0.55f,
                               menuItem = menuItem,
                               spacePadding = 4.dp
                           )
                       }
                   }
               }
           }
        }

        if(isUserLogin){
            DefaultButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(55.dp),
                text = stringResource(id = com.fancymansion.core.common.R.string.logout_title),
                colorText = ColorSet.gray_9d9d9d,
                colorBackground = ColorSet.gray_f1f1f1,
                onClick = onLogoutButtonClicked
            )

        }
    }
}

@Composable
fun FancyMansionDrawerTablet(
    modifier: Modifier = Modifier,
    onClickSetting: () -> Unit = {},
    userNickname : String? = null,
    isUserLogin : Boolean = false,
    onLoginButtonClicked: () -> Unit = {},
    onJoinMemberButtonClicked: () -> Unit = {},
    onLogoutButtonClicked: () -> Unit = {},
    onLogoLongClicked: () -> Unit = {},
    menuCommonItems: List<MenuItem>? = null,
    menuUserItems: List<MenuItem>? = null
) {
    val isLogoLongClick =  remember{ mutableStateOf(false) }
    DisposableEffect(key1 = isLogoLongClick.value) {
        val job = Job()
        if(isLogoLongClick.value) {
            CoroutineScope(Dispatchers.IO + job).launch {
                delay(7000L)
                onLogoLongClicked()
                isLogoLongClick.value = false
            }
        } else {
            job.cancel()
        }

        onDispose {
            job.cancel()
        }
    }

    Column(
        modifier = modifier
            .fillMaxHeight()
            .background(color = MaterialTheme.colorScheme.surface),
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = ColorSet.blue_20b1f9)
        ) {

            Image(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(top = 12.dp, end = 8.5.dp)
                    .clip(CircleShape)
                    .width(32.dp)
                    .clickable(onClick = onClickSetting),
                painter = painterResource(id = R.drawable.ic_setting),
                contentScale = ContentScale.Fit,
                contentDescription = null
            )

            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    modifier = Modifier
                        .padding(top = 39.dp)
                        .fillMaxWidth(0.58f)
                        .pointerInput(Unit) {
                            awaitEachGesture {
                                do {
                                    val event: PointerEvent = awaitPointerEvent()
                                    event.changes.forEach { _ ->
                                        when (event.type) {
                                            PointerEventType.Press -> { isLogoLongClick.value = true }
                                            PointerEventType.Release -> { isLogoLongClick.value = false }
                                        }
                                    }
                                } while (event.changes.any { it.pressed })
                            }
                        },
                    painter = painterResource(id = R.drawable.img_btn_sky_w415_h144),
                    contentScale = ContentScale.FillWidth,
                    contentDescription = null
                )

                if(!isUserLogin){
                    Text(
                        modifier = Modifier.padding(top = 14.dp),
                        text = stringResource(id = R.string.drawer_hello),
                        style = MaterialTheme.typography.headlineMedium,
                        color = Color.White
                    )

                    Text(
                        modifier = Modifier.padding(top = 4.dp),
                        text = stringResource(id = R.string.drawer_request_login),
                        style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Normal),
                        color = Color.White,
                        textAlign = TextAlign.Center
                    )

                    Row(modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 24.dp, bottom = 32.dp)
                        .padding(horizontal = 30.dp))
                        {

                        FlexibleImageButton(
                            modifier = Modifier
                                .weight(1f)
                                .padding(end = 9.dp),
                            contentPadding = PaddingValues(bottom = 4.dp),
                            text = "임시 타이틀",
                            textStyle = MaterialTheme.typography.titleSmall,
                            textColor = ColorSet.blue_3370de,
                            pressScale = 0.95f,
                            onClick = onLoginButtonClicked
                        )

                        FlexibleImageButton(
                            modifier = Modifier
                                .weight(1f)
                                .padding(start = 9.dp),
                            contentPadding = PaddingValues(bottom = 4.dp),
                            text = stringResource(id = com.fancymansion.core.common.R.string.login_join_membership),
                            textStyle = MaterialTheme.typography.titleSmall,
                            pressScale = 0.95f,
                            onClick = onJoinMemberButtonClicked
                        )
                    }
                }else{
                    userNickname?.let {
                        Text(
                            modifier = Modifier.padding(top = 10.5.dp, bottom = 38.5.dp),
                            text = userNickname,
                            style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Normal),
                            color = Color.White
                        )
                    }
                }
            }
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .background(color = MaterialTheme.colorScheme.surface),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                if(isUserLogin && menuUserItems != null){
                    Row(
                        modifier = Modifier
                            .padding(horizontal = 20.dp)
                            .padding(top = 22.5.dp)
                            .clip(shape = MaterialTheme.shapes.large)
                            .background(color = ColorSet.sky_f1faff)
                            .padding(horizontal = 8.dp)
                            .padding(vertical = 15.dp)
                    ) {
                        menuUserItems.forEach { menuItem ->
                            MenuButton(
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(1.dp),
                                imageWidthPercent = 0.77f,
                                menuItem = menuItem,
                                textStyle = MaterialTheme.typography.bodyMedium,
                                spacePadding = 10.dp
                            )
                        }
                    }
                }

                menuCommonItems?.let {
                    Row(
                        modifier = Modifier
                            .padding(vertical = 26.dp)
                            .padding(horizontal = 13.dp)
                    ) {
                        menuCommonItems.forEach { menuItem ->
                            MenuButton(
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(2.dp),
                                imageWidthPercent = 0.50f,
                                menuItem = menuItem,
                                textStyle = MaterialTheme.typography.bodyMedium,
                                spacePadding = 9.dp
                            )
                        }
                    }
                }
            }
        }

        if(isUserLogin){
            DefaultButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(47.5.dp),
                textStyle = MaterialTheme.typography.bodyLarge,
                text = stringResource(id = com.fancymansion.core.common.R.string.logout_title),
                colorText = ColorSet.gray_9d9d9d,
                colorBackground = ColorSet.gray_f1f1f1,
                onClick = onLogoutButtonClicked
            )

        }
    }
}

@Composable
fun MenuButton(
    modifier: Modifier = Modifier,
    imageWidthPercent : Float,
    menuItem: MenuItem,
    textStyle: TextStyle = MaterialTheme.typography.bodyLarge,
    spacePadding: Dp
) {
    Column(
        modifier = modifier
            .clip(shape = MaterialTheme.shapes.small)
            .clickSingle(
                indication = rememberRipple(bounded = true),
                onClick = menuItem.onClickMenu
            )
            .padding(3.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            modifier = Modifier
                .fillMaxWidth(imageWidthPercent)
                .padding(bottom = spacePadding),
            painter = painterResource(id = menuItem.imageId),
            contentScale = ContentScale.FillWidth,
            contentDescription = menuItem.title
        )
        Text(
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            text = menuItem.title,
            style = textStyle
        )
    }
}

val menuCommonItems = listOf<MenuItem>()

val menuUserItems = listOf<MenuItem>()

@Preview(device = TABLET_PREVIEW_SPEC)
@Composable
fun FancyMansionDrawerTabletPreview(){
    val drawerState = rememberDrawerState(DrawerValue.Open)
    FancyMansionTheme {
        BaseScreen(
            loadState = LoadState.Idle,
            isOverlayTopBar = true,
            typePane = TypePane.DUAL,
            drawerState = drawerState,
            drawerContent = {
                FancyMansionDrawerTablet(
                    isUserLogin = true,
                    userNickname = "닉네임닉네임닉네임닉네임",
                    onLoginButtonClicked = { },
                    onJoinMemberButtonClicked = { },
                    menuCommonItems = menuCommonItems,
                    menuUserItems = menuUserItems
                )
            },
        ) {
        }
    }
}

@Preview(device = MOBILE_PREVIEW_SPEC)
@Composable
fun FancyMansionDrawerPreview(){
    val drawerState = rememberDrawerState(DrawerValue.Open)
    FancyMansionTheme {
        BaseScreen(
            loadState = LoadState.Idle,
            isOverlayTopBar = true,
            typePane = TypePane.SINGLE,
            drawerState = drawerState,
            drawerContent = {
                FancyMansionDrawer(
                    isUserLogin = true,
                    userNickname = "닉네임닉네임닉네임닉네임",
                    onLoginButtonClicked = { },
                    onJoinMemberButtonClicked = { },
                    menuCommonItems = menuCommonItems,
                    menuUserItems = menuUserItems
                )
            },
        ) {
        }
    }
}
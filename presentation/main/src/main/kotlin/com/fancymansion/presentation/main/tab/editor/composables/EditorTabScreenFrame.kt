package com.fancymansion.presentation.main.tab.editor.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.fancymansion.core.presentation.base.CommonEvent
import com.fancymansion.core.presentation.base.LoadState
import com.fancymansion.core.presentation.base.SIDE_EFFECTS_KEY
import com.fancymansion.core.presentation.base.window.TypePane
import com.fancymansion.core.presentation.compose.component.EnumDropdown
import com.fancymansion.core.presentation.compose.component.FadeInOutSkeleton
import com.fancymansion.core.presentation.compose.frame.tab.TabBaseScreen
import com.fancymansion.core.presentation.compose.modifier.clickSingle
import com.fancymansion.core.presentation.compose.shape.borderLine
import com.fancymansion.presentation.main.R
import com.fancymansion.presentation.main.tab.editor.EditBookSortOrder
import com.fancymansion.presentation.main.tab.editor.EditorTabContract
import com.fancymansion.presentation.main.tab.editor.composables.part.SearchTextField
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach

@Composable
fun EditorTabScreenFrame(
    uiState: EditorTabContract.State,
    loadState: LoadState,
    effectFlow: SharedFlow<EditorTabContract.Effect>?,
    onCommonEventSent: (event: CommonEvent) -> Unit,
    onEventSent: (event: EditorTabContract.Event) -> Unit,
    onNavigationRequested: (EditorTabContract.Effect.Navigation) -> Unit
) {

    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner) {
        val lifecycle = lifecycleOwner.lifecycle
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                onCommonEventSent(CommonEvent.OnResume)
            }
        }

        lifecycle.addObserver(observer)
        onDispose {
            lifecycle.removeObserver(observer)
        }
    }

    LaunchedEffect(SIDE_EFFECTS_KEY) {
        effectFlow?.onEach { effect ->
            if(effect is EditorTabContract.Effect.Navigation){
                onNavigationRequested(effect)
            }
        }?.collect()
    }

    TabBaseScreen(
        loadState = loadState,
        description = EditorTabContract.NAME,
        statusBarColor = MaterialTheme.colorScheme.surface,
        typePane = TypePane.MOBILE,
        initContent = {
            EditorTabSkeletonScreen()
        }
    ) {
        EditorTabScreenContent(
            modifier = Modifier.fillMaxSize(),
            uiState = uiState,
            onEventSent = onEventSent,
            onCommonEventSent = onCommonEventSent
        )
    }
}

@Composable
fun EditorTabSkeletonScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.surface)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 15.5.dp, end = 13.5.dp)
                .padding(vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            SearchTextField(
                modifier = Modifier.fillMaxWidth(0.875f),
                value = "",
                isEnabled = false
            ) {}

            Box(
                modifier = Modifier
                    .padding(start = 10.5.dp)
                    .fillMaxWidth(1f)
                    .height(35.dp)
                    .clickSingle(
                        enabled = false
                    ) {}
            ) {
                Text(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(bottom = 3.5.dp),
                    text = stringResource(R.string.edit_book_search_text_cancel),
                    color = MaterialTheme.colorScheme.onSurface.copy(
                        alpha = 0.6f
                    ),
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontWeight = FontWeight.Medium
                    )
                )
            }
        }

        Column(modifier = Modifier.fillMaxSize()
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
                    text = stringResource(id = R.string.edit_book_bar_title),
                    style = MaterialTheme.typography.titleLarge
                )
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 18.dp, horizontal = 14.dp)
            ) {
                EnumDropdown(
                    modifier = Modifier.width(140.dp),
                    options = EditBookSortOrder.entries.toTypedArray(),
                    selectedOption = EditBookSortOrder.LAST_EDITED,
                    getDisplayName = { "" },
                    isEnabled = false,
                    backgroundColor = MaterialTheme.colorScheme.background
                ) {}

                Row(
                    modifier = Modifier.align(Alignment.CenterEnd),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        modifier = Modifier,
                        text = stringResource(id = R.string.edit_book_header_mode_edit),
                        style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Medium)
                    )
                }
            }

            for (i in 0..2) {
                Row(
                    modifier = Modifier
                        .padding(start = 15.5.dp, end = 18.dp)
                        .padding(vertical = 18.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    FadeInOutSkeleton(
                        modifier = Modifier
                            .width(70.dp)
                            .height(120.dp)
                    )

                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .padding(start = 16.dp)
                    ) {
                        FadeInOutSkeleton(
                            modifier = Modifier
                                .width(210.dp)
                                .height(MaterialTheme.typography.bodyLarge.lineHeight.value.dp)
                        )

                        FadeInOutSkeleton(
                            modifier = Modifier
                                .padding(top = 4.dp)
                                .width(100.dp)
                                .height(MaterialTheme.typography.bodyMedium.lineHeight.value.dp)
                        )

                        FadeInOutSkeleton(
                            modifier = Modifier
                                .padding(top = 3.5.dp)
                                .width(80.dp)
                                .height(MaterialTheme.typography.bodySmall.lineHeight.value.dp)
                        )
                    }
                }

                if (i < 2) {
                    HorizontalDivider(
                        modifier = Modifier
                            .padding(horizontal = 14.dp)
                            .height(0.3.dp)
                            .fillMaxWidth(),
                        color = MaterialTheme.colorScheme.outline
                    )
                }
            }
        }
    }
}
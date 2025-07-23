package com.fancymansion.core.presentation.base.tab

import com.fancymansion.core.presentation.base.CommonEvent
import com.fancymansion.core.presentation.base.LoadState
import com.fancymansion.core.presentation.base.ViewEvent
import com.fancymansion.core.presentation.base.ViewSideEffect
import com.fancymansion.core.presentation.base.ViewState
import kotlinx.coroutines.flow.SharedFlow

data class TabScreenComponents
<S : ViewState, E : ViewEvent, F : ViewSideEffect>(
    val uiState: S,
    val loadState: LoadState,
    val effectFlow: SharedFlow<F>,
    val onEventSent: (E) -> Unit,
    val onCommonEventSent: (CommonEvent) -> Unit,
    val onNavigationRequested: (F) -> Unit
)
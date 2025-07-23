package com.fancymansion.presentation.main.common

import com.fancymansion.presentation.main.tab.editor.EditorTabContract

sealed class MainScreenTab(val tabName: String) {
    object Editor : MainScreenTab(EditorTabContract.NAME)
}
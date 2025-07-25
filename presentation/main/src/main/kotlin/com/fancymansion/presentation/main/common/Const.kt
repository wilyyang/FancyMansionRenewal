package com.fancymansion.presentation.main.common

import com.fancymansion.presentation.main.tab.editor.EditorTabContract

sealed class MainScreenTab(val tabName: String) {
    object Editor : MainScreenTab(EditorTabContract.NAME)

    // TODO : Tab Test
    object Home : MainScreenTab(HomeTabContract.NAME)
    object MyInfo : MainScreenTab(MyInfoTabContract.NAME)
}
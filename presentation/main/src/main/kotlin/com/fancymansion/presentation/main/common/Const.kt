package com.fancymansion.presentation.main.common

import com.fancymansion.presentation.main.R
import com.fancymansion.presentation.main.tab.editor.EditorTabContract

const val BOOKS_PER_PAGE = 10
const val VISIBLE_PAGE_LIMIT = 5

const val QUERY_BOOKS_LIMIT = BOOKS_PER_PAGE * VISIBLE_PAGE_LIMIT
const val EDIT_BOOKS_LIMIT = BOOKS_PER_PAGE * VISIBLE_PAGE_LIMIT

enum class MainScreenTab(val tabName: String, val titleResId: Int, val iconResId: Int, val iconFillResId: Int) {
    Editor(EditorTabContract.NAME, R.string.main_tab_title_editor, R.drawable.ic_main_tab_editor, R.drawable.ic_main_tab_editor_fill),

    // TODO : Tab Test
    Home("main_tab_home", R.string.main_tab_title_home, R.drawable.ic_main_tab_home, R.drawable.ic_main_tab_home_fill),
    Library("main_tab_library", R.string.main_tab_title_library, R.drawable.ic_main_tab_library, R.drawable.ic_main_tab_library_fill),
    MyInfo("main_tab_my_info", R.string.main_tab_title_my_info, R.drawable.ic_main_tab_my_info, R.drawable.ic_main_tab_my_info_fill)
}

enum class ListTarget{
    ALL, SEARCH
}
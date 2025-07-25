package com.fancymansion.app.navigation

import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.fancymansion.core.common.const.ArgName
import com.fancymansion.core.common.const.CONDITION_ID_NOT_ASSIGNED
import com.fancymansion.core.common.const.PAGE_ID_NOT_ASSIGNED
import com.fancymansion.core.common.const.ROUTE_ID_NOT_ASSIGNED
import com.fancymansion.core.common.const.ReadMode
import com.fancymansion.core.common.const.SELECTOR_ID_NOT_ASSIGNED

object NavArgument{
    val argUserId = navArgument(ArgName.NAME_USER_ID){
        type = NavType.StringType
        defaultValue = ""
    }

    val argReadMode = navArgument(ArgName.NAME_READ_MODE){
        type = NavType.EnumType(ReadMode::class.java)
        defaultValue = ReadMode.EDIT
    }

    val argBookId = navArgument(ArgName.NAME_BOOK_ID){
        type = NavType.StringType
        defaultValue = ""
    }

    val argEpisodeId = navArgument(ArgName.NAME_EPISODE_ID){
        type = NavType.StringType
        defaultValue = ""
    }

    val argBookTitle = navArgument(ArgName.NAME_BOOK_TITLE){
        type = NavType.StringType
        defaultValue = ""
    }

    val argEpisodeTitle = navArgument(ArgName.NAME_EPISODE_TITLE){
        type = NavType.StringType
        defaultValue = ""
    }

    val argIsPageListEditMode = navArgument(ArgName.NAME_IS_PAGE_LIST_EDIT_MODE){
        type = NavType.BoolType
        defaultValue = false
    }

    val argPageId = navArgument(ArgName.NAME_PAGE_ID){
        type = NavType.LongType
        defaultValue = PAGE_ID_NOT_ASSIGNED
    }

    val argSelectorId = navArgument(ArgName.NAME_SELECTOR_ID){
        type = NavType.LongType
        defaultValue = SELECTOR_ID_NOT_ASSIGNED
    }

    val argRouteId = navArgument(ArgName.NAME_ROUTE_ID){
        type = NavType.LongType
        defaultValue = ROUTE_ID_NOT_ASSIGNED
    }

    val argConditionId = navArgument(ArgName.NAME_CONDITION_ID){
        type = NavType.LongType
        defaultValue = CONDITION_ID_NOT_ASSIGNED
    }

    val argIsSelectorListEditMode = navArgument(ArgName.NAME_IS_SELECTOR_LIST_EDIT_MODE){
        type = NavType.BoolType
        defaultValue = false
    }
}
package com.fancymansion.app.navigation

import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.fancymansion.core.common.const.ArgName
import com.fancymansion.core.common.const.ReadMode

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
}
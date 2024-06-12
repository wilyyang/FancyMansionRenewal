package com.fancymansion.app.navigation

import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.fancymansion.core.common.const.ArgName
import com.fancymansion.core.common.const.FindAccountType

object NavArgument{
    val argCurriculumId = navArgument(ArgName.nameCurriculumId){
        type = NavType.StringType
        defaultValue = ""
    }

    val argFindType = navArgument(ArgName.nameFindAccountType){
        type = NavType.EnumType(type = FindAccountType::class.java)
        defaultValue = FindAccountType.None
    }

    val argIsFirstScreen = navArgument(ArgName.nameIsFirstScreen) {
        type = NavType.BoolType
        defaultValue = false
    }
}
package com.fancymansion.domain.usecase.util

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class UseCaseGetResource @Inject constructor(
    @param:ApplicationContext private val context : Context
) {
    fun string(id: Int) = context.getString(id)
    fun string(id: Int, vararg formatArgs: Any) = context.getString(id, *formatArgs)
    fun drawable(id: Int) = context.getDrawable(id)
}
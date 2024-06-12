package com.fancymansion.domain.interfaceRepository

import com.fancymansion.core.common.wrapper.ApiResult
import com.fancymansion.domain.model.auth.LoginModel

interface AuthRepository {
    suspend fun userLogin(userId: String, password: String): ApiResult<LoginModel>
    suspend fun autoLogin(): ApiResult<LoginModel>

    suspend fun getIsAutoLogin() : Boolean
    suspend fun setIsAutoLogin(isAutoLogin : Boolean)

}
package com.fancymansion.data.repository

import com.fancymansion.core.common.wrapper.ApiResult
import com.fancymansion.data.datasource.network.base.BaseRetrofitNetwork
import com.fancymansion.data.datasource.network.source.dao.AuthRetrofitDao
import com.fancymansion.data.datasource.network.source.model.request.LoginBody
import com.fancymansion.data.datasource.network.source.model.response.asModel
import com.fancymansion.domain.model.auth.LoginModel
import com.fancymansion.domain.interfaceRepository.AuthRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepositoryImpl @Inject constructor(
    private val authRetrofitDao: AuthRetrofitDao
) : AuthRepository, BaseRetrofitNetwork() {

    override suspend fun userLogin(userId: String, password: String): ApiResult<LoginModel> {
        return callApi(
            api = { authRetrofitDao.userLogin(LoginBody(userId, password)) },
            mapping = {
                val message = it.body()?.message
                it.body()?.data?.asModel(token = it.body()?.access_token?:"") ?: LoginModel() }
        )
    }

    override suspend fun autoLogin(): ApiResult<LoginModel> {
        return callApi(
            api = { authRetrofitDao.autoLogin() },
            mapping = {
                val message = it.body()?.message
                it.body()?.data?.asModel(token = it.body()?.access_token?:"") ?: LoginModel() }
        )
    }

    override suspend fun getIsAutoLogin(): Boolean {
        return true
    }

    override suspend fun setIsAutoLogin(isAutoLogin: Boolean) {}
}
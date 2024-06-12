package com.fancymansion.test.fake.datasource

import android.content.Context
import com.fancymansion.data.datasource.network.source.dao.AuthRetrofitDao
import com.fancymansion.data.datasource.network.source.model.BaseResponse
import com.fancymansion.data.datasource.network.source.model.request.LoginBody
import com.fancymansion.data.datasource.network.source.model.response.LoginData
import com.fancymansion.test.fake.data.FakeAuthData
import retrofit2.Response

class AuthRetrofitDaoFake(context: Context) : AuthRetrofitDao {
    private val fakeAuthData = FakeAuthData(context)

    override suspend fun userLogin(body: LoginBody): Response<BaseResponse<LoginData>> {
        return Response.success(fakeAuthData.getDataUserLoginSuccessUser1())
    }

    override suspend fun autoLogin(): Response<BaseResponse<LoginData>> {
        return Response.success(fakeAuthData.getDataAutoLoginSuccessUser1())
    }
}
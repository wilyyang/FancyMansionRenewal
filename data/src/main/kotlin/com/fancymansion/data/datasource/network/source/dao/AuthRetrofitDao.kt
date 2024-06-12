package com.fancymansion.data.datasource.network.source.dao

import com.fancymansion.data.datasource.network.source.model.BaseResponse
import com.fancymansion.data.datasource.network.source.model.request.LoginBody
import com.fancymansion.data.datasource.network.source.model.response.LoginData
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface AuthRetrofitDao {
    @POST("/api/v1/auth/login")
    suspend fun userLogin(@Body body : LoginBody) : Response<BaseResponse<LoginData>>

    @GET("/api/v1/auth/me")
    suspend fun autoLogin() : Response<BaseResponse<LoginData>>
}
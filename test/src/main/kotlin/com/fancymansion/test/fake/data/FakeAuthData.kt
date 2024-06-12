package com.fancymansion.test.fake.data

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.fancymansion.data.datasource.network.source.model.BaseResponse
import com.fancymansion.data.datasource.network.source.model.response.LoginData
import com.fancymansion.test.R
import com.fancymansion.test.common.FileReader
import java.lang.reflect.Type


class FakeAuthData (private val context : Context) {
    private fun getJsonUserLoginSuccessUser1() = FileReader.readRaw(context, R.raw.user_login_success_user1)
    private fun getJsonAutoLoginSuccessUser1() = FileReader.readRaw(context, R.raw.auto_login_success_user1)

    var loginResponseType: Type = object : TypeToken<BaseResponse<LoginData>>() {}.type


    fun getDataUserLoginSuccessUser1() : BaseResponse<LoginData> = Gson()
        .fromJson(getJsonUserLoginSuccessUser1(), loginResponseType)

    fun getDataAutoLoginSuccessUser1() : BaseResponse<LoginData> = Gson()
        .fromJson(getJsonAutoLoginSuccessUser1(), loginResponseType)
}
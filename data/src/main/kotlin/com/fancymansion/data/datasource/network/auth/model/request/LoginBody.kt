package com.fancymansion.data.datasource.network.auth.model.request

import com.google.gson.annotations.SerializedName

data class LoginBody(
    @SerializedName("id") private val userId : String,
    @SerializedName("password") private val  password : String
)
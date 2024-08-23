package com.fancymansion.data.datasource.network.base

import android.content.Context
import android.os.Build
import com.fancymansion.data.datasource.datastore.AuthDatastore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class AuthenticationInterceptor @Inject constructor(
    @ApplicationContext context : Context,
    private val authDatastore : AuthDatastore
) : Interceptor {

    private val app_name = "FANCY_MANSION_APP_AOS"

    private val app_version =
        context.packageManager.getPackageInfo(context.packageName, 0).versionName
    private val device_name = Build.MODEL
    private val os_name = "Android"
    private val os_version = Build.VERSION.RELEASE

    private val token : String
        get() = runBlocking {
            authDatastore.getToken()
        }

    @Throws(IOException::class)
    override fun intercept(chain : Interceptor.Chain) : Response {
        val request = chain.request().newBuilder()
            .addHeader(
                "api-user-info",
                "$app_name:$app_version/$device_name/$os_name:$os_version"
            ).apply {
                if (token.isNotBlank()) {
                    addHeader("Authorization", "Bearer $token")
                }
            }.build()

        return chain.proceed(request)
    }
}
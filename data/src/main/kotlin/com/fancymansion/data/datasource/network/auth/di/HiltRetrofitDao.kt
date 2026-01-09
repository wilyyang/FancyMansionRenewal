package com.fancymansion.data.datasource.network.auth.di

import com.google.gson.GsonBuilder
import com.fancymansion.core.common.const.BASE_URL
import com.fancymansion.data.datasource.network.auth.dao.AuthRetrofitDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object HiltRetrofitDao {

    @Provides
    @Singleton
    internal fun providesAuthRetrofitDao(okHttpCallFactory : Call.Factory) : AuthRetrofitDao =
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .callFactory(okHttpCallFactory)
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
            .build()
            .create(AuthRetrofitDao::class.java)
}
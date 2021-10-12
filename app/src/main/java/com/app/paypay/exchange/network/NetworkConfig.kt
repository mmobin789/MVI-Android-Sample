package com.app.paypay.exchange.network

import com.app.paypay.exchange.repositories.currencylayer.source.remote.CurrencyLayerWebService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object NetworkConfig {

    fun getRetrofit(): Retrofit =
        Retrofit.Builder().addConverterFactory(GsonConverterFactory.create())
            .baseUrl("http://api.currencylayer.com/")
            .client(
                OkHttpClient.Builder()
                    .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                    .build()
            )
            .build()


    fun getCurrencyLayerWebService(retrofit: Retrofit): CurrencyLayerWebService =
        retrofit.create(CurrencyLayerWebService::class.java)
}
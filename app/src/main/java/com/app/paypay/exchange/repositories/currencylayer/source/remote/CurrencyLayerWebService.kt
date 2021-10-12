package com.app.paypay.exchange.repositories.currencylayer.source.remote

import com.app.paypay.exchange.repositories.currencylayer.source.remote.business.CurrencyLayerResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface CurrencyLayerWebService {
    @GET("live")
    suspend fun getLiveExchangeRates(
        @Query("source") sourceCurrency: String,
        @Query("access_key") accessKey: String = "c4fc75d87d55b476d5397eb6c286391d",
    ): CurrencyLayerResponse
}
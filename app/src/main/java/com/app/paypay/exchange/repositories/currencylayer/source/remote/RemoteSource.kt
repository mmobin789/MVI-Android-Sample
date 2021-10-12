package com.app.paypay.exchange.repositories.currencylayer.source.remote

class RemoteSource(private val currencyLayerWebService: CurrencyLayerWebService) {

    suspend fun getLiveExchangeRates(sourceCurrency: String) = currencyLayerWebService.getLiveExchangeRates(sourceCurrency)
}
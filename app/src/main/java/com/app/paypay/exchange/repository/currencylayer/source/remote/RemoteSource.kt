package com.app.paypay.exchange.repository.currencylayer.source.remote

class RemoteSource(private val currencyLayerWebService: CurrencyLayerWebService) {

    suspend fun getLiveExchangeRates(sourceCurrency: String) = currencyLayerWebService.getLiveExchangeRates(sourceCurrency)
}
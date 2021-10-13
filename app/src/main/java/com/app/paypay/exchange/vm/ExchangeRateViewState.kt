package com.app.paypay.exchange.vm

import com.app.paypay.exchange.repositories.currencylayer.source.local.business.ExchangeRate

sealed class ExchangeRateViewState {
    data class ExchangeRates(val exchangeRates: List<ExchangeRate>) : ExchangeRateViewState()
    data class CurrenciesFromAssets(val currencies: List<String>) : ExchangeRateViewState()
    data class Error(val error: String? = null) : ExchangeRateViewState()
    data class Loading(val isLoading: Boolean) : ExchangeRateViewState()
    object Idle : ExchangeRateViewState()
}
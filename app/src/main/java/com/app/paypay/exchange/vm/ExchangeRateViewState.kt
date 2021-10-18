package com.app.paypay.exchange.vm

import androidx.annotation.StringRes
import com.app.paypay.exchange.repository.currencylayer.source.local.business.ExchangeRate

sealed class ExchangeRateViewState {
    data class ExchangeRates(val sourceAmount: Double, val exchangeRates: List<ExchangeRate>) :
        ExchangeRateViewState()

    data class CurrenciesFromAssets(val currencies: List<String>) : ExchangeRateViewState()
    data class Error(val error: String? = null) : ExchangeRateViewState()
    data class GenericError(@StringRes val errorIdRes: Int) : ExchangeRateViewState()
    data class Loading(val isLoading: Boolean) : ExchangeRateViewState()
    object Idle : ExchangeRateViewState()
}
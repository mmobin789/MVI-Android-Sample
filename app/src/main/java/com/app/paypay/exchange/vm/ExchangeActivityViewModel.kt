package com.app.paypay.exchange.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.paypay.exchange.repositories.currencylayer.CurrencyLayerServiceRepository
import com.app.paypay.exchange.usecases.CachedCurrenciesUseCase
import com.app.paypay.exchange.usecases.CurrencyServiceUseCase
import kotlinx.coroutines.launch

class ExchangeActivityViewModel(
    private val currencyServiceUseCase: CurrencyServiceUseCase,
    private val cachedCurrenciesUseCase: CachedCurrenciesUseCase
) : ViewModel() {

    private val viewState = MutableLiveData<ExchangeRateViewState>(ExchangeRateViewState.Idle)

    fun getViewState(): LiveData<ExchangeRateViewState> {
        return viewState
    }

    fun getExchangeRates(sourceCurrency: String) {
        viewModelScope.launch {
            viewState.value = ExchangeRateViewState.Loading(true)

            viewState.value = when (val result = cachedCurrenciesUseCase(sourceCurrency)) {
                is CurrencyLayerServiceRepository.Payload.Success -> {
                    stopLoading()
                    ExchangeRateViewState.Success(result.exchangeRates)
                }
                is CurrencyLayerServiceRepository.Payload.Fail -> runCurrencyServiceUseCase(
                    sourceCurrency
                )

            }


        }
    }

    private fun stopLoading() {
        viewState.value = ExchangeRateViewState.Loading(false)
    }

    private suspend fun runCurrencyServiceUseCase(sourceCurrency: String): ExchangeRateViewState {
        return when (val result = currencyServiceUseCase(sourceCurrency)) {
            is CurrencyLayerServiceRepository.Payload.Success -> {
                stopLoading()
                ExchangeRateViewState.Success(result.exchangeRates)
            }
            is CurrencyLayerServiceRepository.Payload.Fail -> {
                stopLoading()
                ExchangeRateViewState.Error(result.error)
            }


        }

    }

}
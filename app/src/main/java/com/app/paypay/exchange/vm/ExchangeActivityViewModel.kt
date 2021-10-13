package com.app.paypay.exchange.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.paypay.exchange.repositories.currencylayer.CurrencyLayerServiceRepository
import com.app.paypay.exchange.usecases.CachedCurrenciesUseCase
import com.app.paypay.exchange.usecases.CurrencyServiceUseCase
import com.app.paypay.exchange.usecases.GetCurrenciesViaAssetsUseCase
import kotlinx.coroutines.launch

class ExchangeActivityViewModel(
    private val currencyServiceUseCase: CurrencyServiceUseCase,
    private val cachedCurrenciesUseCase: CachedCurrenciesUseCase,
    private val getCurrenciesViaAssetsUseCase: GetCurrenciesViaAssetsUseCase
) : ViewModel() {

    private val viewState = MutableLiveData<ExchangeRateViewState>(ExchangeRateViewState.Idle)

    fun getViewState(): LiveData<ExchangeRateViewState> {
        return viewState
    }

    fun getExchangeRates(sourceCurrency: String) {
        viewModelScope.launch {
            startLoading()
            viewState.value = when (val result = cachedCurrenciesUseCase(sourceCurrency)) {
                is CurrencyLayerServiceRepository.Payload.ExchangeRates.Success -> {
                    stopLoading()
                    ExchangeRateViewState.ExchangeRates(result.exchangeRates)
                }
                is CurrencyLayerServiceRepository.Payload.ExchangeRates.Fail -> runCurrencyServiceUseCase(
                    sourceCurrency
                )
                else -> throw NotImplementedError()
            }


        }
    }

    fun getCurrenciesFromAssets() {
        viewModelScope.launch {
            startLoading()
           viewState.value =  when (val result = getCurrenciesViaAssetsUseCase()) {
                is CurrencyLayerServiceRepository.Payload.CurrencyInfoViaAssets.Fail -> {
                    stopLoading()
                    ExchangeRateViewState.Error(result.error)
                }
                is CurrencyLayerServiceRepository.Payload.CurrencyInfoViaAssets.Success -> {
                    stopLoading()
                    ExchangeRateViewState.CurrenciesFromAssets(result.currencies)
                }
               else -> throw NotImplementedError()
           }
        }
    }

    private fun stopLoading() {
        viewState.value = ExchangeRateViewState.Loading(isLoading = false)
    }

    private fun startLoading() {
        viewState.value = ExchangeRateViewState.Loading(isLoading = true)
    }

    private suspend fun runCurrencyServiceUseCase(sourceCurrency: String): ExchangeRateViewState {
        return when (val result = currencyServiceUseCase(sourceCurrency)) {
            is CurrencyLayerServiceRepository.Payload.ExchangeRates.Success -> {
                stopLoading()
                ExchangeRateViewState.ExchangeRates(result.exchangeRates)
            }
            is CurrencyLayerServiceRepository.Payload.ExchangeRates.Fail -> {
                stopLoading()
                ExchangeRateViewState.Error(result.error)
            }
            else -> { // to avoid when exhaustion error.
                throw NotImplementedError()
            }
        }

    }

}
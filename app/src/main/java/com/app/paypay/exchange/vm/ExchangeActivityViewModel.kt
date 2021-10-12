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
    private val data = MutableLiveData<ExchangeRateViewState>(ExchangeRateViewState.Idle)

    fun getViewState() = data as LiveData<ExchangeRateViewState>

    fun getExchangeRates(sourceCurrency: String) {
        viewModelScope.launch {
            data.value = ExchangeRateViewState.Loading(true)

            data.postValue(
                when (val result = cachedCurrenciesUseCase(sourceCurrency)) {
                    is CurrencyLayerServiceRepository.Payload.Success -> {
                        ExchangeRateViewState.Success(result.exchangeRates)
                    }
                    is CurrencyLayerServiceRepository.Payload.Fail -> runCurrencyServiceUseCase(
                        sourceCurrency
                    )

                }
            )

            stopLoading()


        }
    }

    private fun stopLoading() = data.postValue(ExchangeRateViewState.Loading(false))

    private suspend fun runCurrencyServiceUseCase(sourceCurrency: String): ExchangeRateViewState {
        return when (val result = currencyServiceUseCase(sourceCurrency)) {
            is CurrencyLayerServiceRepository.Payload.Success -> {
                ExchangeRateViewState.Success(result.exchangeRates)
            }
            is CurrencyLayerServiceRepository.Payload.Fail -> {
                ExchangeRateViewState.Error(result.error)
            }


        }

    }

}
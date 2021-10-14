package com.app.paypay.exchange.usecases

import com.app.paypay.exchange.repositories.currencylayer.CurrencyLayerServiceRepository
import java.util.concurrent.TimeUnit

class CachedCurrenciesUseCase(
    private val repository: CurrencyLayerServiceRepository
) {

    suspend operator fun invoke(
        sourceCurrency: String,
        sourceAmount: Double
    ): CurrencyLayerServiceRepository.Payload {

        val payload = repository.getCachedExchangeRates(sourceCurrency, sourceAmount)
        refreshCurrenciesIfRequired(payload)
        return payload
    }

    private suspend fun refreshCurrenciesIfRequired(
        payload: CurrencyLayerServiceRepository.Payload
    ) {
        // no other currencies are available via currency layer except USD.

        if (payload is CurrencyLayerServiceRepository.Payload.ExchangeRates.Success) {
            val savedTimestamp = payload.exchangeRates[0].timestamp
            val currentTimeStamp = System.currentTimeMillis()
            val minutesDiffValid =
                TimeUnit.MILLISECONDS.toMinutes(currentTimeStamp) - TimeUnit.MILLISECONDS.toMinutes(
                    savedTimestamp
                ) >= 30
            if (minutesDiffValid)
                repository.deleteCachedExchangeRates()


        }
    }

}
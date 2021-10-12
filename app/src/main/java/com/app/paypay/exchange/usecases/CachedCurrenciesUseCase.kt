package com.app.paypay.exchange.usecases

import com.app.paypay.exchange.repositories.currencylayer.CurrencyLayerServiceRepository

class CachedCurrenciesUseCase(private val repository: CurrencyLayerServiceRepository) {

    suspend operator fun invoke(sourceCurrency: String): CurrencyLayerServiceRepository.Payload {
        return repository.getCachedExchangeRates(sourceCurrency)
    }
}
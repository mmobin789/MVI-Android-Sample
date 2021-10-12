package com.app.paypay.exchange.usecases

import com.app.paypay.exchange.repositories.currencylayer.CurrencyLayerServiceRepository

class CurrencyServiceUseCase(private val repository: CurrencyLayerServiceRepository) {

    suspend operator fun invoke(sourceCurrency: String): CurrencyLayerServiceRepository.Payload {
        return repository.getLiveExchangeRates(sourceCurrency)
    }
}
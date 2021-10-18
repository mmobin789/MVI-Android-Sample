package com.app.paypay.exchange.usecases

import com.app.paypay.exchange.repository.currencylayer.CurrencyLayerServiceRepository

class CurrencyServiceUseCase(private val repository: CurrencyLayerServiceRepository) {

    suspend operator fun invoke(
        sourceCurrency: String,
        sourceAmount: Double
    ): CurrencyLayerServiceRepository.Payload {
        return repository.getLiveExchangeRates(sourceCurrency,sourceAmount)
    }
}
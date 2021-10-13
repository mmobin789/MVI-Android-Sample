package com.app.paypay.exchange.usecases

import com.app.paypay.exchange.repositories.currencylayer.CurrencyLayerServiceRepository

class GetCurrenciesViaAssetsUseCase(private val repository: CurrencyLayerServiceRepository) {

    suspend operator fun invoke(): CurrencyLayerServiceRepository.Payload {
        return repository.getCurrenciesFromAssets()
    }
}
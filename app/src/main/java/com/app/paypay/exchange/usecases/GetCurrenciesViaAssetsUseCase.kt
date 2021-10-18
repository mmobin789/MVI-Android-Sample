package com.app.paypay.exchange.usecases

import com.app.paypay.exchange.repository.currencylayer.CurrencyLayerServiceRepository

class GetCurrenciesViaAssetsUseCase(private val repository: CurrencyLayerServiceRepository) {

    suspend operator fun invoke(): CurrencyLayerServiceRepository.Payload {
        return repository.getCurrenciesFromAssets()
    }
}
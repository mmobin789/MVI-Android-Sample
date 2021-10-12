package com.app.paypay.exchange.repositories.currencylayer

import com.app.paypay.exchange.repositories.currencylayer.source.local.LocalSource
import com.app.paypay.exchange.repositories.currencylayer.source.local.business.ExchangeRate
import com.app.paypay.exchange.repositories.currencylayer.source.remote.RemoteSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class CurrencyLayerServiceRepository(
    private val remoteSource: RemoteSource,
    private val localSource: LocalSource
) {
    suspend fun getLiveExchangeRates(sourceCurrency: String): Payload {
        return withContext(Dispatchers.IO) {
            try {
                val response = remoteSource.getLiveExchangeRates(sourceCurrency)
                val quotes = response.quotes.values.toList()
                when (localSource.addExchangeRates(sourceCurrency, quotes)) {
                    true -> Payload.Success(
                        localSource.getExchangeRatesForSourceCurrency(
                            sourceCurrency
                        )
                    )
                    else -> Payload.Fail("Failed to insert exchange rates in Database.")
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Payload.Fail(e.message)
            }
        }
    }

    suspend fun getCachedExchangeRates(sourceCurrency: String): Payload {
        return withContext(Dispatchers.IO) {
            val exchangeRates = localSource.getExchangeRatesForSourceCurrency(sourceCurrency)
            when (exchangeRates.isNotEmpty()) {
                true -> Payload.Success(exchangeRates)
                else -> Payload.Fail("Exchange rates not available in database.")
            }
        }
    }

    sealed class Payload {
        data class Success(val exchangeRates: List<ExchangeRate>) : Payload()
        data class Fail(val error: String? = null) : Payload()
    }

}
package com.app.paypay.exchange.repositories.currencylayer

import com.app.paypay.exchange.repositories.currencylayer.source.app.AssetSource
import com.app.paypay.exchange.repositories.currencylayer.source.local.LocalSource
import com.app.paypay.exchange.repositories.currencylayer.source.local.business.ExchangeRate
import com.app.paypay.exchange.repositories.currencylayer.source.remote.RemoteSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class CurrencyLayerServiceRepository(
    private val remoteSource: RemoteSource,
    private val localSource: LocalSource,
    private val assetSource: AssetSource
) {
    suspend fun getLiveExchangeRates(sourceCurrency: String): Payload {
        return withContext(Dispatchers.IO) {
            try {
                val response = remoteSource.getLiveExchangeRates(sourceCurrency)
                response.quotes?.run {
                    when (localSource.addExchangeRates(sourceCurrency, this)) {
                        true -> Payload.ExchangeRates.Success(
                            localSource.getExchangeRatesForSourceCurrency(
                                sourceCurrency
                            )
                        )
                        else -> Payload.ExchangeRates.Fail("Failed to insert exchange rates in Database.")
                    }
                } ?: run {
                    //todo return a dummy rate here for calculation
                    Payload.ExchangeRates.Fail("Failed to get exchange rates.")
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Payload.ExchangeRates.Fail(e.message)
            }
        }
    }

    suspend fun getCachedExchangeRates(sourceCurrency: String): Payload {
        return withContext(Dispatchers.IO) {
            val exchangeRates = localSource.getExchangeRatesForSourceCurrency(sourceCurrency)
            when (exchangeRates.isNotEmpty()) {
                true -> Payload.ExchangeRates.Success(exchangeRates)
                else -> Payload.ExchangeRates.Fail("Exchange rates not available in database.")
            }
        }
    }

    suspend fun getCurrenciesFromAssets(): Payload {
        return withContext(Dispatchers.IO) {
            val currencies = assetSource.getCurrenciesFromAssets()
            when (currencies.isNotEmpty()) {
                true -> Payload.CurrencyInfoViaAssets.Success(currencies)
                else -> Payload.CurrencyInfoViaAssets.Fail("Exchange rates not available in database.")
            }
        }
    }

    sealed class Payload {
        sealed class ExchangeRates : Payload() {
            data class Success(val exchangeRates: List<ExchangeRate>) : ExchangeRates()
            data class Fail(val error: String? = null) : ExchangeRates()
        }

        sealed class CurrencyInfoViaAssets : Payload() {
            data class Success(val currencies: List<String>) : CurrencyInfoViaAssets()
            data class Fail(val error: String? = null) : CurrencyInfoViaAssets()
        }
    }

}
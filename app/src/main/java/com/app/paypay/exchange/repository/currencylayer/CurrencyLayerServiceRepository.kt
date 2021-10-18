package com.app.paypay.exchange.repository.currencylayer

import com.app.paypay.exchange.repository.currencylayer.source.app.AssetSource
import com.app.paypay.exchange.repository.currencylayer.source.local.LocalSource
import com.app.paypay.exchange.repository.currencylayer.source.local.business.ExchangeRate
import com.app.paypay.exchange.repository.currencylayer.source.remote.RemoteSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.random.Random

class CurrencyLayerServiceRepository(
    private val remoteSource: RemoteSource,
    private val localSource: LocalSource,
    private val assetSource: AssetSource
) {
    suspend fun getLiveExchangeRates(sourceCurrency: String, sourceAmount: Double): Payload {
        return withContext(Dispatchers.IO) {
            try {
                val response = remoteSource.getLiveExchangeRates(sourceCurrency)
                response.quotes?.run {
                    when (localSource.addExchangeRates(sourceCurrency, sourceAmount, this)) {
                        true -> Payload.ExchangeRates.Success(
                            localSource.getExchangeRatesForSourceCurrency(
                                sourceCurrency,
                                sourceAmount
                            )
                        )
                        else -> Payload.ExchangeRates.Fail("Failed to insert exchange rates in Database.")
                    }
                } ?: run {
                    /* returns dummy rates here for calculation as they are not available in currency layer api.
                     This has been handled non-ideally here as it's not a valid use case to save time and effort.
                     */
                    val dummyExchangeRates =
                        assetSource.getCurrenciesFromAssets().map { destinationCurrency ->
                            ExchangeRate(
                                sourceAmount,
                                sourceCurrency,
                                destinationCurrency,
                                exchangeRate = Random.nextDouble()
                            )
                        }
                    Payload.ExchangeRates.Success(dummyExchangeRates)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Payload.ExchangeRates.Fail(e.message)
            }
        }
    }

    suspend fun getCachedExchangeRates(sourceCurrency: String, sourceAmount: Double): Payload {
        return withContext(Dispatchers.IO) {
            val exchangeRates =
                localSource.getExchangeRatesForSourceCurrency(sourceCurrency, sourceAmount)
            when (exchangeRates.isNotEmpty()) {
                true -> Payload.ExchangeRates.Success(exchangeRates)
                else -> Payload.ExchangeRates.Fail("Exchange rates not available in database.")
            }
        }
    }

    suspend fun deleteCachedExchangeRates(): Payload {
        return withContext(Dispatchers.IO) {
            val deletedExchangeRates = localSource.deleteCachedExchangeRates()
            when (deletedExchangeRates > 0) {
                true -> Payload.ExchangeRates.Success(emptyList())
                else -> Payload.ExchangeRates.Fail("Exchange rates not available in database.")
            }
        }
    }

    suspend fun getCurrenciesFromAssets(): Payload {
        return withContext(Dispatchers.IO) {
            val currencies = assetSource.getCurrenciesFromAssets()
            when (currencies.isNotEmpty()) {
                true -> Payload.CurrencyInfoViaAssets.Success(currencies)
                else -> Payload.CurrencyInfoViaAssets.Fail("Exchange rates not available in app.")
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
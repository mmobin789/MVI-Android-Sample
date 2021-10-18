package com.app.paypay.repository.currencylayer.source.local

import com.app.paypay.exchange.repository.currencylayer.source.local.LocalSource
import com.app.paypay.exchange.repository.currencylayer.source.local.business.ExchangeRate
import io.mockk.every
import io.mockk.mockk
import org.junit.Assert
import org.junit.Test

class CurrencyLayerLocalSource {

    private val localSourceMock = mockk<LocalSource>()

    private companion object {
        const val dummyCurrencySource = "USD"
        const val dummySourceAmount = Double.MAX_VALUE
        val dummyQuotes = linkedMapOf<String, Double>()
    }

    @Test
    fun addExchangeRatesSuccess() {
        every {
            localSourceMock.addExchangeRates(
                sourceCurrency = dummyCurrencySource,
                sourceAmount = dummySourceAmount,
                quotes = dummyQuotes
            )
        } returns true

        Assert.assertTrue(
            localSourceMock.addExchangeRates(
                sourceCurrency = dummyCurrencySource, sourceAmount = dummySourceAmount,
                quotes = dummyQuotes
            )
        )

    }

    @Test
    fun addExchangeRatesFailed() {
        every {
            localSourceMock.addExchangeRates(
                sourceCurrency = dummyCurrencySource,
                sourceAmount = dummySourceAmount,
                quotes = dummyQuotes
            )
        } returns false

        Assert.assertFalse(
            localSourceMock.addExchangeRates(
                sourceCurrency = dummyCurrencySource, sourceAmount = dummySourceAmount,
                quotes = dummyQuotes
            )
        )

    }

    @Test
    fun getExchangeRatesForSourceCurrencySuccess() {

        val exchangeRates: List<ExchangeRate> = mockk()

        every {
            localSourceMock.getExchangeRatesForSourceCurrency(
                sourceCurrency = dummyCurrencySource,
                sourceAmount = dummySourceAmount
            )


        } returns exchangeRates


        every { exchangeRates.isEmpty() } returns false

        Assert.assertTrue(
            localSourceMock.getExchangeRatesForSourceCurrency(
                sourceCurrency = dummyCurrencySource,
                sourceAmount = dummySourceAmount
            ).isNotEmpty()
        )
    }

    @Test
    fun getExchangeRatesForSourceCurrencyFailed() {
        val exchangeRates: List<ExchangeRate> = mockk()

        every {
            localSourceMock.getExchangeRatesForSourceCurrency(
                sourceCurrency = dummyCurrencySource,
                sourceAmount = dummySourceAmount
            )


        } returns exchangeRates


        every { exchangeRates.isEmpty() } returns true

        Assert.assertTrue(
            localSourceMock.getExchangeRatesForSourceCurrency(
                sourceCurrency = dummyCurrencySource,
                sourceAmount = dummySourceAmount
            ).isEmpty()
        )
    }

    @Test
    fun deleteCachedExchangeRatesSuccess() {
        val deletedEntries = 1

        every {
            localSourceMock.deleteCachedExchangeRates()
        } returns deletedEntries


        Assert.assertTrue(
            localSourceMock.deleteCachedExchangeRates(
            ) == deletedEntries
        )
    }

    @Test
    fun deleteCachedExchangeRatesFailed() {
        val deletedEntries = 0

        every {
            localSourceMock.deleteCachedExchangeRates()
        } returns deletedEntries


        Assert.assertTrue(
            localSourceMock.deleteCachedExchangeRates(
            ) == deletedEntries
        )
    }

}
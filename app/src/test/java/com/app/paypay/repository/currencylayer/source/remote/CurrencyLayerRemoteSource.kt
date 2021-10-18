package com.app.paypay.repository.currencylayer.source.remote

import com.app.paypay.exchange.repositories.currencylayer.source.remote.RemoteSource
import com.app.paypay.exchange.repositories.currencylayer.source.remote.business.CurrencyLayerResponse
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import org.junit.Assert
import org.junit.Test

class CurrencyLayerRemoteSource {

    private val apiCallMock = mockk<RemoteSource>()

    private companion object {
        const val dummyCurrencySource = "USD"
    }

    @Test
    fun getLiveExchangeRatesSuccess() {
        val successResponse = mockk<CurrencyLayerResponse>()
        every { successResponse.success } returns true
        every { successResponse.quotes } returns mockk()
        every { successResponse.quotes?.isEmpty() } returns false
        coEvery { apiCallMock.getLiveExchangeRates(sourceCurrency = dummyCurrencySource) } returns successResponse
        Assert.assertTrue(
            successResponse.success
                    && successResponse.quotes?.isNotEmpty() == true
        )

    }

    @Test
    fun getLiveExchangeRatesFailed() {
        val failResponse = mockk<CurrencyLayerResponse>()
        every { failResponse.success } returns false
        every { failResponse.quotes } returns null
        coEvery { apiCallMock.getLiveExchangeRates(sourceCurrency = dummyCurrencySource) } returns failResponse
        Assert.assertFalse(
            failResponse.success
        )

    }
}
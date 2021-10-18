package com.app.paypay.repository.currencylayer.source.app

import com.app.paypay.exchange.repositories.currencylayer.source.app.AssetSource
import io.mockk.every
import io.mockk.mockk
import org.junit.Assert
import org.junit.Test

class CurrencyLayerAssetSource {

    private val assetSourceMock = mockk<AssetSource>()

    @Test
    fun getCurrenciesFromAssetsSuccess() {
        val currencies = mockk<List<String>>()
        every {
            assetSourceMock.getCurrenciesFromAssets()
        } returns currencies

        every { currencies.isEmpty() } returns false

        Assert.assertTrue(currencies.isNotEmpty())
    }

    @Test
    fun getCurrenciesFromAssetsFailed() {
        val currencies = mockk<List<String>>()
        every {
            assetSourceMock.getCurrenciesFromAssets()
        } returns currencies

        every { currencies.isEmpty() } returns true

        Assert.assertTrue(currencies.isEmpty())
    }
}
package com.app.paypay.database

import androidx.room.Room
import androidx.test.platform.app.InstrumentationRegistry
import com.app.paypay.exchange.repository.currencylayer.source.local.business.ExchangeRate
import com.app.paypay.exchange.repository.currencylayer.source.local.dao.CurrencyDao
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class CurrencyDao {

    private lateinit var currencyDao: CurrencyDao


    @Before
    fun createDB() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        val offlineDatabase =
            Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java).build()
        currencyDao = offlineDatabase.currencyDao()
    }

    @Test
    fun addExchangeRatesForSourceCurrency() {
        Assert.assertTrue(
            currencyDao.insertAll(
                listOf(
                    ExchangeRate(
                        sourceAmount = 12.31,
                        sourceCurrency = "USD", destinationCurrency = "YEN",
                        exchangeRate = 3.44
                    )
                )
            ).isNotEmpty()
        )
    }

    @Test
    fun getExchangeRatesForSourceCurrency() {
        currencyDao.insertAll(
            listOf(
                ExchangeRate(
                    sourceAmount = 12.31,
                    sourceCurrency = "USD", destinationCurrency = "YEN",
                    exchangeRate = 3.44
                )
            )
        )
        Assert.assertTrue(currencyDao.getExchangeRatesForSourceCurrency("USD", 12.31).isNotEmpty())
    }

    @Test
    fun deleteExchangeRates() {
        currencyDao.insert(
            ExchangeRate(
                sourceAmount = 12.31,
                sourceCurrency = "USD", destinationCurrency = "YEN",
                exchangeRate = 3.44
            )
        )
        Assert.assertEquals(currencyDao.deleteAll(), 1)
    }
}
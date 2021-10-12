package com.app.paypay.exchange.repositories.currencylayer.source.local

import com.app.paypay.exchange.repositories.currencylayer.source.local.business.ExchangeRate
import com.app.paypay.exchange.repositories.currencylayer.source.local.dao.CurrencyDao

class LocalSource(private val currencyDao: CurrencyDao) {

    fun addExchangeRates(sourceCurrency: String, quotes: LinkedHashMap<String, Double>): Boolean {
        val rates = quotes.map {
            ExchangeRate(
                sourceCurrency = sourceCurrency, destinationCurrency = it.key.substring(3),
                exchangeRate = it.value
            )
        }
        return currencyDao.insertAll(rates).isNotEmpty()
    }

    fun getExchangeRatesForSourceCurrency(sourceCurrency: String) =
        currencyDao.getExchangeRatesForSourceCurrency(sourceCurrency)
}
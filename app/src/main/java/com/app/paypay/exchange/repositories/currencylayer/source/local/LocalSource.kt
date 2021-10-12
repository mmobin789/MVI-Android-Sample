package com.app.paypay.exchange.repositories.currencylayer.source.local

import com.app.paypay.exchange.repositories.currencylayer.source.local.business.ExchangeRate
import com.app.paypay.exchange.repositories.currencylayer.source.local.dao.CurrencyDao
import com.app.paypay.exchange.repositories.currencylayer.source.remote.business.Quote

class LocalSource(private val currencyDao: CurrencyDao) {

    fun addExchangeRates(sourceCurrency: String, quotes: List<Quote>): Boolean {
        return currencyDao.insertAll(quotes.map {
            ExchangeRate(sourceCurrency, it)
        }).isNotEmpty()
    }

    fun getExchangeRatesForSourceCurrency(sourceCurrency: String) =
        currencyDao.getExchangeRatesForSourceCurrency(sourceCurrency)
}
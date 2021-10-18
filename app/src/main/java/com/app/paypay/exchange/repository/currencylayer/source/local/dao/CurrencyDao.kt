package com.app.paypay.exchange.repository.currencylayer.source.local.dao

import androidx.room.Dao
import androidx.room.Query
import com.app.paypay.base.dao.BaseDao
import com.app.paypay.exchange.repository.currencylayer.source.local.business.ExchangeRate

@Dao
interface CurrencyDao : BaseDao<ExchangeRate> {
    @Query("SELECT * FROM exchange_rate WHERE sourceCurrency = :sourceCurrency AND sourceAmount = :sourceAmount")
    fun getExchangeRatesForSourceCurrency(
        sourceCurrency: String,
        sourceAmount: Double
    ): List<ExchangeRate>

    @Query("DELETE FROM exchange_rate")
    fun deleteAll(): Int
}
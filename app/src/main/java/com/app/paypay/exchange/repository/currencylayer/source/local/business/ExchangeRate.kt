package com.app.paypay.exchange.repository.currencylayer.source.local.business

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "exchange_rate")
data class ExchangeRate(
    val sourceAmount: Double,
    val sourceCurrency: String,
    val destinationCurrency: String,
    val exchangeRate: Double,
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val timestamp: Long = System.currentTimeMillis()
)
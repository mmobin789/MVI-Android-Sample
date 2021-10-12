package com.app.paypay.exchange.repositories.currencylayer.source.local.business

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.app.paypay.exchange.repositories.currencylayer.source.remote.business.Quote

@Entity(tableName = "exchange_rate")
data class ExchangeRate(
    val sourceCurrency: String,
    @Embedded val quote: Quote,
    @PrimaryKey(autoGenerate = true)
    var id: Long = -1
    )
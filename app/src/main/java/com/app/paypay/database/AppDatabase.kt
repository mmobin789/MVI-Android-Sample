package com.app.paypay.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.app.paypay.exchange.repositories.currencylayer.source.local.dao.CurrencyDao
import com.app.paypay.exchange.repositories.currencylayer.source.local.business.ExchangeRate

@Database(entities = [ExchangeRate::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun currencyDao(): CurrencyDao
}
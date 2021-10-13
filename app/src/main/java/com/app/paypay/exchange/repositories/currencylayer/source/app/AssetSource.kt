package com.app.paypay.exchange.repositories.currencylayer.source.app

import android.content.Context
import com.app.paypay.utils.loadCurrenciesFromAssets

class AssetSource(private val context: Context) {

   fun getCurrenciesFromAssets() = context.loadCurrenciesFromAssets()
}
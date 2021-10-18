package com.app.paypay.exchange.repository.currencylayer.source.app

import android.content.Context
import com.app.paypay.utils.loadCurrenciesFromAssets

class AssetSource(private val context: Context) {

   fun getCurrenciesFromAssets() = context.loadCurrenciesFromAssets()
}
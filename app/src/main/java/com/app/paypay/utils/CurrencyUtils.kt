package com.app.paypay.utils

import android.content.Context
import com.app.paypay.exchange.repository.currencylayer.source.local.business.ExchangeRate
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException
import java.io.InputStream

/**
 * This method can be used to read a json file.
 * For now it specifically reads movies.json file from assets folder.
 */
fun Context.loadCurrenciesFromAssets(): List<String> {
    try {
        val `is`: InputStream = assets.open("currencies.json")
        val size: Int = `is`.available()
        val buffer = ByteArray(size)
        `is`.read(buffer)
        `is`.close()
        val json = JSONObject(String(buffer))
        val currencies = ArrayList<String>(180)
        currencies.addAll(json.keys().asSequence())
        return currencies

    } catch (e: IOException) {
        e.printStackTrace()
    } catch (e: JSONException) {
        e.printStackTrace()
    }

    return emptyList()
}

fun ExchangeRate.getConvertedAmount() = "${sourceAmount * exchangeRate}"
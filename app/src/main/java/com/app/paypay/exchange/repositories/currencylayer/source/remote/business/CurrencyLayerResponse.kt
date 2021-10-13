package com.app.paypay.exchange.repositories.currencylayer.source.remote.business

data class CurrencyLayerResponse(
    val success: Boolean,
    val change: String,
    val startDate: String,
    val endDate: String,
    val source: String,
    val quotes: LinkedHashMap<String, Double>?
)
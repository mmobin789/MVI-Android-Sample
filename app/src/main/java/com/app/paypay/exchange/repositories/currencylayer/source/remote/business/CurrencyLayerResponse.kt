package com.app.paypay.exchange.repositories.currencylayer.source.remote.business

data class CurrencyLayerResponse(
    val success: Boolean = false,
    val change: String? = null,
    val startDate: String? = null,
    val endDate: String? = null,
    val source: String? = null,
    val quotes: LinkedHashMap<String, Double>? = null
)
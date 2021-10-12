package com.app.paypay.exchange.repositories.currencylayer.source.remote.business

import com.google.gson.annotations.SerializedName

data class Quote(
    val change: String,
    @SerializedName("change_pct")
    val changePCT: String,
    val startDate: String,
    val endDate: String
)
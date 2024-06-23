package com.zacker.bookmaster.model

import com.google.gson.annotations.SerializedName

data class PaymentIntentRequest(
    @SerializedName("price") val price: Double
)

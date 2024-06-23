package com.zacker.bookmaster.model

import com.google.gson.annotations.SerializedName

data class PaymentIntentResponse(
    @SerializedName("clientSecret") val clientSecret: String
)

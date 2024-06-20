package com.zacker.bookmaster.model

data class PaymentIntentRequest(
    val amount: Int,
    val cartItems: List<String>
)

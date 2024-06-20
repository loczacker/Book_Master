package com.zacker.bookmaster.model

data class PurchasedModel(
    val _id: String ?= null,
    val bookId: List<String>,
    val transactionId: String ?= null,
    val userEmail: String ?= null
)
package com.zacker.bookmaster.model

data class PaymentsModel(
    val _id: String ?= null,
    val amount: Double,
    val bookId: List<String>,
    val currency: String ?= null,
    val date: String ?= null,
    val paymentMethod: String ?= null,
    val paymentStatus: String ?= null,
    val transactionId: String ?= null,
    val userEmail: String ?= null,
    val userName: String ?= null
)
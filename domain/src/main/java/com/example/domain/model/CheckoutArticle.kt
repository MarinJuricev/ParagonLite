package com.example.domain.model

data class CheckoutArticle(
    val name: String,
    val quantity: String,
    val price: Double,
    var inCheckout: Int = 1
)
package com.example.mockfactory

import com.example.data.model.RoomCheckout
import com.example.data.model.RoomReceipt
import com.example.domain.model.Article
import com.example.domain.model.CheckoutArticle

val articleTestData = Article(
    name = "Cesnjak",
    quantity = "1",
    price = 10.00
)

val roomCheckoutTestData = RoomCheckout(
    name = "Cesnjak",
    quantity = "1",
    price = 10.00,
    inCheckout = 1.00
)

val checkoutArticleTestData = CheckoutArticle(
    name = "Cesnjak",
    quantity = "1",
    price = 10.00,
    inCheckout = 1.00
)

val roomReceiptTestData = RoomReceipt(
    number = 1,
    date = "14.07.2019",
    price = 10.00
)

val valuesToPrintTestData = listOf<ByteArray>()

const val startDateTestData = "14.07.2019"
const val endDateTestData = "20.07.2019"
const val macAddressTestData = "192.168.1.1"
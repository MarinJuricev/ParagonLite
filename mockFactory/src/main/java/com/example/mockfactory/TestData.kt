package com.example.mockfactory

import com.example.data.model.RoomCheckout
import com.example.data.model.RoomReceipt
import com.example.domain.model.Article
import com.example.domain.model.CheckoutArticle
import com.example.domain.model.Receipt
import java.lang.Exception

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

val receiptTestData = Receipt(
    number = 1,
    date = "14.07.2019",
    price = 10.00
)

val valuesToPrintTestData = listOf<ByteArray>()
val exceptionTestData = Exception()

const val startDateTestData = "14.07.2019"
const val endDateTestData = "20.07.2019"
const val macAddressTestData = "192.168.1.1"
const val currentCheckoutTestData = "5"
const val receiptNumberTestData = 1
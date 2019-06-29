package com.example.data

import com.example.data.model.RoomArticle
import com.example.data.model.RoomBluetoothEntry
import com.example.data.model.RoomCheckout
import com.example.data.model.RoomReceipt
import com.example.domain.model.Article
import com.example.domain.model.BluetoothEntry
import com.example.domain.model.CheckoutArticle
import com.example.domain.model.Receipt
import java.text.SimpleDateFormat
import java.util.*

internal val Article.toRoomArticle: RoomArticle
    get() = RoomArticle(
        this.name,
        this.quantity,
        this.price
    )

internal val RoomArticle.toArticle: Article
    get() = Article(
        this.name,
        this.quantity,
        this.price
    )

internal val CheckoutArticle.toRoomCheckout: RoomCheckout
    get() = RoomCheckout(
        this.name,
        this.quantity,
        this.price,
        this.inCheckout
    )

internal fun Article.toRoomCheckoutArticle(inCheckout: Int): RoomCheckout {
    return RoomCheckout(
        this.name,
        this.quantity,
        this.price,
        inCheckout
    )
}

internal val RoomCheckout.toCheckoutArticle: CheckoutArticle
    get() = CheckoutArticle(
        this.name,
        this.quantity,
        this.price,
        this.inCheckout
    )

internal val RoomReceipt.toReceipt: Receipt
    get() = Receipt(
        this.number,
        this.date,
        this.price
    )

internal val Receipt.toRoomReceipt: RoomReceipt
    get() = RoomReceipt(
        this.number,
        this.date,
        this.price
    )

internal fun CheckoutArticle.toReceipt(receiptNumber: String): Receipt {
    return Receipt(
        receiptNumber.toInt(),
        generateCurrentTime(),
        this.price
    )
}

internal fun BluetoothEntry.toRoomBluetooth(): RoomBluetoothEntry {
    val currentTime = generateCurrentTime()

    return RoomBluetoothEntry(
        this.name,
        this.macAddress,
        currentTime
    )
}

internal fun RoomBluetoothEntry.toBluetooth(): BluetoothEntry {
    return BluetoothEntry(
        this.name,
        this.macAddress,
        this.lastUpdated
    )
}

internal fun generateCurrentTime(): String {
    val formatter = SimpleDateFormat("dd.MM.yy hh:mm", Locale.ENGLISH)
    return formatter.format(Date())
}

//Maps from lists of different Data Model types
internal fun List<RoomArticle>.toArticleList(): List<Article> = this.flatMap {
    listOf(it.toArticle)
}

//Maps from lists of different Data Model types
internal fun List<RoomCheckout>.toCheckoutList(): List<CheckoutArticle> = this.flatMap {
    listOf(it.toCheckoutArticle)
}

//Maps from lists of different Data Model types
internal fun List<RoomReceipt>.toReceiptList(): List<Receipt> = this.flatMap {
    listOf(it.toReceipt)
}

//Maps from lists of different Data Model types
internal fun List<BluetoothEntry>.toRoomBluetoothList(): List<RoomBluetoothEntry> = this.flatMap {
    listOf(it.toRoomBluetooth())
}

//Maps from lists of different Data Model types
internal fun List<RoomBluetoothEntry>.toBluetoothList(): List<BluetoothEntry> = this.flatMap {
    listOf(it.toBluetooth())
}
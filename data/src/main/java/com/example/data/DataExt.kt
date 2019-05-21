package com.example.data

import com.example.data.model.RoomArticle
import com.example.data.model.RoomCheckout
import com.example.domain.model.Article
import com.example.domain.model.CheckoutArticle

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
        this.price
    )

internal val Article.toCheckoutArticle: CheckoutArticle
    get() = CheckoutArticle(
        this.name,
        this.quantity,
        this.price
    )

internal val Article.toRoomCheckoutArticle: RoomCheckout
    get() = RoomCheckout(
        this.name,
        this.quantity,
        this.price
    )

internal val RoomCheckout.toCheckoutArticle: CheckoutArticle
    get() = CheckoutArticle(
        this.name,
        this.quantity,
        this.price
    )

//Maps from lists of different Data Model types
internal fun List<RoomArticle>.toArticleList(): List<Article> = this.flatMap {
    listOf(it.toArticle)
}

//Maps from lists of different Data Model types
internal fun List<RoomCheckout>.toCheckoutList(): List<CheckoutArticle> = this.flatMap {
    listOf(it.toCheckoutArticle)
}
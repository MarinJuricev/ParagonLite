package com.example.data

import com.example.data.model.RoomArticle
import com.example.domain.model.Article

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

//Maps from lists of different Data Model types
internal fun List<RoomArticle>.toArticleList(): List<Article> = this.flatMap {
    listOf(it.toArticle)
}
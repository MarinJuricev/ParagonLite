package com.example.data

import com.example.data.model.RoomArticle
import com.example.domain.model.Article

internal val Article.toRoomArticle: RoomArticle
    get() = RoomArticle(
        this.name,
        this.quantity,
        this.price
    )

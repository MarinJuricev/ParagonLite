package com.example.data.article

import com.example.data.model.RoomArticle

object LocalData {

    fun getInitialArticles(): Array<RoomArticle> {
        return arrayOf(
            RoomArticle(
                "Cheese", "200g", 120.00
            ),
            RoomArticle(
                "Pizza", "Piece", 150.00
            ),
            RoomArticle(
                "Meal for two", "Piece", 320.00
            ),
            RoomArticle(
                "Cake", "Piece", 30.00
            ),
            RoomArticle(
                "Babic Vine 1l", "1l", 180.00
            ),
            RoomArticle(
                "Babic Vine 0.75l", "0.75l", 150.00
            ),
            RoomArticle(
                "Rose Vine 1l", "1l", 160.00
            ),
            RoomArticle(
                "Rose Vine 0.75l", "0.75", 140.00
            ),
            RoomArticle(
                "Prosec", "1dcl", 30.00
            ),
            RoomArticle(
                "Juice 2dcl", "2dcl", 10.00
            ),
            RoomArticle(
                "Mineral Water", "1l", 30.00
            ),
            RoomArticle(
                "Natural water", "0.75", 25.00
            )
        )
    }
}
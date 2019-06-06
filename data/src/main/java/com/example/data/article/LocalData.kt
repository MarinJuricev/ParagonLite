package com.example.data.article

import com.example.data.model.RoomArticle

object LocalData {

    fun getInitialArticles(): Array<RoomArticle> {
        return arrayOf(
            RoomArticle(
                "Prsut", "200g", 120.00
            ),
            RoomArticle(
                "Sir", "200g", 120.00
            ),
            RoomArticle(
                "Peka", "Kom", 150.00
            ),
            RoomArticle(
                "Peka za dvoje", "Kom", 320.00
            ),
            RoomArticle(
                "Domaci kolac od badema", "Kom", 30.00
            ),
            RoomArticle(
                "Panna cotta s plodovima sume", "Kom", 30.00
            ),
            RoomArticle(
                "Babic 1l", "1l", 180.00
            ),
            RoomArticle(
                "Babic 0.75l", "0.75l", 150.00
            ),
            RoomArticle(
                "Rose 1l", "1l", 160.00
            ),
            RoomArticle(
                "Rose 0.75l", "0.75", 140.00
            ),
            RoomArticle(
                "Bilo Vino 0.75l", "1l", 120.00
            ),
            RoomArticle(
                "Prosek", "1dcl", 30.00
            ),
            RoomArticle(
                "Juice 2dcl", "2dcl", 10.00
            ),
            RoomArticle(
                "Mineralna", "1l", 30.00
            ),
            RoomArticle(
                "Prirodna voda", "0.75", 25.00
            )
        )
    }
}
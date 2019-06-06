package com.example.domain.repository

import androidx.lifecycle.LiveData
import com.example.domain.model.Article
import com.example.domain.model.CheckoutArticle
import com.example.domain.model.Result
import com.example.domain.usecase.print.GetReceiptNumber

interface ICheckoutRepository {

    suspend fun sendArticleToCheckout(article: Article): Result<Exception, Unit>
    suspend fun getArticlesInCheckout(): Result<Exception, LiveData<List<CheckoutArticle>>>
    suspend fun deleteArticle(checkoutArticle: CheckoutArticle): Result<Exception, Unit>
    suspend fun deleteAllArticles()
    suspend fun saveReceiptNumber(receiptNumber: Int): Result<Exception, Unit>
    suspend fun getReceiptNumber(): Result<Exception, Int>

}
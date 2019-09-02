package com.example.domain.repository

import androidx.lifecycle.LiveData
import com.example.domain.model.Article
import com.example.domain.model.CheckoutArticle
import com.example.domain.model.Result

interface ICheckoutRepository {

    suspend fun sendArticleToCheckout(article: Article): Result<Exception, LiveData<Int>>
    suspend fun getArticlesInCheckout(): Result<Exception, LiveData<List<CheckoutArticle>>>
    suspend fun getArticlesInCheckoutSize(): LiveData<Int>
    suspend fun deleteArticle(checkoutArticle: CheckoutArticle): Result<Exception, Unit>
    suspend fun deleteAllArticles()
    suspend fun updateArticle(checkoutArticle: CheckoutArticle): Result<Exception, Unit>

}
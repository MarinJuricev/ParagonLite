package com.example.domain.repository

import com.example.domain.model.Article
import com.example.domain.model.CheckoutArticle
import com.example.domain.model.Result
import kotlinx.coroutines.flow.Flow

interface ICheckoutRepository {

    suspend fun sendArticleToCheckout(article: Article): Result<Exception, Flow<Int>>
    suspend fun getArticlesInCheckout(): Result<Exception, Flow<List<CheckoutArticle>>>
    suspend fun getArticlesInCheckoutSize(): Flow<Int>
    suspend fun deleteArticle(checkoutArticle: CheckoutArticle): Result<Exception, Unit>
    suspend fun deleteAllArticles()
    suspend fun updateArticle(checkoutArticle: CheckoutArticle): Result<Exception, Unit>

}
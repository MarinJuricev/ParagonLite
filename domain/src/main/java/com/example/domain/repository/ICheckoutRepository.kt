package com.example.domain.repository

import androidx.lifecycle.LiveData
import com.example.domain.model.Article
import com.example.domain.model.CheckoutArticle
import com.example.domain.model.Result

interface ICheckoutRepository {

    suspend fun sendArticleToCheckout(article: Article): Result<Exception, Unit>
    suspend fun getArticlesInCheckout(): Result<Exception, LiveData<List<CheckoutArticle>>>
}
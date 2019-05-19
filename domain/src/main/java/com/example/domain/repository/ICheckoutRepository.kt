package com.example.domain.repository

import com.example.domain.model.Article
import com.example.domain.model.Result

interface ICheckoutRepository {

    suspend fun sendArticleToCheckout(article: Article): Result<Exception, Unit>
}
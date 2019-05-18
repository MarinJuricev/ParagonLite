package com.example.domain.repository

import com.example.domain.model.Article
import com.example.domain.model.Result

interface IArticleRepository {

    suspend fun createArticle(article: Article): Result<Exception, Unit>

}
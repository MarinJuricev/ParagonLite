package com.example.domain.repository

import com.example.domain.model.Article
import com.example.domain.model.Result
import kotlinx.coroutines.flow.Flow

interface IArticleRepository {

    suspend fun createArticle(article: Article): Result<Exception, Unit>
    suspend fun getArticles(): Result<Exception, Flow<List<Article>>>
    suspend fun deleteArticle(article: Article): Result<Exception, Unit>

}
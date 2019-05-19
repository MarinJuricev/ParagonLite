package com.example.domain.repository

import androidx.lifecycle.LiveData
import com.example.domain.model.Article
import com.example.domain.model.Result

interface IArticleRepository {

    suspend fun createArticle(article: Article): Result<Exception, Unit>
    suspend fun getArticles(): Result<Exception, LiveData<List<Article>>>
    suspend fun deleteArticle(article: Article): Result<Exception, Unit>

}
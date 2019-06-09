package com.example.domain.usecase.article

import androidx.lifecycle.LiveData
import com.example.domain.model.Article
import com.example.domain.model.Result
import com.example.domain.repository.IArticleRepository

class GetArticles(
    private val articleRepository: IArticleRepository
) {
    suspend fun execute(
    ): Result<Exception, LiveData<List<Article>>> = articleRepository.getArticles()
}
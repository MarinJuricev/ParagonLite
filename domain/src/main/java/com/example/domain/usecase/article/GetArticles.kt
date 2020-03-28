package com.example.domain.usecase.article

import com.example.domain.model.Article
import com.example.domain.model.Result
import com.example.domain.repository.IArticleRepository
import kotlinx.coroutines.flow.Flow

class GetArticles(
    private val articleRepository: IArticleRepository
) {
    suspend fun execute(
    ): Result<Exception, Flow<List<Article>>> = articleRepository.getArticles()
}
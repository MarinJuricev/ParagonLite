package com.example.domain.usecase.article

import com.example.domain.model.Article
import com.example.domain.model.Result
import com.example.domain.repository.IArticleRepository

class CreateArticle {

    suspend fun createArticle(
        articleRepository: IArticleRepository,
        article: Article
    ): Result<Exception, Unit> = articleRepository.createArticle(article)
}
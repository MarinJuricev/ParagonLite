package com.example.domain.usecase.article

import com.example.domain.model.Article
import com.example.domain.model.Result
import com.example.domain.repository.IArticleRepository

class DeleteArticle {

    suspend fun deleteArticle(
        articleRepository: IArticleRepository,
        article: Article
    ): Result<Exception, Unit> = articleRepository.deleteArticle(article)
}
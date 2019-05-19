package com.example.domain.usecase

import com.example.domain.model.Article
import com.example.domain.model.Result
import com.example.domain.repository.IArticleRepository
import java.lang.Exception

class GetArticles{

    suspend fun getArticles(
        articleRepository: IArticleRepository
    ): Result<Exception, List<Article>> = articleRepository.getArticles()
}
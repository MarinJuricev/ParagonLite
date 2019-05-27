package com.example.domain.usecase.article

import androidx.lifecycle.LiveData
import com.example.domain.model.Article
import com.example.domain.model.Result
import com.example.domain.repository.IArticleRepository

class GetArticles {

    suspend fun execute(
        articleRepository: IArticleRepository
    ): Result<Exception, LiveData<List<Article>>> = articleRepository.getArticles()
}
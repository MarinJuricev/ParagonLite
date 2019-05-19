package com.example.data.article

import com.example.data.model.RoomArticle
import com.example.data.toArticleList
import com.example.data.toRoomArticle
import com.example.domain.DispatcherProvider
import com.example.domain.error.ParagonError
import com.example.domain.model.Article
import com.example.domain.model.Result
import com.example.domain.repository.IArticleRepository
import kotlinx.coroutines.withContext

class ArticleRepositoryImpl(
    private val dispatcherProvider: DispatcherProvider,
    private val articleDao: ArticleDao
) : IArticleRepository {

    override suspend fun createArticle(article: Article): Result<Exception, Unit> =
        withContext(dispatcherProvider.provideIOContext()) {

            when (articleDao.upsert(article.toRoomArticle)) {
                0L -> Result.build { throw ParagonError.LocalIOException }
                else -> Result.build { Unit }
            }
        }

    override suspend fun getArticles(): Result<Exception, List<Article>> =
        withContext(dispatcherProvider.provideIOContext()) {

            val result = articleDao.getArticles()

            when (result) {
                listOf<RoomArticle>() -> Result.build { throw ParagonError.LocalIOException }
                else -> Result.build { result.value!!.toArticleList() }
            }
        }
}
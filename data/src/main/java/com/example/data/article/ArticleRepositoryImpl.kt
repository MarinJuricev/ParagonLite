package com.example.data.article

import com.example.data.model.RoomArticle
import com.example.data.toArticleList
import com.example.data.toRoomArticle
import com.example.domain.error.ParagonError
import com.example.domain.model.Article
import com.example.domain.model.Result
import com.example.domain.repository.IArticleRepository
import com.example.domain.shared.DispatcherProvider
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
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

    override suspend fun getArticles(): Result<Exception, Flow<List<Article>>> =
        withContext(dispatcherProvider.provideIOContext()) {

            when (val result = articleDao.getArticles()) {
                listOf<RoomArticle>() -> Result.build { throw ParagonError.LocalIOException }
                else -> {
                    Result.build {
                        result.map { value: List<RoomArticle> ->
                            value.toArticleList()
                        }
                    }
                }
            }
        }

    override suspend fun deleteArticle(article: Article): Result<Exception, Unit> =
        withContext(dispatcherProvider.provideIOContext()) {

            when (articleDao.deleteArticle(article.toRoomArticle)) {
                0 -> Result.build { throw ParagonError.LocalIOException }
                else -> Result.build { Unit }
            }
        }

}
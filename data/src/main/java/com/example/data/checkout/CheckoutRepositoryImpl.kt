package com.example.data.checkout

import com.example.data.model.RoomCheckout
import com.example.data.toCheckoutList
import com.example.data.toRoomCheckout
import com.example.data.toRoomCheckoutArticle
import com.example.domain.error.ParagonError
import com.example.domain.model.Article
import com.example.domain.model.CheckoutArticle
import com.example.domain.model.Result
import com.example.domain.repository.ICheckoutRepository
import com.example.domain.shared.DispatcherProvider
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class CheckoutRepositoryImpl(
    private val dispatcherProvider: DispatcherProvider,
    private val checkoutDao: CheckoutDao
) : ICheckoutRepository {

    override suspend fun sendArticleToCheckout(
        article: Article
    ): Result<Exception, Flow<Int>> =
        withContext(dispatcherProvider.provideIOContext()) {

            val previousArticleCount = getPreviousCheckoutArticleNumberIfAvailable(article) ?: 0.00
            val newArticleCountInCheckout = previousArticleCount + 1.00

            when (checkoutDao.upsert(article.toRoomCheckoutArticle(newArticleCountInCheckout))) {
                0L -> Result.build { throw ParagonError.LocalIOException }
                else -> Result.build { getArticlesInCheckoutSize() }
            }
        }

    override suspend fun getArticlesInCheckoutSize(): Flow<Int> =
        withContext(dispatcherProvider.provideIOContext()) {
            checkoutDao.getCheckoutArticleCount()
        }

    private suspend fun getPreviousCheckoutArticleNumberIfAvailable(article: Article): Double? =
        withContext(dispatcherProvider.provideIOContext()) {
            when (val result = checkoutDao.getArticle(article.name)) {
                null -> null
                else -> result.inCheckout
            }
        }

    override suspend fun getArticlesInCheckout(): Result<Exception, Flow<List<CheckoutArticle>>> =
        withContext(dispatcherProvider.provideIOContext()) {

            when (val result = checkoutDao.getArticles()) {
                listOf<RoomCheckout>() -> Result.build { throw ParagonError.LocalIOException }
                else -> Result.build {
                    result.map { value: List<RoomCheckout> ->
                        value.toCheckoutList()
                    }
                }
            }
        }

    override suspend fun deleteArticle(checkoutArticle: CheckoutArticle): Result<Exception, Unit> =
        withContext(dispatcherProvider.provideIOContext()) {
            when (checkoutDao.deleteArticle(checkoutArticle.toRoomCheckout)) {
                0 -> Result.build { throw ParagonError.LocalIOException }
                else -> Result.build { Unit }
            }
        }

    override suspend fun deleteAllArticles() =
        withContext(dispatcherProvider.provideIOContext()) {
            checkoutDao.deleteAll()
        }

    override suspend fun updateArticle(checkoutArticle: CheckoutArticle): Result<Exception, Unit> =
        withContext(dispatcherProvider.provideIOContext()) {
            when (checkoutDao.upsert(checkoutArticle.toRoomCheckout)) {
                0L -> Result.build { throw ParagonError.LocalIOException }
                else -> Result.build { Unit }
            }
        }
}
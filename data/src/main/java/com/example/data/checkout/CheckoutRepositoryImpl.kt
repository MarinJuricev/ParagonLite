package com.example.data.checkout

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
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
import kotlinx.coroutines.withContext

class CheckoutRepositoryImpl(
    private val dispatcherProvider: DispatcherProvider,
    private val checkoutDao: CheckoutDao
) : ICheckoutRepository {

    override suspend fun sendArticleToCheckout(
        article: Article
    ): Result<Exception, LiveData<Int>> =
        withContext(dispatcherProvider.provideIOContext()) {

            val previousArticleCount = getPreviousCheckoutArticleNumberIfAvailable(article) ?: 0.00
            val newArticleCountInCheckout = previousArticleCount + 1.00

            when (checkoutDao.upsert(article.toRoomCheckoutArticle(newArticleCountInCheckout))) {
                0L -> Result.build { throw ParagonError.LocalIOException }
                else -> Result.build { getArticlesInCheckoutSize() }
            }
        }

    override suspend fun getArticlesInCheckoutSize(): LiveData<Int> =
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

    override suspend fun getArticlesInCheckout(): Result<Exception, LiveData<List<CheckoutArticle>>> =
        withContext(dispatcherProvider.provideIOContext()) {

            when (val result= checkoutDao.getArticles()) {
                listOf<RoomCheckout>() -> Result.build { throw ParagonError.LocalIOException }
                else -> Result.build {
                    Transformations.map(
                        result,
                        ::mapToCheckoutArticle
                    )
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

private fun mapToCheckoutArticle(list: List<RoomCheckout>) = list.toCheckoutList()
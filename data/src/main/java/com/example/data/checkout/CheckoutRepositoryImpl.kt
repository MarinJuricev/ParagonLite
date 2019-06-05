package com.example.data.checkout

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.example.data.model.RoomCheckout
import com.example.data.toCheckoutList
import com.example.data.toRoomCheckout
import com.example.data.toRoomCheckoutArticle
import com.example.domain.DispatcherProvider
import com.example.domain.error.ParagonError
import com.example.domain.model.Article
import com.example.domain.model.CheckoutArticle
import com.example.domain.model.Result
import com.example.domain.repository.ICheckoutRepository
import kotlinx.coroutines.withContext

class CheckoutRepositoryImpl(
    private val dispatcherProvider: DispatcherProvider,
    private val checkoutDao: CheckoutDao
) : ICheckoutRepository {

    override suspend fun sendArticleToCheckout(article: Article): Result<Exception, Unit> =
        withContext(dispatcherProvider.provideIOContext()) {

            when (checkoutDao.upsert(article.toRoomCheckoutArticle)) {
                0L -> Result.build { throw ParagonError.LocalIOException }
                else -> Result.build { Unit }
            }
        }

    override suspend fun getArticlesInCheckout(): Result<Exception, LiveData<List<CheckoutArticle>>> =
        withContext(dispatcherProvider.provideIOContext()) {

            when (val result = checkoutDao.getArticles()) {
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
}

private fun mapToCheckoutArticle(list: List<RoomCheckout>) = list.toCheckoutList()
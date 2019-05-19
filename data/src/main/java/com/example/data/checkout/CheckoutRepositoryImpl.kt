package com.example.data.checkout

import com.example.data.toRoomCheckoutArticle
import com.example.domain.DispatcherProvider
import com.example.domain.error.ParagonError
import com.example.domain.model.Article
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
}
package com.example.data.checkout

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.example.data.*
import com.example.data.model.RoomCheckout
import com.example.domain.DispatcherProvider
import com.example.domain.error.ParagonError
import com.example.domain.model.Article
import com.example.domain.model.CheckoutArticle
import com.example.domain.model.Result
import com.example.domain.repository.ICheckoutRepository
import kotlinx.coroutines.withContext

//when (val result = checkoutDao.getArticle(article.name)) can throw a NPE if
// there isn't a article present in the checkout table
@Suppress("SENSELESS_NULL_IN_WHEN")
class CheckoutRepositoryImpl(
    private val context: Context,
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

    // TODO REFACTOR INTO IT'S OWN USECASE!
    private suspend fun getPreviousCheckoutArticleNumberIfAvailable(article: Article): Double? =
        withContext(dispatcherProvider.provideIOContext()) {
            when (val result = checkoutDao.getArticle(article.name)) {
                null -> null
                else -> result.inCheckout
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

    override suspend fun saveReceiptNumber(receiptNumber: Int): Result<Exception, Unit> =
        withContext(dispatcherProvider.provideIOContext()) {
            val preferences = context.getSharedPreferences(PACKAGE_NAME, Context.MODE_PRIVATE)

            val editor = preferences.edit()
            editor.putString(RECEIPT_KEY, receiptNumber.toString())

            when (editor.commit()) {
                true -> Result.build { Unit }
                else -> Result.build { throw ParagonError.ReceiptException }
            }
        }

    override suspend fun getReceiptNumber(): Result<Exception, Int> =
        withContext(dispatcherProvider.provideIOContext()) {

            val preferences = context.getSharedPreferences(PACKAGE_NAME, Context.MODE_PRIVATE)

            when (val result = preferences.getString(RECEIPT_KEY, "1")!!.toInt()) {
                // TODO It'll never be 0... add better error handling
                0 -> Result.build { throw ParagonError.ReceiptException }
                else -> Result.build { result }
            }
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
package com.example.data.checkout

import com.example.data.toRoomCheckout
import com.example.data.toRoomCheckoutArticle
import com.example.domain.error.ParagonError
import com.example.domain.model.Result
import com.example.domain.repository.ICheckoutRepository
import com.example.domain.shared.DispatcherProvider
import com.example.mockfactory.articleTestData
import com.example.mockfactory.checkoutArticleTestData
import com.example.mockfactory.roomCheckoutTestData
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test


internal class CheckoutRepositoryTest {

    private val dispatcherProvider: DispatcherProvider = mockk()
    private val checkoutDao: CheckoutDao = mockk()

    private lateinit var checkoutRepository: ICheckoutRepository

    @ExperimentalCoroutinesApi
    @BeforeEach
    fun setUp() {
        clearAllMocks()

        every { dispatcherProvider.provideIOContext() } returns Dispatchers.Unconfined

        checkoutRepository = CheckoutRepositoryImpl(
            dispatcherProvider, checkoutDao
        )
    }

    @Test
    fun `send article to checkout fail`() = runBlocking {
        coEvery {
            checkoutDao.getArticle(articleTestData.name)
        } coAnswers { null }

        coEvery {
            checkoutDao.upsert(articleTestData.toRoomCheckoutArticle(1.0))
        } coAnswers { 0L }

        val actualResult = checkoutRepository.sendArticleToCheckout(articleTestData)
        val expectedResult = Result.build { throw ParagonError.LocalIOException }

        assertEquals(expectedResult, actualResult)
    }

    @Test
    fun `send article to checkout success`() = runBlocking {
        val articleCount = flow {
            emit(4)
        }

        coEvery {
            checkoutDao.getArticle(articleTestData.name)
        } coAnswers { roomCheckoutTestData }

        coEvery {
            checkoutDao.upsert(articleTestData.toRoomCheckoutArticle(2.0))
        } coAnswers { 2L }

        coEvery {
            checkoutDao.getCheckoutArticleCount()
        } coAnswers { articleCount }

        val actualResult = checkoutRepository.sendArticleToCheckout(articleTestData)
        val expectedResult = Result.build { articleCount }

        assertEquals(expectedResult, actualResult)
    }

    @Test
    fun `get article in checkout success`() = runBlocking {
        val articleTestData = flow {
            emit(listOf(roomCheckoutTestData))
        }

        coEvery {
            checkoutDao.getArticles()
        } coAnswers { articleTestData }

        val actualResult = checkoutRepository.getArticlesInCheckout()
        val expectedResult = Result.build { articleTestData }

        assertEquals(
            expectedResult is Result.Value<*>,
            actualResult is Result.Value<*>
        )
    }

    @Test
    fun `delete article fail`() = runBlocking {
        coEvery {
            checkoutDao.deleteArticle(checkoutArticleTestData.toRoomCheckout)
        } coAnswers { 0 }

        val actualResult = checkoutRepository.deleteArticle(checkoutArticleTestData)
        val expectedResult = Result.build { throw ParagonError.LocalIOException }

        assertEquals(expectedResult, actualResult)
    }

    @Test
    fun `delete article success`() = runBlocking {
        coEvery {
            checkoutDao.deleteArticle(checkoutArticleTestData.toRoomCheckout)
        } coAnswers { 1 }

        val actualResult = checkoutRepository.deleteArticle(checkoutArticleTestData)
        val expectedResult = Result.build { Unit }

        assertEquals(expectedResult, actualResult)
    }

    @Test
    fun `updateArticle article fail`() = runBlocking {
        coEvery {
            checkoutDao.upsert(checkoutArticleTestData.toRoomCheckout)
        } coAnswers { 0 }

        val actualResult = checkoutRepository.updateArticle(checkoutArticleTestData)
        val expectedResult = Result.build { throw ParagonError.LocalIOException }

        assertEquals(expectedResult, actualResult)
    }

    @Test
    fun `updateArticle article success`() = runBlocking {
        coEvery {
            checkoutDao.upsert(checkoutArticleTestData.toRoomCheckout)
        } coAnswers { 1 }

        val actualResult = checkoutRepository.updateArticle(checkoutArticleTestData)
        val expectedResult = Result.build { Unit }

        assertEquals(expectedResult, actualResult)
    }
}
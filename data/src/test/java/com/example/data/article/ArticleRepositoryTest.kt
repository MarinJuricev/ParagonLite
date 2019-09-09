package com.example.data.article

import com.example.data.checkout.articleTestData
import com.example.data.toRoomArticle
import com.example.domain.error.ParagonError
import com.example.domain.model.Result
import com.example.domain.shared.DispatcherProvider
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test


internal class ArticleRepositoryTest {

    private val dispatcherProvider: DispatcherProvider = mockk()
    private val articleDao: ArticleDao = mockk()

    private lateinit var articleRepository: ArticleRepositoryImpl

    @ExperimentalCoroutinesApi
    @BeforeEach
    fun setUp() {
        clearAllMocks()

        every { dispatcherProvider.provideIOContext() } returns Dispatchers.Unconfined

        articleRepository = ArticleRepositoryImpl(
            dispatcherProvider, articleDao
        )
    }

    @Test
    fun `create article success`() = runBlocking {

        coEvery {
            articleDao.upsert(articleTestData.toRoomArticle)
        } coAnswers { 1L }

        val actualResult = articleRepository.createArticle(articleTestData)
        val expectedResult = Result.build { Unit }

        assertEquals(expectedResult, actualResult)
    }

    @Test
    fun `create article fail`() = runBlocking {
        coEvery {
            articleDao.upsert(articleTestData.toRoomArticle)
        } coAnswers { 0L }

        val actualResult = articleRepository.createArticle(articleTestData)
        val expectedResult = Result.build { throw ParagonError.LocalIOException }

        assertEquals(expectedResult, actualResult)
    }
}
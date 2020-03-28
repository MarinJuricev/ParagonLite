package com.example.data.article

import androidx.lifecycle.MutableLiveData
import com.example.data.model.RoomArticle
import com.example.data.toRoomArticle
import com.example.domain.error.ParagonError
import com.example.domain.model.Result
import com.example.domain.shared.DispatcherProvider
import com.example.mockfactory.articleTestData
import com.example.mockfactory.roomArticleTestData
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
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

    @Test
    fun `get articles success`() = runBlocking {
        val articlesTestData = flow {
            emit(listOf(roomArticleTestData))
        }

        coEvery {
            articleDao.getArticles()
        } coAnswers { articlesTestData }

        val actualResult = articleRepository.getArticles()
        val expectedResult = Result.build { articlesTestData }

        assertEquals(
            expectedResult is Result.Value<*>,
            actualResult is Result.Value<*>
        )
    }

    @Test
    fun `delete article fail`() = runBlocking {
        coEvery {
            articleDao.deleteArticle(articleTestData.toRoomArticle)
        } coAnswers { 0 }

        val actualResult = articleRepository.deleteArticle(articleTestData)
        val expectedResult = Result.build { throw ParagonError.LocalIOException }

        assertEquals(expectedResult, actualResult)
    }

    @Test
    fun `delete article success`() = runBlocking {
        coEvery {
            articleDao.deleteArticle(articleTestData.toRoomArticle)
        } coAnswers { 1 }

        val actualResult = articleRepository.deleteArticle(articleTestData)
        val expectedResult = Result.build { Unit }

        assertEquals(expectedResult, actualResult)
    }
}
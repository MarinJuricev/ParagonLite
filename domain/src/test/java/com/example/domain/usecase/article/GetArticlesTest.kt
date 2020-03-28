package com.example.domain.usecase.article

import com.example.domain.model.Result
import com.example.domain.repository.IArticleRepository
import com.example.mockfactory.articleTestData
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class GetArticlesTest {

    private val articleRepository: IArticleRepository = mockk()

    private lateinit var getArticle: GetArticles

    @BeforeEach
    fun setUp() {
        clearAllMocks()
        getArticle = GetArticles(articleRepository)
    }

    @Test
    fun `get article is article repository triggered with unit response`() = runBlocking {
        val repositoryResult = flow {
            emit(listOf(articleTestData))
        }

        coEvery {
            articleRepository.getArticles()
        } coAnswers { Result.build { repositoryResult } }

        getArticle.execute()

        coVerify { getArticle.execute() }
        coVerify { articleRepository.getArticles() }
    }

    @Test
    fun `get article is article repository triggered with error response`() = runBlocking {
        coEvery {
            articleRepository.getArticles()
        } coAnswers { Result.build { throw Exception() } }

        getArticle.execute()

        coVerify { getArticle.execute() }
        coVerify { articleRepository.getArticles() }
    }
}
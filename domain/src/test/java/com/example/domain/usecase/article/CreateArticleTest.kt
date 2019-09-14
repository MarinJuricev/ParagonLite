package com.example.domain.usecase.article

import com.example.domain.model.Result
import com.example.domain.repository.IArticleRepository
import com.example.mockfactory.articleTestData
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.lang.Exception

internal class CreateArticleTest {

    private val articleRepository: IArticleRepository = mockk()

    private lateinit var createArticle: CreateArticle

    @BeforeEach
    fun setUp() {
        clearAllMocks()

        createArticle = CreateArticle(articleRepository)
    }

    @Test
    fun `create article is article repository triggered with unit response`() = runBlocking {
        coEvery {
            articleRepository.createArticle(articleTestData)
        } coAnswers { Result.build { Unit } }

        createArticle.execute(articleTestData)

        coVerify { createArticle.execute(articleTestData) }
        coVerify { articleRepository.createArticle(articleTestData) }
    }

    @Test
    fun `create article is article repository triggered with error response`() = runBlocking {
        coEvery {
            articleRepository.createArticle(articleTestData)
        } coAnswers { Result.build { throw Exception()} }

        createArticle.execute(articleTestData)

        coVerify { createArticle.execute(articleTestData) }
        coVerify { articleRepository.createArticle(articleTestData) }
    }
}
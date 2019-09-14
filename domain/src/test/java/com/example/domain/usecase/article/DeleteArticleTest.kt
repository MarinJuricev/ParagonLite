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

internal class DeleteArticleTest {

    private val articleRepository: IArticleRepository = mockk()

    private lateinit var deleteArticle: DeleteArticle

    @BeforeEach
    fun setUp() {
        clearAllMocks()

        deleteArticle = DeleteArticle(articleRepository)
    }

    @Test
    fun `delete article is article repository triggered with unit response`() = runBlocking {
        coEvery {
            articleRepository.deleteArticle(articleTestData)
        } coAnswers { Result.build { Unit } }

        deleteArticle.execute(articleTestData)

        coVerify { deleteArticle.execute(articleTestData) }
        coVerify { articleRepository.deleteArticle(articleTestData) }
    }

    @Test
    fun `delete article is article repository triggered with error response`() = runBlocking {
        coEvery {
            articleRepository.deleteArticle(articleTestData)
        } coAnswers { Result.build { throw Exception() } }

        deleteArticle.execute(articleTestData)

        coVerify { deleteArticle.execute(articleTestData) }
        coVerify { articleRepository.deleteArticle(articleTestData) }
    }
}
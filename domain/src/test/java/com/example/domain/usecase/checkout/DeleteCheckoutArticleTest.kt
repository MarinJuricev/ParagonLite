package com.example.domain.usecase.checkout

import com.example.domain.model.Result
import com.example.domain.repository.ICheckoutRepository
import com.example.mockfactory.checkoutArticleTestData
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class DeleteCheckoutArticleTest {

    private val checkoutRepository: ICheckoutRepository = mockk()

    private lateinit var deleteCheckoutArticle: DeleteCheckoutArticle

    @BeforeEach
    fun setUp() {
        clearAllMocks()

        deleteCheckoutArticle = DeleteCheckoutArticle(checkoutRepository)
    }

    @Test
    fun `delete checkout article triggers checkout repository triggered with success response`() =
        runBlocking {
            coEvery {
                checkoutRepository.deleteArticle(checkoutArticleTestData)
            } coAnswers { Result.build { Unit } }

            checkoutRepository.deleteArticle(checkoutArticleTestData)

            coVerify { deleteCheckoutArticle.execute(checkoutArticleTestData) }
            coVerify { checkoutRepository.deleteArticle(checkoutArticleTestData) }
        }

    @Test
    fun `delete checkout article triggers checkout repository triggered with error response`() =
        runBlocking {
            coEvery {
                checkoutRepository.deleteArticle(checkoutArticleTestData)
            } coAnswers { Result.build { throw Exception() } }

            checkoutRepository.deleteArticle(checkoutArticleTestData)

            coVerify { deleteCheckoutArticle.execute(checkoutArticleTestData) }
            coVerify { checkoutRepository.deleteArticle(checkoutArticleTestData) }
        }
}
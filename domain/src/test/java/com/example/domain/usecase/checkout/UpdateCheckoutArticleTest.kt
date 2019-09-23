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

internal class UpdateCheckoutArticleTest {

    private val checkoutRepository: ICheckoutRepository = mockk()

    private lateinit var updateCheckoutArticle: UpdateCheckoutArticle

    @BeforeEach
    fun setUp() {
        clearAllMocks()

        updateCheckoutArticle = UpdateCheckoutArticle(checkoutRepository)
    }

    @Test
    fun `update checkout triggers checkout repository triggered with success response`() =
        runBlocking {

            coEvery {
                checkoutRepository.updateArticle(checkoutArticleTestData)
            } coAnswers { Result.build { Unit } }

            checkoutRepository.updateArticle(checkoutArticleTestData)

            coVerify { updateCheckoutArticle.execute(checkoutArticleTestData) }
            coVerify { checkoutRepository.updateArticle(checkoutArticleTestData) }
        }

    @Test
    fun `update checkout triggers checkout repository triggered with error response`() =
        runBlocking {

            coEvery {
                checkoutRepository.updateArticle(checkoutArticleTestData)
            } coAnswers { Result.build { throw Exception() } }

            checkoutRepository.updateArticle(checkoutArticleTestData)

            coVerify { updateCheckoutArticle.execute(checkoutArticleTestData) }
            coVerify { checkoutRepository.updateArticle(checkoutArticleTestData) }
        }
}
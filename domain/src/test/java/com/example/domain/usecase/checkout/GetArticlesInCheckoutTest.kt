package com.example.domain.usecase.checkout

import com.example.domain.model.Result
import com.example.domain.repository.ICheckoutRepository
import com.example.mockfactory.checkoutArticleTestData
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class GetArticlesInCheckoutTest {
    private val checkoutRepository: ICheckoutRepository = mockk()

    private lateinit var getArticleInCheckout: GetArticlesInCheckout

    @BeforeEach
    fun setUp() {
        clearAllMocks()

        getArticleInCheckout = GetArticlesInCheckout(checkoutRepository)
    }

    @Test
    fun `get article in checkout triggers checkout repository triggered with success response`() =
        runBlocking {
            val repositoryResult = flow {
                emit(listOf(checkoutArticleTestData))
            }


            coEvery {
                checkoutRepository.getArticlesInCheckout()
            } coAnswers { Result.build { repositoryResult } }

            checkoutRepository.getArticlesInCheckout()

            coVerify { getArticleInCheckout.execute() }
            coVerify { checkoutRepository.getArticlesInCheckout() }
        }

    @Test
    fun `get article in checkout triggers checkout repository triggered with error response`() =
        runBlocking {

            coEvery {
                checkoutRepository.getArticlesInCheckout()
            } coAnswers {
                Result.build { throw Exception() }
            }

            checkoutRepository.getArticlesInCheckout()

            coVerify { getArticleInCheckout.execute() }
            coVerify { checkoutRepository.getArticlesInCheckout() }
        }
}


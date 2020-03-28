package com.example.domain.usecase.checkout

import com.example.domain.model.Result
import com.example.domain.repository.ICheckoutRepository
import com.example.mockfactory.articleTestData
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class SendArticleToCheckoutTest {
    private val checkoutRepository: ICheckoutRepository = mockk()

    private lateinit var sendArticleToCheckout: SendArticleToCheckout

    @BeforeEach
    fun setUp() {
        clearAllMocks()

        sendArticleToCheckout = SendArticleToCheckout(checkoutRepository)
    }

    @Test
    fun `send article in checkout triggers checkout repository triggered with success response`() =
        runBlocking {
            val repositoryResult = flow {
                emit(4)
            }

            coEvery {
                checkoutRepository.sendArticleToCheckout(articleTestData)
            } coAnswers { Result.build { repositoryResult } }

            checkoutRepository.sendArticleToCheckout(articleTestData)

            coVerify { sendArticleToCheckout.execute(articleTestData) }
            coVerify { checkoutRepository.sendArticleToCheckout(articleTestData) }
        }

    @Test
    fun `send article in checkout triggers checkout repository triggered with error response`() =
        runBlocking {

            coEvery {
                checkoutRepository.sendArticleToCheckout(articleTestData)
            } coAnswers { Result.build { throw Exception() } }

            checkoutRepository.sendArticleToCheckout(articleTestData)

            coVerify { sendArticleToCheckout.execute(articleTestData) }
            coVerify { checkoutRepository.sendArticleToCheckout(articleTestData) }
        }
}
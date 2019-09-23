package com.example.domain.usecase.checkout

import com.example.domain.shared.DispatcherProvider
import com.example.mockfactory.checkoutArticleTestData
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class CalculateCheckoutTest {

    private val dispatcherProvider: DispatcherProvider = mockk()

    private lateinit var calculateCheckout: CalculateCheckout

    @ExperimentalCoroutinesApi
    @BeforeEach
    fun setUp() {
        clearAllMocks()
        every { dispatcherProvider.provideComputationContext() } returns Dispatchers.Unconfined

        calculateCheckout = CalculateCheckout(dispatcherProvider)
    }

    @Test
    fun `calculate the checkout amount and format depending on the provided list`() =
        runBlocking {

            val testData = listOf(checkoutArticleTestData, checkoutArticleTestData)

            val actualResult = calculateCheckout.execute(testData)
            val expectedResult = "20.00"

            assertEquals(expectedResult, actualResult)
        }


}
package com.example.domain.usecase.checkout

import androidx.lifecycle.MutableLiveData
import com.example.domain.repository.ICheckoutRepository
import io.mockk.clearAllMocks

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class GetArticlesInCheckoutSizeTest {

    private val checkoutRepository: ICheckoutRepository = mockk()

    private lateinit var getArticlesInCheckoutSize: GetArticlesInCheckoutSize

    @BeforeEach
    fun setUp() {
        clearAllMocks()

        getArticlesInCheckoutSize = GetArticlesInCheckoutSize(checkoutRepository)
    }

    @Test
    fun `get article size triggers checkout repository triggered with success response`() =
        runBlocking {
            coEvery {
                checkoutRepository.getArticlesInCheckoutSize()
            } coAnswers { MutableLiveData<Int>() }

            checkoutRepository.getArticlesInCheckoutSize()

            coVerify { getArticlesInCheckoutSize.execute() }
            coVerify { checkoutRepository.getArticlesInCheckoutSize() }
        }
}
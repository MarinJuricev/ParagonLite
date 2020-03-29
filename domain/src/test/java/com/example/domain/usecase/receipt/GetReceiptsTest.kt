package com.example.domain.usecase.receipt

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.domain.error.ParagonError
import com.example.domain.model.Result
import com.example.domain.repository.IReceiptRepository
import com.example.mockfactory.receiptTestData
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import org.junit.Rule
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class GetReceiptsTest {

    private val receiptRepository: IReceiptRepository = mockk()

    private lateinit var getReceipt: GetReceipts

    @Rule
    @JvmField
    val rule = InstantTaskExecutorRule()

    @BeforeEach
    fun setUp() {
        clearAllMocks()

        getReceipt = GetReceipts(receiptRepository)
    }

    @Test
    fun `get receipts receipt repository triggered with success response`() = runBlocking {
        val startDate = 1583190000000
        val endDate = 1585605600000

        val formattedStartDate = "03.03.20"
        val formattedEndDate = "31.03.20"

        val repositoryResult = flow {
            emit(listOf(receiptTestData))
        }

        coEvery {
            receiptRepository.getReceipts(
                formattedStartDate,
                formattedEndDate
            )
        } coAnswers { Result.build { repositoryResult } }

        val actualResult = getReceipt.execute(startDate, endDate)
        val expectedResult = Result.build { repositoryResult }

        coVerify {
            receiptRepository.getReceipts(
                formattedStartDate,
                formattedEndDate
            )
        }

        assert(expectedResult == actualResult)
    }

    @Test
    fun `get receipts receipt repository triggered with failure response`() = runBlocking {
        val startDate = 1583190000000
        val endDate = 1585605600000

        val formattedStartDate = "03.03.20"
        val formattedEndDate = "31.03.20"

        coEvery {
            receiptRepository.getReceipts(
                formattedStartDate,
                formattedEndDate
            )
        } coAnswers { Result.build { throw ParagonError.ReceiptException } }

        val actualResult = getReceipt.execute(startDate, endDate)
        val expectedResult = Result.build { throw ParagonError.ReceiptException }

        coVerify {
            receiptRepository.getReceipts(
                formattedStartDate,
                formattedEndDate
            )
        }

        assert(expectedResult == actualResult)
    }

}


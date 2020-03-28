package com.example.data.receipt

import com.example.domain.model.Result
import com.example.domain.repository.IReceiptRepository
import com.example.domain.shared.DispatcherProvider
import com.example.mockfactory.endDateTestData
import com.example.mockfactory.roomReceiptTestData
import com.example.mockfactory.startDateTestData
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class ReceiptRepositoryTest {

    private val dispatcherProvider: DispatcherProvider = mockk()
    private val receiptDao: ReceiptDao = mockk()

    private lateinit var receiptRepository: IReceiptRepository

    @ExperimentalCoroutinesApi
    @BeforeEach
    fun setUp() {
        clearAllMocks()

        every { dispatcherProvider.provideIOContext() } returns Dispatchers.Unconfined

        receiptRepository = ReceiptRepositoryImpl(dispatcherProvider, receiptDao)
    }

    @Test
    fun `get receipts success`() = runBlocking {
        val receiptTestData = flow {
            emit(listOf(roomReceiptTestData))
        }

        coEvery {
            receiptDao.getReceipts(
                startDateTestData,
                endDateTestData
            )
        } coAnswers { receiptTestData }

        val actualResult = receiptRepository.getReceipts(
            startDateTestData,
            endDateTestData
        )
        val expectedResult = Result.build { receiptTestData }

        assertEquals(
            expectedResult is Result.Value<*>,
            actualResult is Result.Value<*>
        )
    }
}
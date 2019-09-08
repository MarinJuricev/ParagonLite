package com.example.data.receipt

import androidx.lifecycle.MutableLiveData
import com.example.data.checkout.endDateTestData
import com.example.data.checkout.startDateTestData
import com.example.data.model.RoomReceipt
import com.example.domain.repository.IReceiptRepository
import com.example.domain.shared.DispatcherProvider
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.BeforeEach
import com.example.domain.model.Result
import junit.framework.Assert.assertEquals
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
        val receiptTestData = MutableLiveData<List<RoomReceipt>>()

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
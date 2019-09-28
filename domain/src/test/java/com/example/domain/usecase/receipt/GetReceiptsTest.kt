package com.example.domain.usecase.receipt

import com.example.domain.model.Result
import com.example.domain.repository.IReceiptRepository
import com.example.mockfactory.macAddressTestData
import com.example.mockfactory.valuesToPrintTestData
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import junit.framework.Assert
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class GetReceiptsTest {

    private val receiptRepository: IReceiptRepository = mockk()

    private lateinit var getReceipt: GetReceipts

    @BeforeEach
    fun setUp() {
        clearAllMocks()

        getReceipt = GetReceipts(receiptRepository)
    }

//    @Test
//    fun `get receipts receipt repository triggered with success response`() = runBlocking {
//        coEvery {
//            bluetoothRepository.connectAndSendDataOverBluetooth(
//                macAddressTestData,
//                valuesToPrintTestData
//            )
//        } coAnswers { Result.build { Unit } }
//
//        val actualResult = printHistory.execute(macAddressTestData, valuesToPrintTestData)
//        val expectedResult = Result.build { Unit }
//
//        coVerify { printHistory.execute(macAddressTestData, valuesToPrintTestData) }
//        coVerify {
//            bluetoothRepository.connectAndSendDataOverBluetooth(
//                macAddressTestData,
//                valuesToPrintTestData
//            )
//        }
//
//        Assert.assertEquals(expectedResult, actualResult)
//    }

}


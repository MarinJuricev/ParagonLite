package com.example.domain.usecase.print

import com.example.domain.model.Result
import com.example.domain.repository.IBluetoothRepository
import com.example.domain.repository.ICheckoutRepository
import com.example.domain.usecase.receipt.AddReceipt
import com.example.mockfactory.currentCheckoutTestData
import com.example.mockfactory.macAddressTestData
import com.example.mockfactory.receiptNumberTestData
import com.example.mockfactory.valuesToPrintTestData
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class PrintCheckoutTest {

    private val checkoutRepository: ICheckoutRepository = mockk()
    private val bluetoothRepository: IBluetoothRepository = mockk()
    private val addReceipt: AddReceipt = mockk()

    private lateinit var printCheckout: PrintCheckout

    @BeforeEach
    fun setUp() {
        clearAllMocks()

        printCheckout = PrintCheckout(
            bluetoothRepository,
            checkoutRepository
        )
    }

    @Test
    fun `connect and send data over bluetooth success`() = runBlocking {
        coEvery {
            bluetoothRepository.connectAndSendDataOverBluetooth(
                macAddressTestData,
                valuesToPrintTestData
            )
        } coAnswers { Result.build { Unit } }

        coEvery {
            checkoutRepository.deleteAllArticles()
        } coAnswers { Unit }

        coEvery {
            addReceipt.execute(
                receiptNumberTestData,
                currentCheckoutTestData
            )
        } coAnswers { Result.build { Unit } }

        printCheckout.execute(
            valuesToPrintTestData,
            macAddressTestData,
            currentCheckoutTestData,
            receiptNumberTestData,
            addReceipt
        )

        coVerify(exactly = 1) { addReceipt.execute(receiptNumberTestData, currentCheckoutTestData) }
        coVerify(exactly = 1) { checkoutRepository.deleteAllArticles() }
    }

    @Test
    fun `connect and send data over bluetooth error`() = runBlocking {
        val testException = Exception()

        coEvery {
            bluetoothRepository.connectAndSendDataOverBluetooth(
                macAddressTestData,
                valuesToPrintTestData
            )
        } coAnswers { Result.build { throw testException } }

        val actualResult = printCheckout.execute(
            valuesToPrintTestData,
            macAddressTestData,
            currentCheckoutTestData,
            receiptNumberTestData,
            addReceipt
        )

        val expectedResult: Result<Exception, Unit> = Result.build { throw testException }

        assertEquals(expectedResult, actualResult)
    }
}
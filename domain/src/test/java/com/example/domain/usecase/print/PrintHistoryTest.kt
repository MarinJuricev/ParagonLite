package com.example.domain.usecase.print

import com.example.domain.model.Result
import com.example.domain.repository.IBluetoothRepository
import com.example.mockfactory.exceptionTestData
import com.example.mockfactory.macAddressTestData
import com.example.mockfactory.valuesToPrintTestData
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class PrintHistoryTest {

    private val bluetoothRepository: IBluetoothRepository = mockk()

    private lateinit var printHistory: PrintHistory

    @BeforeEach
    fun setUp() {
        clearAllMocks()

        printHistory = PrintHistory(bluetoothRepository)
    }

    @Test
    fun `print history repository triggered with success response`() = runBlocking {
        coEvery {
            bluetoothRepository.connectAndSendDataOverBluetooth(
                macAddressTestData,
                valuesToPrintTestData
            )
        } coAnswers { Result.build { Unit } }

        val actualResult = printHistory.execute(macAddressTestData, valuesToPrintTestData)
        val expectedResult = Result.build { Unit }

        coVerify { printHistory.execute(macAddressTestData, valuesToPrintTestData) }
        coVerify {
            bluetoothRepository.connectAndSendDataOverBluetooth(
                macAddressTestData,
                valuesToPrintTestData
            )
        }

        assertEquals(expectedResult, actualResult)
    }

    @Test
    fun `print history repository triggered with error response`() = runBlocking {
        coEvery {
            bluetoothRepository.connectAndSendDataOverBluetooth(
                macAddressTestData,
                valuesToPrintTestData
            )
        } coAnswers { Result.build { throw exceptionTestData } }

        val actualResult = printHistory.execute(macAddressTestData, valuesToPrintTestData)
        val expectedResult = Result.build { throw exceptionTestData }

        coVerify { printHistory.execute(macAddressTestData, valuesToPrintTestData) }
        coVerify {
            bluetoothRepository.connectAndSendDataOverBluetooth(
                macAddressTestData,
                valuesToPrintTestData
            )
        }

        assertEquals(expectedResult, actualResult)
    }
}
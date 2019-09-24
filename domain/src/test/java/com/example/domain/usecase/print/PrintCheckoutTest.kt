package com.example.domain.usecase.print

import com.example.domain.repository.IBluetoothRepository
import com.example.domain.repository.ICheckoutRepository
import com.example.mockfactory.macAddressTestData
import com.example.mockfactory.valuesToPrintTestData
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class PrintCheckoutTest {

    private val checkoutRepository: ICheckoutRepository = mockk()
    private val bluetoothRepository: IBluetoothRepository = mockk()

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
    fun `connect and send data over bluetooth returns error`() = runBlocking {
        coEvery {
            bluetoothRepository.connectAndSendDataOverBluetooth(
                macAddressTestData,
                valuesToPrintTestData
            )
        }
    }


}
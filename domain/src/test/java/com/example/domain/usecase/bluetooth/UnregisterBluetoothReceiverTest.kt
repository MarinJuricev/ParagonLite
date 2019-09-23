package com.example.domain.usecase.bluetooth

import com.example.domain.model.Result
import com.example.domain.repository.IBluetoothRepository
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class UnregisterBluetoothReceiverTest {

    private val bluetoothRepository: IBluetoothRepository = mockk()

    private lateinit var unregisterBluetoothReceiver: UnregisterBluetoothReceiver

    @BeforeEach
    fun setUp() {
        clearAllMocks()

        unregisterBluetoothReceiver = UnregisterBluetoothReceiver(bluetoothRepository)
    }

    @Test
    fun `unregisterBluetooth receiver is bluetooth repository triggered with success response`() =
        runBlocking {
            coEvery {
                bluetoothRepository.unRegisterReceiver()
            } coAnswers { Result.build { Unit } }

            unregisterBluetoothReceiver.execute()

            coVerify { unregisterBluetoothReceiver.execute() }
            coVerify { bluetoothRepository.unRegisterReceiver() }
        }

    @Test
    fun `unregisterBluetooth receiver is bluetooth repository triggered with error response`() =
        runBlocking {
            runBlocking {
                coEvery {
                    bluetoothRepository.unRegisterReceiver()
                } coAnswers { Result.build { throw Exception() } }

                unregisterBluetoothReceiver.execute()

                coVerify { unregisterBluetoothReceiver.execute() }
                coVerify { bluetoothRepository.unRegisterReceiver() }
            }
        }
}
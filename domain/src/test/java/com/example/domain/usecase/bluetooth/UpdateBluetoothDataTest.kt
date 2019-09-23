package com.example.domain.usecase.bluetooth

import com.example.domain.model.BluetoothEntry
import com.example.domain.model.Result
import com.example.domain.repository.IBluetoothRepository
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class UpdateBluetoothDataTest {

    private val bluetoothRepository: IBluetoothRepository = mockk()

    private lateinit var updateBluetoothData: UpdateBluetoothData

    @BeforeEach
    fun setUp() {
        clearAllMocks()

        updateBluetoothData = UpdateBluetoothData(bluetoothRepository)
    }

    @Test
    fun `updateBluetoothData is bluetooth repository triggered with success response`() =
        runBlocking {

            val mockedList: List<BluetoothEntry> = mockk()

            coEvery {
                bluetoothRepository.getNearbyBluetoothDevices()
            } coAnswers { Result.build { mockedList } }

            updateBluetoothData.execute()

            coVerify { updateBluetoothData.execute() }
            coVerify { bluetoothRepository.getNearbyBluetoothDevices() }
        }

    @Test
    fun `updateBluetoothData is bluetooth repository triggered with error response`() =
        runBlocking {
            runBlocking {
                coEvery {
                    bluetoothRepository.getNearbyBluetoothDevices()
                } coAnswers { Result.build { throw Exception() } }

                updateBluetoothData.execute()

                coVerify { updateBluetoothData.execute() }
                coVerify { bluetoothRepository.getNearbyBluetoothDevices() }
            }
        }
}
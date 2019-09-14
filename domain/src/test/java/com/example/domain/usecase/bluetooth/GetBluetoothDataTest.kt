package com.example.domain.usecase.bluetooth

import androidx.lifecycle.MutableLiveData
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

internal class GetBluetoothDataTest {

    private val bluetoothRepository: IBluetoothRepository = mockk()

    private lateinit var getBluetoothData: GetBluetoothData

    @BeforeEach
    fun setUp() {
        clearAllMocks()

        getBluetoothData = GetBluetoothData(bluetoothRepository)
    }

    @Test
    fun `get bluetooth data is bluetooth repository triggered with success response`() =
        runBlocking {
            coEvery {
                bluetoothRepository.getBluetoothData()
            } coAnswers { Result.build { MutableLiveData<List<BluetoothEntry>>() } }

            getBluetoothData.execute()

            coVerify { getBluetoothData.execute() }
            coVerify { bluetoothRepository.getBluetoothData() }
        }

    @Test
    fun `get bluetooth data is bluetooth repository triggered with error response`() = runBlocking {
        coEvery {
            bluetoothRepository.getBluetoothData()
        } coAnswers { Result.build { throw Exception() } }

        getBluetoothData.execute()

        coVerify { getBluetoothData.execute() }
        coVerify { bluetoothRepository.getBluetoothData() }
    }
}
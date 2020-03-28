package com.example.domain.usecase.bluetooth

import com.example.domain.model.BluetoothEntry
import com.example.domain.model.Result
import com.example.domain.repository.IBluetoothRepository
import kotlinx.coroutines.flow.Flow

class GetBluetoothData(
    private val bluetoothRepository: IBluetoothRepository
) {
    suspend fun execute(): Result<Exception, Flow<List<BluetoothEntry>>> =
        bluetoothRepository.getBluetoothData()
}

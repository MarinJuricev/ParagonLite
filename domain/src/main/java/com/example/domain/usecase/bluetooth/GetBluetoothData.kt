package com.example.domain.usecase.bluetooth

import androidx.lifecycle.LiveData
import com.example.domain.model.BluetoothEntry
import com.example.domain.model.Result
import com.example.domain.repository.IBluetoothRepository

class GetBluetoothData(
    private val bluetoothRepository: IBluetoothRepository
) {
    suspend fun execute(): Result<Exception, LiveData<List<BluetoothEntry>>> = bluetoothRepository.getBluetoothData()
}

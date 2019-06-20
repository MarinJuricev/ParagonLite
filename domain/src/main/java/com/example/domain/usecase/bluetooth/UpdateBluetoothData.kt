package com.example.domain.usecase.bluetooth

import com.example.domain.model.BluetoothEntry
import com.example.domain.model.Result
import com.example.domain.repository.IBluetoothRepository

class UpdateBluetoothData(
    private val bluetoothRepository: IBluetoothRepository
) {
    suspend fun execute(): Result<Exception, List<BluetoothEntry>> = bluetoothRepository.getNearbyBluetoothDevices()
}
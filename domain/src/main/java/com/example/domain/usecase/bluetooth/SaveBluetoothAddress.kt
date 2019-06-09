package com.example.domain.usecase.bluetooth

import com.example.domain.model.Result
import com.example.domain.repository.IBluetoothRepository

class SaveBluetoothAddress(
    private val bluetoothRepository: IBluetoothRepository
) {
    suspend fun execute(
        macAddress: String
    ): Result<Exception, Unit> = bluetoothRepository.saveMacAddress(macAddress)
}
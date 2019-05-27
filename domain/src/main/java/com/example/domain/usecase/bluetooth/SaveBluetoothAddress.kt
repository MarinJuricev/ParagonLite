package com.example.domain.usecase.bluetooth

import com.example.domain.model.Result
import com.example.domain.repository.IBluetoothRepository

class SaveBluetoothAddress {

    suspend fun execute(
        bluetoothRepository: IBluetoothRepository,
        macAddress: String
    ): Result<Exception, Unit> = bluetoothRepository.saveMacAddress(macAddress)
}
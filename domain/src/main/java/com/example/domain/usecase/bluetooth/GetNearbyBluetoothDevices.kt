package com.example.domain.usecase.bluetooth

import com.example.domain.model.BluetoothEntry
import com.example.domain.model.Result
import com.example.domain.repository.IBluetoothRepository

class GetNearbyBluetoothDevices {

    suspend fun getNearbyBluetoothDevices(
        bluetoothRepository: IBluetoothRepository
    ): Result<Exception, List<BluetoothEntry>> = bluetoothRepository.getNearbyBluetoothDevices()

}
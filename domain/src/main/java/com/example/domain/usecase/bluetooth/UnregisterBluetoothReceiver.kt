package com.example.domain.usecase.bluetooth

import com.example.domain.model.Result
import com.example.domain.repository.IBluetoothRepository

class UnregisterBluetoothReceiver(
    private val bluetoothRepository: IBluetoothRepository
) {
    fun execute(): Result<Exception, Unit> = bluetoothRepository.unRegisterReceiver()
}

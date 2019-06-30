package com.example.domain.usecase.print

import com.example.domain.model.Result
import com.example.domain.repository.IBluetoothRepository

class PrintHistory(
    private val bluetoothRepository: IBluetoothRepository
) {

    suspend fun execute(
        savedMacAddress: String,
        printData: List<ByteArray>
    ): Result<Exception, Unit> =
        bluetoothRepository.connectAndSendDataOverBluetooth(savedMacAddress, printData)

}
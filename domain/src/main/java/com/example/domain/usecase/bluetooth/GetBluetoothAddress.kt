package com.example.domain.usecase.bluetooth

import com.example.domain.error.ParagonError
import com.example.domain.model.Result
import com.example.domain.repository.IBluetoothRepository

class GetBluetoothAddress {

    suspend fun execute(
        bluetoothRepository: IBluetoothRepository
    ): Result<Exception, String> {

        val savedMacAddress = bluetoothRepository.getMacAddress()

        return if (validateMacAddress(savedMacAddress))
            Result.build { throw ParagonError.PrintException }
        else
            savedMacAddress
    }

    /**
     * If the mac address is BluetoothException then it means we didn't pair with any bluetooth device yet!
     */
    private fun validateMacAddress(savedMacAddress: Result<Exception, String>) =
        savedMacAddress == Result.build { ParagonError.BluetoothException }
}
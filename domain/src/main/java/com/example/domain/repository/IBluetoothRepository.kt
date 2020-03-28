package com.example.domain.repository

import com.example.domain.model.BluetoothEntry
import com.example.domain.model.Result
import kotlinx.coroutines.flow.Flow

interface IBluetoothRepository {

    suspend fun getNearbyBluetoothDevices(): Result<Exception, List<BluetoothEntry>>
    suspend fun connectAndSendDataOverBluetooth(
        savedMacAddress: String,
        dataToPrint: List<ByteArray>
    ): Result<Exception, Unit>

    suspend fun getBluetoothData(): Result<Exception, Flow<List<BluetoothEntry>>>
    fun unRegisterReceiver(): Result<Exception, Unit>
}
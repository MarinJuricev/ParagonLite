package com.example.domain.repository

import com.example.domain.model.BluetoothEntry
import com.example.domain.model.Result

interface IBluetoothRepository {

    suspend fun getNearbyBluetoothDevices(): Result<Exception, List<BluetoothEntry>>
    suspend fun saveMacAddress(macAddress: String): Result<Exception, Unit>
    suspend fun getMacAddress(): Result<Exception, String>
    suspend fun connectAndSendDataOverBluetooth(
        savedMacAddress:String,
        dataToPrint: List<ByteArray>): Result<Exception, Unit>

    fun unRegisterReceiver(): Result<Exception, Unit>
}
package com.example.domain.repository

import androidx.lifecycle.LiveData
import com.example.domain.model.BluetoothEntry
import com.example.domain.model.Result

interface IBluetoothRepository {

    suspend fun getNearbyBluetoothDevices(): Result<Exception, List<BluetoothEntry>>
    suspend fun connectAndSendDataOverBluetooth(
        savedMacAddress: String,
        dataToPrint: List<ByteArray>
    ): Result<Exception, Unit>

    suspend fun getBluetoothData(): Result<Exception, LiveData<List<BluetoothEntry>>>
    fun unRegisterReceiver(): Result<Exception, Unit>
}
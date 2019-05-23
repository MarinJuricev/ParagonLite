package com.example.domain.repository

import androidx.lifecycle.LiveData
import com.example.domain.model.BluetoothDevice
import com.example.domain.model.Result

interface IBluetoothRepository {

    suspend fun getNearbyBluetoothDevices(): Result<Exception, LiveData<BluetoothDevice>>
}
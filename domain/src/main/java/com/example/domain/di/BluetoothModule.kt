package com.example.domain.di

import com.example.domain.usecase.bluetooth.GetNearbyBluetoothDevices
import com.example.domain.usecase.bluetooth.SaveBluetoothAddress
import org.koin.dsl.module

val bluetoothDomainModel = module {
    factory { GetNearbyBluetoothDevices() }
    factory { SaveBluetoothAddress() }
}
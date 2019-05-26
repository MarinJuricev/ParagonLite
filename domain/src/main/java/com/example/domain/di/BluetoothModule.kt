package com.example.domain.di

import com.example.domain.usecase.bluetooth.GetNearbyBluetoothDevices
import org.koin.dsl.module

val bluetoothDomainModel = module {
    factory { GetNearbyBluetoothDevices() }
}
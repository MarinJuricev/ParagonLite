package com.example.domain.di

import com.example.domain.usecase.bluetooth.GetBluetoothAddress
import com.example.domain.usecase.bluetooth.GetNearbyBluetoothDevices
import com.example.domain.usecase.bluetooth.SaveBluetoothAddress
import com.example.domain.usecase.print.GeneratePrintData
import com.example.domain.usecase.print.GetReceiptNumber
import com.example.domain.usecase.print.SaveReceiptNumber
import org.koin.dsl.module

val bluetoothDomainModel = module {
    factory { GetNearbyBluetoothDevices() }
    factory { SaveBluetoothAddress() }
    factory { GetBluetoothAddress() }
    factory { GeneratePrintData() }
    factory { GetReceiptNumber() }
    factory { SaveReceiptNumber() }
}
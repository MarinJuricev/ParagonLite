package com.example.domain.di

import com.example.domain.usecase.bluetooth.*
import com.example.domain.usecase.print.GeneratePrintData
import com.example.domain.usecase.print.GetReceiptNumber
import com.example.domain.usecase.print.SaveReceiptNumber
import org.koin.dsl.module

val bluetoothDomainModel = module {
    factory { UpdateBluetoothData(get()) }
    factory { SaveBluetoothAddress(get()) }
    factory { GetBluetoothAddress(get()) }
    factory { GeneratePrintData(get()) }
    factory { GetReceiptNumber(get()) }
    factory { SaveReceiptNumber(get()) }
    factory { UnregisterBluetoothReceiver(get()) }
    factory { GetBluetoothData(get()) }
}
package com.example.paragonlite.di

import com.example.paragonlite.ui.bluetooth.BluetoothViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val bluetoothModule = module {
    viewModel { BluetoothViewModel(get(), get(), get()) }
}
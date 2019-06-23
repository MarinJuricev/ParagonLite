package com.example.presentation.di

import com.example.presentation.ui.bluetooth.BluetoothViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val bluetoothModule = module {
    viewModel { BluetoothViewModel(get(), get(), get(), get()) }
}
package com.example.presentation.ui.bluetooth

sealed class BluetoothStatus {
    object Enabled: BluetoothStatus()
    object Disabled: BluetoothStatus()
    object Dismissed: BluetoothStatus()
}
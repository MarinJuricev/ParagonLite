package com.example.paragonlite.ui.bluetooth

import androidx.recyclerview.widget.DiffUtil
import com.example.domain.model.BluetoothEntry

class BluetoothDiffUtillCallback : DiffUtil.ItemCallback<BluetoothEntry>() {

    override fun areItemsTheSame(oldItem: BluetoothEntry, newItem: BluetoothEntry): Boolean {
        return oldItem.macAddress == newItem.macAddress
    }

    override fun areContentsTheSame(oldItem: BluetoothEntry, newItem: BluetoothEntry): Boolean {
        return oldItem == newItem
    }
}
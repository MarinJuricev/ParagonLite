package com.example.paragonlite.ui.bluetooth

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.domain.model.BluetoothEntry
import com.example.paragonlite.R
import com.google.android.material.card.MaterialCardView
import kotlinx.android.synthetic.main.item_bluetooth.view.*

class BluetoothDeviceAdapter(
    private val onBluetoothItemClick: (String) -> (Unit)
) : ListAdapter<BluetoothEntry, BluetoothDeviceAdapter.BluetoothViewHolder>(BluetoothDiffUtillCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BluetoothViewHolder {
        val inflater = LayoutInflater.from(parent.context)

        return BluetoothViewHolder(
            inflater.inflate(R.layout.item_bluetooth, parent, false)
        )
    }

    override fun onBindViewHolder(holder: BluetoothViewHolder, position: Int) {
        getItem(position).let { device ->
            with(holder) {
                deviceName.text = device.name
                deviceMacAddress.text = device.macAddress

                root.setOnClickListener { onBluetoothItemClick(device.macAddress) }
            }
        }
    }

    class BluetoothViewHolder(root: View) : RecyclerView.ViewHolder(root) {
        var deviceName: AppCompatTextView = root.tvBluetoothNameValue
        var deviceMacAddress: AppCompatTextView = root.tvBluetoothAdressValue
        var root: MaterialCardView = root.bluetoothItemRoot
    }
}
package com.example.presentation.ui.bluetooth

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.example.domain.model.BluetoothEntry
import com.example.presentation.R
import com.example.presentation.shared.SimpleViewHolder
import com.example.presentation.shared.inflateIntoSelf
import kotlinx.android.synthetic.main.item_bluetooth.view.*

class BluetoothDeviceAdapter(
    private val onBluetoothItemClick: (String) -> (Unit)
) : ListAdapter<BluetoothEntry, SimpleViewHolder>(BluetoothDiffUtilCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SimpleViewHolder {
        return SimpleViewHolder(parent.inflateIntoSelf(R.layout.item_bluetooth))
    }

    override fun getItemCount(): Int = currentList.size

    override fun onBindViewHolder(holder: SimpleViewHolder, position: Int) {
        if (currentList.isNotEmpty()) {
            getItem(position).let { device ->
                holder.itemView.apply {
                    tvBluetoothNameValue.text = device.name
                    tvBluetoothAdressValue.text = device.macAddress
                    tvLastUpdatedValue.text = device.lastUpdated
                    bluetoothItemRoot.setOnClickListener { onBluetoothItemClick(device.macAddress) }
                }
            }
        }
    }
}
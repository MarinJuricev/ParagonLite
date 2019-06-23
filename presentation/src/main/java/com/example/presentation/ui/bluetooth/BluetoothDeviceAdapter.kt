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

    private enum class BluetoothViewType { CONTENT, NO_CONTENT }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SimpleViewHolder {
        return if (viewType == BluetoothViewType.CONTENT.ordinal)
            SimpleViewHolder(parent.inflateIntoSelf(R.layout.item_bluetooth))
        else
            SimpleViewHolder(parent.inflateIntoSelf(R.layout.no_bluetooth_devices_layout))
    }

    override fun getItemViewType(position: Int): Int {
        return if (currentList.isEmpty()) BluetoothViewType.NO_CONTENT.ordinal
        else BluetoothViewType.CONTENT.ordinal
    }

    override fun getItemCount(): Int {
        return if (currentList.isEmpty()) {
            1
        } else
            currentList.size
    }

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
package com.example.data.bluetooth

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import com.example.domain.error.ParagonError.BluetoothException
import com.example.domain.model.BluetoothEntry
import com.example.domain.model.Result
import com.example.domain.repository.IBluetoothRepository
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine


class BluetoothRepositoryImpl(
    private val context: Context
) : IBluetoothRepository {

    lateinit var bluetoothReceiver: BroadcastReceiver

    override suspend fun getNearbyBluetoothDevices(): Result<Exception, List<BluetoothEntry>> {

        return when (val result = startDiscovery()) {
            listOf<BluetoothEntry>() -> Result.build { throw BluetoothException }
            else -> Result.build { result }
        }
    }

    private suspend fun startDiscovery(): List<BluetoothEntry> {
        return suspendCoroutine { continuation ->
            val bluetoothList = mutableListOf<BluetoothEntry>()

            bluetoothReceiver = object : BroadcastReceiver() {
                override fun onReceive(context: Context, intent: Intent) {
                    when (intent.action) {
                        BluetoothDevice.ACTION_FOUND -> updateBluetoothList(bluetoothList, intent)
                        BluetoothAdapter.ACTION_DISCOVERY_FINISHED -> resumeExecution()
                    }
                }

                private fun resumeExecution() {
                    continuation.resume(bluetoothList)
                }
            }

            val filter = IntentFilter()
            filter.apply {
                addAction(BluetoothDevice.ACTION_FOUND)
                addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED)
            }

            context.registerReceiver(bluetoothReceiver, filter)
            val mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter()

            mBluetoothAdapter.startDiscovery()
        }
    }

    private fun updateBluetoothList(
        bluetoothList: MutableList<BluetoothEntry>,
        intent: Intent
    ) {
        val device = intent.getParcelableExtra<BluetoothDevice>(BluetoothDevice.EXTRA_DEVICE)
        val deviceName = device.name
        val deviceHardwareAddress = device.address

        bluetoothList.add(BluetoothEntry(deviceName, deviceHardwareAddress))
    }

    override fun unRegisterReceiver(): Result<Exception, Unit> {
        context.unregisterReceiver(bluetoothReceiver)

        return Result.build { Unit }
    }
}
package com.example.data.bluetooth

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import com.example.domain.DispatcherProvider
import com.example.domain.error.ParagonError
import com.example.domain.error.ParagonError.BluetoothException
import com.example.domain.model.BluetoothEntry
import com.example.domain.model.Result
import com.example.domain.repository.IBluetoothRepository
import kotlinx.coroutines.withContext
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

const val BLUETOOTH_MAC_ADDRESS_KEY = "BLUETOOTH_MAC_ADDRESS_KEY"
const val PACKAGE_NAME = "\"com.example.data\""

class BluetoothRepositoryImpl(
    private val context: Context,
    private val dispatcherProvider: DispatcherProvider
) : IBluetoothRepository {

    lateinit var bluetoothReceiver: BroadcastReceiver
    lateinit var bluetoothAdapter: BluetoothAdapter

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
            bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()

            bluetoothAdapter.startDiscovery()
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

    @SuppressLint("ApplySharedPref")
    override suspend fun saveMacAddress(macAddress: String): Result<Exception, Unit> =
        withContext(dispatcherProvider.provideIOContext()) {
            val preferences = context.getSharedPreferences(PACKAGE_NAME, Context.MODE_PRIVATE)

            val editor = preferences.edit()
            editor.putString(BLUETOOTH_MAC_ADDRESS_KEY, macAddress)

            when (editor.commit()) {
                true -> Result.build { Unit }
                else -> Result.build { throw ParagonError.LocalIOException }
            }
        }

    override fun unRegisterReceiver(): Result<Exception, Unit> {
        if (bluetoothAdapter.isDiscovering)
            bluetoothAdapter.cancelDiscovery()

        context.unregisterReceiver(bluetoothReceiver)

        return Result.build { Unit }
    }
}
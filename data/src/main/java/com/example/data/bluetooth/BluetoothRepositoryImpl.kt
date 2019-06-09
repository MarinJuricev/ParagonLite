package com.example.data.bluetooth

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Looper
import com.example.data.BLUETOOTH_MAC_ADDRESS_KEY
import com.example.data.PACKAGE_NAME
import com.example.domain.DispatcherProvider
import com.example.domain.error.ParagonError
import com.example.domain.error.ParagonError.BluetoothException
import com.example.domain.model.BluetoothEntry
import com.example.domain.model.Result
import com.example.domain.repository.IBluetoothRepository
import com.zebra.sdk.comm.BluetoothConnectionInsecure
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import kotlin.coroutines.resume

class BluetoothRepositoryImpl(
    private val context: Context,
    private val dispatcherProvider: DispatcherProvider
) : IBluetoothRepository {

    private var bluetoothReceiver: BroadcastReceiver? = null
    private var bluetoothAdapter: BluetoothAdapter? = null

    override suspend fun getNearbyBluetoothDevices(): Result<Exception, List<BluetoothEntry>> {

        return when (val result = startDiscovery()) {
            listOf<BluetoothEntry>() -> Result.build { throw BluetoothException }
            else -> Result.build { result }
        }
    }

    private suspend fun startDiscovery(): List<BluetoothEntry> {
        return suspendCancellableCoroutine { continuation ->
            val bluetoothList = mutableListOf<BluetoothEntry>()

            bluetoothReceiver = object : BroadcastReceiver() {
                override fun onReceive(context: Context, intent: Intent) {
                    when (intent.action) {
                        BluetoothDevice.ACTION_FOUND -> updateBluetoothList(bluetoothList, intent)
                        BluetoothAdapter.ACTION_DISCOVERY_FINISHED -> resumeExecution()
                    }
                }

                private fun resumeExecution() {
                    if (continuation.isActive)
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

            bluetoothAdapter!!.startDiscovery()
        }
    }

    private fun updateBluetoothList(
        bluetoothList: MutableList<BluetoothEntry>,
        intent: Intent
    ) {
        val device = intent.getParcelableExtra<BluetoothDevice>(BluetoothDevice.EXTRA_DEVICE)
        val deviceName = device.name
        val deviceHardwareAddress = device.address

        bluetoothAdapter?.bondedDevices

        // No need for duplicate entries
        if (!bluetoothList.contains(BluetoothEntry(deviceName, deviceHardwareAddress)))
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

    override suspend fun getMacAddress(): Result<Exception, String> =
        withContext(dispatcherProvider.provideIOContext()) {

            val preferences = context.getSharedPreferences(PACKAGE_NAME, Context.MODE_PRIVATE)

            when (val result = preferences.getString(BLUETOOTH_MAC_ADDRESS_KEY, "")) {
                "" -> Result.build { throw BluetoothException }
                else -> Result.build { result }
            }
        }


    override suspend fun connectAndSendDataOverBluetooth(
        savedMacAddress: String,
        dataToPrint: List<ByteArray>
    ): Result<Exception, Unit> {

        Thread(Runnable {
            try {
                // Instantiate insecure connection for given Bluetooth&reg; MAC Address.
                val thePrinterConn = BluetoothConnectionInsecure(savedMacAddress)

                // Initialize
                Looper.prepare()

                // Open the connection - physical connection is established here.
                thePrinterConn.open()

                // Send the data to printer as a byte array.
                for (data in dataToPrint)
                    thePrinterConn.write(data)

                // Make sure the data got to the printer before closing the connection
                Thread.sleep(500)

                // Close the insecure connection to release resources.
                thePrinterConn.close()

                Looper.myLooper()!!.quit()
            } catch (e: Exception) {
                // Handle communications error here.
                e.printStackTrace()
            }
        }).start()
        // TODO Error handling, make it a continuation coroutine ?
        return Result.build { Unit }
    }

    //TODO refactor this mess
    override fun unRegisterReceiver(): Result<Exception, Unit> {
        bluetoothAdapter?.let {
            if (bluetoothAdapter!!.isDiscovering)
                bluetoothAdapter!!.cancelDiscovery()
        }

        bluetoothReceiver?.let {
            try {
                context.unregisterReceiver(bluetoothReceiver)
            } catch (exception: Exception) {
                return Result.build { Unit }
            }
        }

        return Result.build { Unit }
    }
}
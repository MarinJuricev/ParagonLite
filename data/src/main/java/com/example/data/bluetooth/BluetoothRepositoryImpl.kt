package com.example.data.bluetooth

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import com.example.data.generateCurrentTime
import com.example.data.model.RoomBluetoothEntry
import com.example.data.toBluetoothList
import com.example.data.toRoomBluetoothList
import com.example.domain.error.ParagonError
import com.example.domain.error.ParagonError.BluetoothException
import com.example.domain.model.BluetoothEntry
import com.example.domain.model.Result
import com.example.domain.repository.IBluetoothRepository
import com.example.domain.shared.DispatcherProvider
import com.zebra.sdk.comm.BluetoothConnectionInsecure
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import kotlin.coroutines.resume

class BluetoothRepositoryImpl(
    private val context: Context,
    private val dispatcherProvider: DispatcherProvider,
    private val bluetoothDao: BluetoothDao
) : IBluetoothRepository {

    private var bluetoothReceiver: BroadcastReceiver? = null
    private var bluetoothAdapter: BluetoothAdapter? = null

    override suspend fun getNearbyBluetoothDevices(): Result<Exception, List<BluetoothEntry>> {

        return when (val result = startDiscovery()) {
            listOf<BluetoothEntry>() -> Result.build { throw BluetoothException }
            else -> Result.build {
                saveBluetoothEntriesToLocalPersistence(result)
                result
            }
        }
    }

    override suspend fun getBluetoothData(): Result<Exception, Flow<List<BluetoothEntry>>> =
        withContext(dispatcherProvider.provideIOContext()) {

            when (val result = bluetoothDao.getBluetoothEntries()) {
                listOf<RoomBluetoothEntry>() -> Result.build { throw ParagonError.LocalIOException }
                else -> Result.build {
                    result.map { value: List<RoomBluetoothEntry> ->
                        value.toBluetoothList()
                    }
                }
            }
        }

    private suspend fun saveBluetoothEntriesToLocalPersistence(result: List<BluetoothEntry>) =
        withContext(DispatcherProvider.provideIOContext()) {
            bluetoothDao.insertAll(result.toRoomBluetoothList())
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
                    if (continuation.isActive) {
                        continuation.resume(bluetoothList)
                    }
                }
            }

            val filter = IntentFilter()
            filter.apply {
                addAction(BluetoothDevice.ACTION_FOUND)
                addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED)
            }

            context.registerReceiver(bluetoothReceiver, filter)
            bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()

            bluetoothAdapter?.startDiscovery()
        }
    }

    private fun updateBluetoothList(
        bluetoothList: MutableList<BluetoothEntry>,
        intent: Intent
    ) {
        val device = intent.getParcelableExtra<BluetoothDevice>(BluetoothDevice.EXTRA_DEVICE)
        val deviceName = device.name ?: ""
        val deviceHardwareAddress = device.address ?: ""

        bluetoothAdapter?.bondedDevices

        // No need for duplicate entries
        if (deviceName != "" && deviceHardwareAddress != "" && !bluetoothList.any { it.name == deviceName })
            bluetoothList.add(
                BluetoothEntry(
                    deviceName,
                    deviceHardwareAddress,
                    generateCurrentTime()
                )
            )
    }

    override suspend fun connectAndSendDataOverBluetooth(
        savedMacAddress: String,
        dataToPrint: List<ByteArray>
    ): Result<Exception, Unit> =
        withContext(dispatcherProvider.provideComputationContext()) {
            try {
                // Instantiate insecure connection for given Bluetooth&reg; MAC Address.
                val thePrinterConn = BluetoothConnectionInsecure(savedMacAddress)

                // Open the connection - physical connection is established here.
                thePrinterConn.open()

                // Send the data to printer as a byte array.
                for (data in dataToPrint)
                    thePrinterConn.write(data)

                // Make sure the data got to the printer before closing the connection
                delay(500)

                // Close the insecure connection to release resources.
                thePrinterConn.close()
            } catch (e: Exception) {
                // Handle communications error here.
                e.printStackTrace()
                Result.build { throw ParagonError.PrintException }
            }
            Result.build { Unit }
        }

    override fun unRegisterReceiver(): Result<Exception, Unit> {
        bluetoothAdapter?.let {
            if (it.isDiscovering)
                it.cancelDiscovery()
        }

        bluetoothReceiver?.let {
            try {
                context.unregisterReceiver(bluetoothReceiver)
            } catch (exception: Exception) {
                return Result.build { throw BluetoothException }
            }
        }

        return Result.build { Unit }
    }
}
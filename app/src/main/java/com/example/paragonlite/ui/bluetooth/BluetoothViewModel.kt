package com.example.paragonlite.ui.bluetooth

import android.bluetooth.BluetoothAdapter
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.domain.model.BluetoothEntry
import com.example.domain.model.Result
import com.example.domain.usecase.bluetooth.GetNearbyBluetoothDevices
import com.example.domain.usecase.bluetooth.SaveBluetoothAddress
import com.example.domain.usecase.bluetooth.UnregisterBluetoothReceiver
import com.example.paragonlite.shared.BaseViewModel
import kotlinx.coroutines.launch

class BluetoothViewModel(
    private val unregisterBluetoothReceiver: UnregisterBluetoothReceiver,
    private val getBluetoothDevices: GetNearbyBluetoothDevices,
    private val saveBluetoothAddress: SaveBluetoothAddress
) : BaseViewModel() {

    init {
        isBluetoothAvailable()
    }

    private val _isBluetoothAvailable by lazy { MutableLiveData<Boolean>() }
    val isBluetoothAvailable: LiveData<Boolean> get() = _isBluetoothAvailable

    private val _isBluetoothEnabled by lazy { MutableLiveData<Boolean>() }
    val isBluetoothEnabled: LiveData<Boolean> get() = _isBluetoothEnabled

    private val _bluetoothData by lazy { MutableLiveData<List<BluetoothEntry>>() }
    val bluetoothData: LiveData<List<BluetoothEntry>> get() = _bluetoothData

    private val _isMacAddressSaved by lazy { MutableLiveData<Boolean>() }
    val isMacAddressSaved: LiveData<Boolean> get() = _isMacAddressSaved

    // TODO Implement BLE ?
    fun isBluetoothAvailable() = launch {
        val bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()

        if (bluetoothAdapter == null) {
            _isBluetoothAvailable.postValue(false)
            return@launch
        }
        _isBluetoothAvailable.postValue(true)

        _isBluetoothEnabled.postValue(bluetoothAdapter.isEnabled)
    }

    fun getBluetoothDevices() = launch {
        when (val result = getBluetoothDevices.execute()) {
            is Result.Value -> _bluetoothData.postValue(result.value)
            is Result.Error -> _bluetoothData.postValue(listOf())
        }
    }

    override fun onCleared() {
        super.onCleared()
        unregisterBluetoothReceiver.execute()
    }

    fun saveMacAddress(macAddress: String) = launch {
        when (saveBluetoothAddress.execute(macAddress)) {
            is Result.Value -> _isMacAddressSaved.postValue(true)
            is Result.Error -> _isMacAddressSaved.postValue(false)
        }
    }
}

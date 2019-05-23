package com.example.paragonlite.ui.bluetooth

import android.bluetooth.BluetoothAdapter
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.paragonlite.shared.BaseViewModel
import kotlinx.coroutines.launch

class BluetoothViewModel(

) : BaseViewModel() {

    init {
        isBluetoothAvailable()
    }

    private val _isBluetoothAvailable = MutableLiveData<Boolean>()
    val isBluetoothAvailable: LiveData<Boolean> get() = _isBluetoothAvailable

    private val _isBluetoothEnabled = MutableLiveData<Boolean>()
    val isBluetoothEnabled: LiveData<Boolean> get() = _isBluetoothEnabled

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
}

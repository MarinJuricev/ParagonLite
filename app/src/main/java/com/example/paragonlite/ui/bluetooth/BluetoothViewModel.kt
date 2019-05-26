package com.example.paragonlite.ui.bluetooth

import android.bluetooth.BluetoothAdapter
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.domain.model.BluetoothEntry
import com.example.domain.model.Result
import com.example.domain.repository.IBluetoothRepository
import com.example.domain.usecase.bluetooth.GetNearbyBluetoothDevices
import com.example.paragonlite.shared.BaseViewModel
import kotlinx.coroutines.launch

class BluetoothViewModel(
    private val bluetoothRepository: IBluetoothRepository,
    private val getBluetoothDevices: GetNearbyBluetoothDevices
) : BaseViewModel() {

    init {
        isBluetoothAvailable()
    }

    private val _isBluetoothAvailable = MutableLiveData<Boolean>()
    val isBluetoothAvailable: LiveData<Boolean> get() = _isBluetoothAvailable

    private val _isBluetoothEnabled = MutableLiveData<Boolean>()
    val isBluetoothEnabled: LiveData<Boolean> get() = _isBluetoothEnabled

    private val _bluetoothData = MutableLiveData<List<BluetoothEntry>>()
    val bluetoothData: LiveData<List<BluetoothEntry>> get() = _bluetoothData

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
        when (val result = getBluetoothDevices.getNearbyBluetoothDevices(bluetoothRepository)) {
            is Result.Value -> _bluetoothData.postValue(result.value)
            is Result.Error -> _bluetoothData.postValue(listOf())
        }
    }

    override fun onCleared() {
        super.onCleared()
        bluetoothRepository.unRegisterReceiver()
    }
}

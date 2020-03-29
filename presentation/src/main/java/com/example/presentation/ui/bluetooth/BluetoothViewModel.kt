package com.example.presentation.ui.bluetooth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.model.BluetoothEntry
import com.example.domain.model.Result
import com.example.domain.shared.BLUETOOTH_MAC_ADDRESS_KEY
import com.example.domain.shared.ISharedPrefsService
import com.example.domain.usecase.bluetooth.GetBluetoothData
import com.example.domain.usecase.bluetooth.UnregisterBluetoothReceiver
import com.example.domain.usecase.bluetooth.UpdateBluetoothData
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

const val BLUETOOTH_REQUEST_ACCEPTED = -1
const val BLUETOOTH_REQUEST_DISMISSED = 0

class BluetoothViewModel(
    private val unregisterBluetoothReceiver: UnregisterBluetoothReceiver,
    private val updateBluetoothData: UpdateBluetoothData,
    private val getBluetoothData: GetBluetoothData,
    private val sharedPrefsService: ISharedPrefsService
) : ViewModel() {

    private val _isBluetoothAvailable by lazy { MutableLiveData<Boolean>() }
    val isBluetoothAvailable: LiveData<Boolean> get() = _isBluetoothAvailable

    private val _isBluetoothEnabled by lazy { MutableLiveData<BluetoothStatus>() }
    val isBluetoothEnabled: LiveData<BluetoothStatus> get() = _isBluetoothEnabled

    private val _bluetoothData by lazy { MutableLiveData<List<BluetoothEntry>>() }
    val bluetoothData: LiveData<List<BluetoothEntry>> get() = _bluetoothData

    private val _isMacAddressSaved by lazy { MutableLiveData<Boolean>() }
    val isMacAddressSaved: LiveData<Boolean> get() = _isMacAddressSaved

    fun isBluetoothAvailable(
        isBluetoothPresent: Boolean,
        enabled: Boolean
    ) = viewModelScope.launch {
        if (!isBluetoothPresent) {
            _isBluetoothAvailable.postValue(false)
            return@launch
        }
        _isBluetoothAvailable.postValue(true)

        val bluetoothStatus = if (enabled)
            BluetoothStatus.Enabled
        else
            BluetoothStatus.Disabled

        _isBluetoothEnabled.postValue(bluetoothStatus)
    }

    fun handleBluetoothRequest(resultCode: Int) {
        when (resultCode) {
            BLUETOOTH_REQUEST_ACCEPTED -> _isBluetoothEnabled.postValue(BluetoothStatus.Enabled)
            BLUETOOTH_REQUEST_DISMISSED -> _isBluetoothEnabled.postValue(BluetoothStatus.Dismissed)
        }
    }

    fun getData() = viewModelScope.launch {
        when (val result = getBluetoothData.execute()) {
            is Result.Value -> result.value.collect { data -> _bluetoothData.postValue(data) }
            is Result.Error -> _bluetoothData.postValue(listOf())
        }
    }

    fun updateBluetoothData() = viewModelScope.launch {
        when (val result = updateBluetoothData.execute()) {
            is Result.Value -> _bluetoothData.postValue(result.value)
            is Result.Error -> _bluetoothData.postValue(listOf())
        }
    }

    override fun onCleared() {
        super.onCleared()
        unregisterBluetoothReceiver.execute()
    }

    fun saveMacAddress(macAddress: String) = viewModelScope.launch {
        sharedPrefsService.saveValue(BLUETOOTH_MAC_ADDRESS_KEY, macAddress)
    }
}

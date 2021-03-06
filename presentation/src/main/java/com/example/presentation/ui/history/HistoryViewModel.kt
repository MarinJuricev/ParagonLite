package com.example.presentation.ui.history

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.model.Receipt
import com.example.domain.model.Result
import com.example.domain.shared.BLUETOOTH_MAC_ADDRESS_DEFAULT_VALUE
import com.example.domain.shared.BLUETOOTH_MAC_ADDRESS_KEY
import com.example.domain.shared.ISharedPrefsService
import com.example.domain.usecase.print.GenerateReceiptPrintData
import com.example.domain.usecase.print.PrintHistory
import com.example.domain.usecase.receipt.GetReceipts
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class HistoryViewModel(
    private val getReceipts: GetReceipts,
    private val generateReceiptPrintData: GenerateReceiptPrintData,
    private val printHistory: PrintHistory,
    private val sharedPrefsService: ISharedPrefsService
) : ViewModel() {

    private val _receiptData by lazy { MutableLiveData<List<Receipt>>() }
    val receiptData: LiveData<List<Receipt>> get() = _receiptData

    private val _getBluetoothAddressError by lazy { MutableLiveData<Boolean>() }
    val getBluetoothAddressError: LiveData<Boolean> get() = _getBluetoothAddressError

    fun fetchReceiptsFromTheSelectedDateRange(startDate: Long, endDate: Long) =
        viewModelScope.launch {
            when (val result = getReceipts.execute(startDate, endDate)) {
                is Result.Value -> result.value.collect { data -> _receiptData.value = data }
                is Result.Error -> _receiptData.value = listOf()
            }
        }

    fun prepareDataForPrint(receiptList: List<Receipt>) = viewModelScope.launch {
        when (val result = sharedPrefsService.getValue(
            BLUETOOTH_MAC_ADDRESS_KEY,
            BLUETOOTH_MAC_ADDRESS_DEFAULT_VALUE
        ) as String) {
            BLUETOOTH_MAC_ADDRESS_DEFAULT_VALUE -> _getBluetoothAddressError.postValue(true)
            else -> {
                generatePrintData(result, receiptList)
            }
        }
    }

    private suspend fun generatePrintData(
        savedMacAddress: String,
        receiptList: List<Receipt>
    ) {
        when (val result = generateReceiptPrintData.execute(
            receiptList
        )) {
            is Result.Value -> printHistory(savedMacAddress, result.value)
            is Result.Error -> _getBluetoothAddressError.postValue(true)
        }
    }

    private suspend fun printHistory(savedMacAddress: String, printData: List<ByteArray>) =
        printHistory.execute(savedMacAddress, printData)
}

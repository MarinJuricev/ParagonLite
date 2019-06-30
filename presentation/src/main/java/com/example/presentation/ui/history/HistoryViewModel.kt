package com.example.presentation.ui.history

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.example.domain.model.Receipt
import com.example.domain.model.Result
import com.example.domain.usecase.bluetooth.GetBluetoothAddress
import com.example.domain.usecase.print.GenerateReceiptPrintData
import com.example.domain.usecase.print.PrintHistory
import com.example.domain.usecase.receipt.GetReceipts
import com.example.presentation.shared.BaseViewModel
import kotlinx.coroutines.launch

class HistoryViewModel(
    private val getReceipts: GetReceipts,
    private val generateReceiptPrintData: GenerateReceiptPrintData,
    private val getBluetoothAddress: GetBluetoothAddress,
    private val printHistory: PrintHistory
) : BaseViewModel() {

    private val _receiptData by lazy { MediatorLiveData<List<Receipt>>() }
    val receiptData: LiveData<List<Receipt>> get() = _receiptData

    private val _getBluetoothAddressError by lazy { MutableLiveData<Boolean>() }
    //TODO CONSUME AS SNACKBAR IN FRAGMENT
    val getBluetoothAddressError: LiveData<Boolean> get() = _getBluetoothAddressError

    fun fetchReceiptsFromTheSelectedDateRange(startDate: String, endDate: String) = launch {
        when (val result = getReceipts.execute(startDate, endDate)) {
            is Result.Value -> {
                _receiptData.addSource(
                    result.value
                ) { newArticleList ->
                    _receiptData.value = newArticleList
                }
            }
            is Result.Error -> _receiptData.value = listOf()
        }
    }

    fun prepareDataForPrint() = launch {
        when (val result = getBluetoothAddress.execute()) {
            is Result.Value -> {
                generatePrintData(result.value)
            }
            is Result.Error -> _getBluetoothAddressError.postValue(true)
        }
    }

    private suspend fun generatePrintData(savedMacAddress: String) {
        when (val result = generateReceiptPrintData.execute(
            receiptData.value!!
        )) {
            is Result.Value -> printHistory(savedMacAddress, result.value)
            is Result.Error -> TODO()
        }
    }

    private suspend fun printHistory(savedMacAddress: String, printData: List<ByteArray>) {
        printHistory.execute(savedMacAddress, printData)
    }
}

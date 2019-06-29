package com.example.presentation.ui.history

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.example.domain.model.Receipt
import com.example.domain.model.Result
import com.example.domain.usecase.receipt.GetReceipts
import com.example.presentation.shared.BaseViewModel
import kotlinx.coroutines.launch

class HistoryViewModel(
    private val getReceipts: GetReceipts
) : BaseViewModel() {

    init {
        fetchReceiptData()
    }

    private val _receiptData by lazy { MediatorLiveData<List<Receipt>>() }
    val receiptData: LiveData<List<Receipt>> get() = _receiptData

    private fun fetchReceiptData() = launch {
        when (val result = getReceipts.execute()) {
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
}

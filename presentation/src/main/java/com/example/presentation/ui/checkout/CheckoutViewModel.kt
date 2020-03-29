package com.example.presentation.ui.checkout

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.model.CheckoutArticle
import com.example.domain.model.Result
import com.example.domain.shared.*
import com.example.domain.usecase.checkout.*
import com.example.domain.usecase.print.GeneratePrintData
import com.example.domain.usecase.print.PrintCheckout
import com.example.domain.usecase.receipt.AddReceipt
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class CheckoutViewModel(
    private val getArticlesInCheckout: GetArticlesInCheckout,
    private val deleteCheckoutArticle: DeleteCheckoutArticle,
    private val calculateCheckout: CalculateCheckout,
    private val generatePrintData: GeneratePrintData,
    private val printCheckout: PrintCheckout,
    private val getArticlesInCheckoutSize: GetArticlesInCheckoutSize,
    private val updateCheckoutArticle: UpdateCheckoutArticle,
    private val addReceipt: AddReceipt,
    private val sharedPrefsService: ISharedPrefsService
) : ViewModel() {

    init {
        fetchCheckoutArticles()
        startObservingCheckoutSize()
    }

    private val _checkoutArticles by lazy { MutableLiveData<List<CheckoutArticle>>() }
    val articleData: LiveData<List<CheckoutArticle>> get() = _checkoutArticles

    private val _isArticleDeletionSuccess by lazy { MutableLiveData<Boolean>() }
    val isArticleDeletionSuccess: LiveData<Boolean> get() = _isArticleDeletionSuccess

    private val _checkoutValue by lazy { MutableLiveData<String>() }
    val checkoutValue: LiveData<String> get() = _checkoutValue

    private val _getBluetoothAddressError by lazy { MutableLiveData<Boolean>() }
    val getBluetoothAddressError: LiveData<Boolean> get() = _getBluetoothAddressError

    private val _checkoutBadgeCount by lazy { MutableLiveData<Int>() }
    val checkoutBadgeCount: LiveData<Int> get() = _checkoutBadgeCount

    private val _articleUpdate by lazy { MutableLiveData<Boolean>() }
    val articleUpdate: LiveData<Boolean> get() = _articleUpdate

    private fun fetchCheckoutArticles() = viewModelScope.launch {
        when (val result = getArticlesInCheckout.execute()) {
            is Result.Value -> result.value.collect { data ->
                run {
                    _checkoutArticles.postValue(data)
                    updateCheckoutAmount(data)
                }
            }
            is Result.Error -> _checkoutArticles.value = listOf()
        }
    }

    private fun updateCheckoutAmount(newArticleList: List<CheckoutArticle>?) =
        viewModelScope.launch {
            val list = newArticleList ?: listOf()

            _checkoutValue.postValue(calculateCheckout.execute(list))
        }

    private fun startObservingCheckoutSize() = viewModelScope.launch {
        val result = getArticlesInCheckoutSize.execute()
        result
            .map { value: Int? -> value ?: 0 }
            .collect { data -> _checkoutBadgeCount.value = data }
    }

    fun deleteArticle(checkoutArticle: CheckoutArticle) = viewModelScope.launch {
        when (deleteCheckoutArticle.execute(checkoutArticle)) {
            is Result.Value -> _isArticleDeletionSuccess.postValue(true)
            is Result.Error -> _isArticleDeletionSuccess.postValue(false)
        }
    }

    fun printCheckout() = viewModelScope.launch {
        when (val result = sharedPrefsService.getValue(
            BLUETOOTH_MAC_ADDRESS_KEY,
            BLUETOOTH_MAC_ADDRESS_DEFAULT_VALUE
        ) as String) {
            BLUETOOTH_MAC_ADDRESS_DEFAULT_VALUE -> _getBluetoothAddressError.postValue(true)
            else -> {
                generateDataToPrint(
                    result,
                    Integer.parseInt(
                        sharedPrefsService.getValue(
                            RECEIPT_KEY,
                            RECEIPT_DEFAULT_VALUE
                        ) as String
                    )
                )
            }
        }
    }

    private suspend fun generateDataToPrint(macAddress: String, receiptNumber: Int) =
        viewModelScope.launch {
            when (val result = generatePrintData.execute(
                articleData.value ?: listOf(),
                checkoutValue.value ?: "",
                receiptNumber
            )) {
                is Result.Value -> {
                    printGeneratedData(result.value, macAddress, receiptNumber)
                }
            }
        }

    private suspend fun printGeneratedData(
        dataToPrint: List<ByteArray>,
        macAddress: String,
        receiptNumber: Int
    ) = viewModelScope.launch {
        when (printCheckout.execute(
            dataToPrint,
            macAddress,
            checkoutValue.value!!,
            Integer.parseInt(
                sharedPrefsService.getValue(
                    RECEIPT_KEY,
                    RECEIPT_DEFAULT_VALUE
                ) as String
            ),
            addReceipt
        )) {
            is Result.Value -> {
                val incrementedReceiptNumber = receiptNumber + 1
                sharedPrefsService.saveValue(RECEIPT_KEY, incrementedReceiptNumber.toString())
            }
        }
    }

    fun updateArticle(checkoutArticle: CheckoutArticle) = viewModelScope.launch {
        when (updateCheckoutArticle.execute(checkoutArticle)) {
            is Result.Value -> _articleUpdate.postValue(true)
            is Result.Error -> _articleUpdate.postValue(false)
        }
    }
}

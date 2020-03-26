package com.example.presentation.ui.checkout

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.example.domain.model.CheckoutArticle
import com.example.domain.model.Result
import com.example.domain.shared.*
import com.example.domain.usecase.checkout.*
import com.example.domain.usecase.print.GeneratePrintData
import com.example.domain.usecase.print.PrintCheckout
import com.example.domain.usecase.receipt.AddReceipt
import com.example.presentation.shared.BaseViewModel
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
) : BaseViewModel() {

    init {
        fetchCheckoutArticles()
        startObservingCheckoutSize()
    }

    private val _checkoutArticles by lazy { MediatorLiveData<List<CheckoutArticle>>() }
    val articleData: LiveData<List<CheckoutArticle>> get() = _checkoutArticles

    private val _isArticleDeletionSuccess by lazy { MutableLiveData<Boolean>() }

    private val _checkoutValue by lazy { MutableLiveData<String>() }
    val checkoutValue: LiveData<String> get() = _checkoutValue

    private val _getBluetoothAddressError by lazy { MutableLiveData<Boolean>() }
    val getBluetoothAddressError: LiveData<Boolean> get() = _getBluetoothAddressError

    private val _checkoutBadgeCount by lazy { MediatorLiveData<Int>() }
    val checkoutBadgeCount: LiveData<Int> get() = _checkoutBadgeCount

    private val _articleUpdate by lazy { MutableLiveData<Boolean>() }
    val articleUpdate: LiveData<Boolean> get() = _articleUpdate

    private fun fetchCheckoutArticles() = launch {
        when (val result = getArticlesInCheckout.execute()) {
            is Result.Value -> {
                _checkoutArticles.addSource(
                    result.value
                ) { newArticleList ->
                    _checkoutArticles.value = newArticleList
                    updateCheckoutAmount(newArticleList)
                }
            }
            is Result.Error -> _checkoutArticles.value = listOf()
        }
    }

    private fun startObservingCheckoutSize() = launch {
        val result = getArticlesInCheckoutSize.execute()

        _checkoutBadgeCount.addSource(result) {
            _checkoutBadgeCount.value = it ?: 0
        }
    }

    private fun updateCheckoutAmount(newArticleList: List<CheckoutArticle>?) = launch {
        val list = newArticleList ?: listOf()

        _checkoutValue.postValue(calculateCheckout.execute(list))
    }

    fun deleteArticle(checkoutArticle: CheckoutArticle) = launch {
        when (deleteCheckoutArticle.execute(checkoutArticle)) {
            is Result.Value -> _isArticleDeletionSuccess.postValue(true)
            is Result.Error -> _isArticleDeletionSuccess.postValue(false)
        }
    }

    fun printCheckout() = launch {
        when (val result = sharedPrefsService.getValue(
            BLUETOOTH_MAC_ADDRESS_KEY,
            BLUETOOTH_MAC_ADDRESS_DEFAULT_VALUE
        ) as String) {
            "" -> _getBluetoothAddressError.postValue(true)
            else -> {
                generateDataToPrint(
                    result,
                    Integer.parseInt(sharedPrefsService.getValue(RECEIPT_KEY, RECEIPT_DEFAULT_VALUE) as String)
                )
            }
        }
    }

    private suspend fun generateDataToPrint(macAddress: String, receiptNumber: Int) = launch {
        when (val result = generatePrintData.execute(
            articleData.value!!,
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
    ) = launch {
        when (printCheckout.execute(
            dataToPrint,
            macAddress,
            checkoutValue.value!!,
            Integer.parseInt(sharedPrefsService.getValue(RECEIPT_KEY, RECEIPT_DEFAULT_VALUE) as String),
            addReceipt
        )) {
            is Result.Value -> {
                val incrementedReceiptNumber = receiptNumber + 1
                sharedPrefsService.saveValue(RECEIPT_KEY, incrementedReceiptNumber.toString())
            }
        }
    }

    fun updateArticle(checkoutArticle: CheckoutArticle) = launch {
        when (updateCheckoutArticle.execute(checkoutArticle)) {
            is Result.Value -> _articleUpdate.postValue(true)
            is Result.Error -> _articleUpdate.postValue(false)
        }
    }
}

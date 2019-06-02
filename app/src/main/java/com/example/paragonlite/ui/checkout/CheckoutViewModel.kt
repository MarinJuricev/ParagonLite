package com.example.paragonlite.ui.checkout

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.example.domain.DispatcherProvider
import com.example.domain.model.CheckoutArticle
import com.example.domain.model.Result
import com.example.domain.repository.IBluetoothRepository
import com.example.domain.repository.ICheckoutRepository
import com.example.domain.usecase.bluetooth.GetBluetoothAddress
import com.example.domain.usecase.checkout.CalculateCheckout
import com.example.domain.usecase.checkout.DeleteCheckoutArticle
import com.example.domain.usecase.checkout.GetArticlesInCheckout
import com.example.domain.usecase.print.GeneratePrintData
import com.example.domain.usecase.print.PrintCheckout
import com.example.paragonlite.shared.BaseViewModel
import kotlinx.coroutines.launch


class CheckoutViewModel(
    private val checkoutRepository: ICheckoutRepository,
    private val bluetoothRepository: IBluetoothRepository,
    private val getArticlesInCheckout: GetArticlesInCheckout,
    private val deleteCheckoutArticle: DeleteCheckoutArticle,
    private val calculateCheckout: CalculateCheckout,
    private val getBluetoothMacAddress: GetBluetoothAddress,
    private val generatePrintData: GeneratePrintData,
    private val printCheckout: PrintCheckout,
    private val dispatcherProvider: DispatcherProvider
) : BaseViewModel() {

    init {
        fetchCheckoutArticles()
    }

    private val _checkoutArticles = MediatorLiveData<List<CheckoutArticle>>()
    val articleData: LiveData<List<CheckoutArticle>> get() = _checkoutArticles

    private val _isArticleDeletionSuccess = MutableLiveData<Boolean>()
    //TODO Implement after crunch is over...
    val isArticleDeletionSuccess: LiveData<Boolean> get() = _isArticleDeletionSuccess

    private val _checkoutValue = MutableLiveData<String>()
    val checkoutValue: LiveData<String> get() = _checkoutValue

    private fun fetchCheckoutArticles() = launch {
        when (val result = getArticlesInCheckout.execute(checkoutRepository)) {
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

    private fun updateCheckoutAmount(newArticleList: List<CheckoutArticle>?) = launch {
        val list = newArticleList ?: listOf()

        _checkoutValue.postValue(calculateCheckout.execute(dispatcherProvider, list))
    }

    fun deleteArticle(checkoutArticle: CheckoutArticle) = launch {
        when (deleteCheckoutArticle.execute(checkoutRepository, checkoutArticle)) {
            is Result.Value -> _isArticleDeletionSuccess.postValue(true)
            is Result.Error -> _isArticleDeletionSuccess.postValue(false)
        }
    }

    fun printCheckout() = launch {

        when(val result = getBluetoothMacAddress.execute(bluetoothRepository)){
            is Result.Value -> {
                generateDataToPrint(result.value)
            }
            is Result.Error -> TODO()
        }

    }

    private suspend fun generateDataToPrint(macAddress: String) {
        when(val result = generatePrintData.execute(articleData.value!!, dispatcherProvider)){
            is Result.Value -> printGeneratedData(result.value, macAddress)
            is Result.Error -> TODO()
        }
    }

    private suspend fun printGeneratedData(
        dataToPrint: List<ByteArray>,
        macAddress: String
    ) {
        printCheckout.execute(
            bluetoothRepository,
            checkoutRepository,
            dispatcherProvider,
            dataToPrint,
            macAddress
        )
    }
}

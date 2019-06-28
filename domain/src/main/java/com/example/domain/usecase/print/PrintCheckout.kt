package com.example.domain.usecase.print

import com.example.domain.model.CheckoutArticle
import com.example.domain.model.Result
import com.example.domain.repository.IBluetoothRepository
import com.example.domain.repository.ICheckoutRepository
import com.example.domain.repository.IReceiptRepository

class PrintCheckout(
    private val bluetoothRepository: IBluetoothRepository,
    private val checkoutRepository: ICheckoutRepository,
    private val receiptRepository: IReceiptRepository
) {

    suspend fun execute(
        article: CheckoutArticle,
        valuesToPrint: List<ByteArray>,
        savedMacAddress: String
    ): Result<Exception, Unit> {
        return when (val result = bluetoothRepository.connectAndSendDataOverBluetooth(
            savedMacAddress,
            valuesToPrint
        )) {
            is Result.Value -> {
                deleteCurrentCheckoutItems(checkoutRepository)
                addToReceiptRepo(article)
                result
            }
            is Result.Error -> result
        }
    }

    private suspend fun addToReceiptRepo(article: CheckoutArticle) {
        receiptRepository.addReceipt(article.toReceipt(savedMacAddress))
    }

    private suspend fun deleteCurrentCheckoutItems(
        checkoutRepository: ICheckoutRepository
    ) = checkoutRepository.deleteAllArticles()

}



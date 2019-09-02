package com.example.domain.usecase.print

import com.example.domain.model.Result
import com.example.domain.repository.IBluetoothRepository
import com.example.domain.repository.ICheckoutRepository
import com.example.domain.usecase.receipt.AddReceipt

class PrintCheckout(
    private val bluetoothRepository: IBluetoothRepository,
    private val checkoutRepository: ICheckoutRepository
    ) {

    suspend fun execute(
        valuesToPrint: List<ByteArray>,
        savedMacAddress: String,
        currentCheckout: String,
        receiptNumber: Int,
        addReceipt: AddReceipt
    ): Result<Exception, Unit> {
        return when (val result = bluetoothRepository.connectAndSendDataOverBluetooth(
            savedMacAddress,
            valuesToPrint
        )) {
            is Result.Value -> {
                deleteCurrentCheckoutItems(checkoutRepository)
                addToReceiptRepo(addReceipt, currentCheckout, receiptNumber)
                result
            }
            is Result.Error -> result
        }
    }

    private suspend fun addToReceiptRepo(
        addReceipt: AddReceipt,
        currentCheckout: String,
        receiptNumber: Int
    ) {
        addReceipt.execute(
            receiptNumber,
            currentCheckout
        )
    }

    private suspend fun deleteCurrentCheckoutItems(
        checkoutRepository: ICheckoutRepository
    ) = checkoutRepository.deleteAllArticles()

}



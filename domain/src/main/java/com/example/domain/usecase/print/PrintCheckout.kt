package com.example.domain.usecase.print

import com.example.domain.error.ParagonError
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
        receiptNumber: GetReceiptNumber,
        currentCheckout: String,
        addReceipt: AddReceipt
    ): Result<Exception, Unit> {
        return when (val result = bluetoothRepository.connectAndSendDataOverBluetooth(
            savedMacAddress,
            valuesToPrint
        )) {
            is Result.Value -> {
                deleteCurrentCheckoutItems(checkoutRepository)
                addToReceiptRepo(addReceipt, receiptNumber, currentCheckout)
                result
            }
            is Result.Error -> result
        }
    }

    private suspend fun addToReceiptRepo(
        addReceipt: AddReceipt,
        getReceiptNumber: GetReceiptNumber,
        currentCheckout: String
    ) {

        when (val result = getReceiptNumber.execute()) {
            is Result.Value -> {
                addReceipt.execute(result.value, currentCheckout)
            }
            is Result.Error -> {
                throw ParagonError.LocalIOException
            }
        }
    }

    private suspend fun deleteCurrentCheckoutItems(
        checkoutRepository: ICheckoutRepository
    ) = checkoutRepository.deleteAllArticles()

}



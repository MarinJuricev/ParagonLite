package com.example.domain.usecase.receipt

import com.example.domain.model.Receipt
import com.example.domain.model.Result
import com.example.domain.repository.IReceiptRepository
import java.text.SimpleDateFormat
import java.util.*

class AddReceipt(
    private val receiptRepository: IReceiptRepository
) {

    suspend fun execute(receiptNumber: Int, currentCheckout: String): Result<Exception, Unit> {
        val receipt = Receipt(
            receiptNumber,
            generateCurrentTime(),
            currentCheckout.toDouble())

        return receiptRepository.addReceipt(receipt)
    }
}

fun generateCurrentTime(): String {
    val formatter = SimpleDateFormat("dd.MM.yy", Locale.ENGLISH)
    return formatter.format(Date())
}
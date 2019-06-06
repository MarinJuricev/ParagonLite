package com.example.domain.usecase.print

import com.example.domain.model.Result
import com.example.domain.repository.ICheckoutRepository

class SaveReceiptNumber {

    suspend fun execute(
        checkoutRepository: ICheckoutRepository,
        receiptNumber: Int
    ): Result<Exception, Unit> = checkoutRepository.saveReceiptNumber(receiptNumber)
}
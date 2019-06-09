package com.example.domain.usecase.print

import com.example.domain.model.Result
import com.example.domain.repository.ICheckoutRepository

class SaveReceiptNumber(
    private val checkoutRepository: ICheckoutRepository
) {
    suspend fun execute(
        receiptNumber: Int
    ): Result<Exception, Unit> = checkoutRepository.saveReceiptNumber(receiptNumber)
}
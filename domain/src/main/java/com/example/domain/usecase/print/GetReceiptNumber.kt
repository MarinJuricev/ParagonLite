package com.example.domain.usecase.print

import com.example.domain.model.Result
import com.example.domain.repository.ICheckoutRepository

class GetReceiptNumber(
    private val checkoutRepository: ICheckoutRepository
) {
    suspend fun execute(): Result<Exception, Int> = checkoutRepository.getReceiptNumber()
}
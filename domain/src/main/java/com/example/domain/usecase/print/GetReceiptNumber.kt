package com.example.domain.usecase.print

import com.example.domain.model.Result
import com.example.domain.repository.ICheckoutRepository

class GetReceiptNumber {

    suspend fun execute(
        checkoutRepository: ICheckoutRepository
        ): Result<Exception, Int> = checkoutRepository.getReceiptNumber()
}
package com.example.domain.usecase.checkout

import com.example.domain.model.CheckoutArticle
import com.example.domain.model.Result
import com.example.domain.repository.ICheckoutRepository

class UpdateCheckoutArticle(
    private val checkoutRepository: ICheckoutRepository
) {
    suspend fun execute(
        checkoutArticle: CheckoutArticle
    ): Result<Exception, Unit> = checkoutRepository.updateArticle(checkoutArticle)
}

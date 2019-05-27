package com.example.domain.usecase.checkout

import com.example.domain.model.CheckoutArticle
import com.example.domain.model.Result
import com.example.domain.repository.ICheckoutRepository
import java.lang.Exception

class DeleteCheckoutArticle {

    suspend fun execute(
        checkoutRepository: ICheckoutRepository,
        checkoutArticle: CheckoutArticle
    ): Result<Exception, Unit> = checkoutRepository.deleteArticle(checkoutArticle)
}
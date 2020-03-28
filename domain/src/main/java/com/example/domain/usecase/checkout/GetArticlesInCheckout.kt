package com.example.domain.usecase.checkout

import com.example.domain.model.CheckoutArticle
import com.example.domain.model.Result
import com.example.domain.repository.ICheckoutRepository
import kotlinx.coroutines.flow.Flow

class GetArticlesInCheckout(
    private val checkoutRepository: ICheckoutRepository
) {
    suspend fun execute(
    ): Result<Exception, Flow<List<CheckoutArticle>>> = checkoutRepository.getArticlesInCheckout()
}
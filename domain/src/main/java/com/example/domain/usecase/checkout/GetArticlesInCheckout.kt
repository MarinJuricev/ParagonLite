package com.example.domain.usecase.checkout

import androidx.lifecycle.LiveData
import com.example.domain.model.CheckoutArticle
import com.example.domain.model.Result
import com.example.domain.repository.ICheckoutRepository

class GetArticlesInCheckout(
    private val checkoutRepository: ICheckoutRepository
) {
    suspend fun execute(

    ): Result<Exception, LiveData<List<CheckoutArticle>>> = checkoutRepository.getArticlesInCheckout()
}
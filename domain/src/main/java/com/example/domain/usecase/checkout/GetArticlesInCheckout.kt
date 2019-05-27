package com.example.domain.usecase.checkout

import androidx.lifecycle.LiveData
import com.example.domain.model.CheckoutArticle
import com.example.domain.model.Result
import com.example.domain.repository.ICheckoutRepository
import java.lang.Exception

class GetArticlesInCheckout {

    suspend fun execute(
        checkoutRepository: ICheckoutRepository
    ): Result<Exception, LiveData<List<CheckoutArticle>>> = checkoutRepository.getArticlesInCheckout()
}
package com.example.domain.usecase.checkout

import com.example.domain.DispatcherProvider
import com.example.domain.model.CheckoutArticle
import kotlinx.coroutines.withContext

class CalculateCheckout {

    suspend fun execute(
        dispatcherProvider: DispatcherProvider,
        checkoutList: List<CheckoutArticle>
    ) = withContext(dispatcherProvider.provideComputationContext()){
        var result = 0.0

        checkoutList.map {
            result += it.price
        }

        return@withContext result.toString()
    }
}
package com.example.domain.usecase.checkout

import com.example.domain.DispatcherProvider
import com.example.domain.model.CheckoutArticle
import kotlinx.coroutines.withContext

class CalculateCheckout(
    private val dispatcherProvider: DispatcherProvider
) {
    suspend fun execute(
        checkoutList: List<CheckoutArticle>
    ) = withContext(dispatcherProvider.provideComputationContext()) {
        var result = 0.0

        checkoutList.map {
            result += it.price * it.inCheckout
        }

        return@withContext result.toString()
    }
}
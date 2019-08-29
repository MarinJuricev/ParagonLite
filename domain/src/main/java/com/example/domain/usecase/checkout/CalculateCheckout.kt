package com.example.domain.usecase.checkout

import com.example.domain.shared.DispatcherProvider
import com.example.domain.model.CheckoutArticle
import kotlinx.coroutines.withContext

class CalculateCheckout(
    private val dispatcherProvider: DispatcherProvider
) {
    suspend fun execute(
        checkoutList: List<CheckoutArticle>
    ): String = withContext(dispatcherProvider.provideComputationContext()) {
        var result = 0.00

        checkoutList.map {
            result += it.price * it.inCheckout
        }

        return@withContext "%.2f".format(result)
    }
}
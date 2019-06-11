package com.example.domain.usecase.checkout

import androidx.lifecycle.LiveData
import com.example.domain.repository.ICheckoutRepository

class GetArticlesInCheckoutSize(
    private val checkoutRepository: ICheckoutRepository
) {
    suspend fun execute(
    ): LiveData<Int> = checkoutRepository.getArticlesInCheckoutSize()
}
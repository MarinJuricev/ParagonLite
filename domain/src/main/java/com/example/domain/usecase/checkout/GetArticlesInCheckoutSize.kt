package com.example.domain.usecase.checkout

import com.example.domain.repository.ICheckoutRepository
import kotlinx.coroutines.flow.Flow

class GetArticlesInCheckoutSize(
    private val checkoutRepository: ICheckoutRepository
) {
    suspend fun execute(
    ): Flow<Int> = checkoutRepository.getArticlesInCheckoutSize()
}
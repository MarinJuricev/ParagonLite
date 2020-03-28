package com.example.domain.usecase.checkout

import com.example.domain.model.Article
import com.example.domain.model.Result
import com.example.domain.repository.ICheckoutRepository
import kotlinx.coroutines.flow.Flow

class SendArticleToCheckout(
    private val checkoutRepository: ICheckoutRepository
) {
    suspend fun execute(
        article: Article
    ): Result<Exception, Flow<Int>> = checkoutRepository.sendArticleToCheckout(article)
}
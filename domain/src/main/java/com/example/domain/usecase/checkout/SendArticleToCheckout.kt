package com.example.domain.usecase.checkout

import com.example.domain.model.Article
import com.example.domain.model.Result
import com.example.domain.repository.ICheckoutRepository

class SendArticleToCheckout {

    suspend fun sendArticleToCheckout(
        checkoutRepository: ICheckoutRepository,
        article: Article
    ): Result<Exception, Unit> = checkoutRepository.sendArticleToCheckout(article)
}
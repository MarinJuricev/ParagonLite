package com.example.domain.usecase.checkout

import androidx.lifecycle.LiveData
import com.example.domain.model.Article
import com.example.domain.model.Result
import com.example.domain.repository.ICheckoutRepository

class SendArticleToCheckout(
    private val checkoutRepository: ICheckoutRepository
) {
    suspend fun execute(
        article: Article
    ): Result<Exception, LiveData<Int>> = checkoutRepository.sendArticleToCheckout(article)
}
package com.example.domain.di

import com.example.domain.usecase.checkout.CalculateCheckout
import com.example.domain.usecase.checkout.DeleteCheckoutArticle
import com.example.domain.usecase.checkout.GetArticlesInCheckout
import com.example.domain.usecase.checkout.SendArticleToCheckout
import org.koin.dsl.module

val checkoutModule = module {
    factory { SendArticleToCheckout() }
    factory { GetArticlesInCheckout() }
    factory { DeleteCheckoutArticle() }
    factory { CalculateCheckout() }
}
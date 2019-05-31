package com.example.domain.di

import com.example.domain.usecase.checkout.*
import com.example.domain.usecase.print.PrintCheckout
import org.koin.dsl.module

val checkoutModule = module {
    factory { SendArticleToCheckout() }
    factory { GetArticlesInCheckout() }
    factory { DeleteCheckoutArticle() }
    factory { CalculateCheckout() }
    factory { PrintCheckout() }
}
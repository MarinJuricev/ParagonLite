package com.example.domain.di

import com.example.domain.usecase.checkout.*
import com.example.domain.usecase.print.PrintCheckout
import org.koin.dsl.module

val checkoutModule = module {
    factory { SendArticleToCheckout(get()) }
    factory { GetArticlesInCheckout(get()) }
    factory { DeleteCheckoutArticle(get()) }
    factory { CalculateCheckout(get()) }
    factory { PrintCheckout(get(), get()) }
    factory { GetArticlesInCheckoutSize(get()) }
}
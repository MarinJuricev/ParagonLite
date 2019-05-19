package com.example.domain.di

import com.example.domain.usecase.checkout.SendArticleToCheckout
import org.koin.dsl.module

val checkoutModule = module {
    factory { SendArticleToCheckout() }
}
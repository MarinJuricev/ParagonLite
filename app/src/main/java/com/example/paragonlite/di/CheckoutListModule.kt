package com.example.paragonlite.di

import com.example.paragonlite.ui.checkout.CheckoutViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val checkoutListModule = module {
    viewModel {
        CheckoutViewModel(
            get(), get(), get(), get(), get(), get(),
            get(), get()
        )
    }
}
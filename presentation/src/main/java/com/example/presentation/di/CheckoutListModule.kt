package com.example.presentation.di

import com.example.presentation.ui.checkout.CheckoutViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val checkoutListModule = module {
    viewModel {
        CheckoutViewModel(
            get(), get(), get(), get(), get(), get(),
            get(), get(), get()
        )
    }
}
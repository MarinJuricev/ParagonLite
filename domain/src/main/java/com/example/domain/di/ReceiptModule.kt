package com.example.domain.di

import com.example.domain.usecase.receipt.GetReceipts
import org.koin.dsl.module

val receiptModule = module {
    factory { GetReceipts(get()) }
}
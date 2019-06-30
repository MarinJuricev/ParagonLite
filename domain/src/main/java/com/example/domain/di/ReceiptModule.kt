package com.example.domain.di

import com.example.domain.usecase.print.GenerateReceiptPrintData
import com.example.domain.usecase.print.PrintHistory
import com.example.domain.usecase.receipt.AddReceipt
import com.example.domain.usecase.receipt.GetReceipts
import org.koin.dsl.module

val receiptModule = module {
    factory { GetReceipts(get()) }
    factory { AddReceipt(get()) }
    factory { GenerateReceiptPrintData(get()) }
    factory { PrintHistory(get()) }
}
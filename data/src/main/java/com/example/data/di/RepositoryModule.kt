package com.example.data.di

import com.example.data.article.ArticleRepositoryImpl
import com.example.data.bluetooth.BluetoothRepositoryImpl
import com.example.data.checkout.CheckoutRepositoryImpl
import com.example.data.receipt.ReceiptRepositoryImpl
import com.example.domain.repository.IArticleRepository
import com.example.domain.repository.IBluetoothRepository
import com.example.domain.repository.ICheckoutRepository
import com.example.domain.repository.IReceiptRepository
import org.koin.dsl.module

val repositoryModule = module {
    factory { ArticleRepositoryImpl(get(), get()) as IArticleRepository }
    factory { CheckoutRepositoryImpl(get(), get()) as ICheckoutRepository }
    factory { BluetoothRepositoryImpl(get(), get(), get()) as IBluetoothRepository }
    factory { ReceiptRepositoryImpl(get(), get()) as IReceiptRepository }
}
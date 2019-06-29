package com.example.data.di

import com.example.data.article.ArticleDataBase
import com.example.data.bluetooth.BluetoothDatabase
import com.example.data.checkout.CheckoutDatabase
import com.example.data.receipt.ReceiptDatabase
import org.koin.dsl.module

val localPersistableModule = module {
    single { ArticleDataBase.invoke(get()) }
    factory { (get() as ArticleDataBase).articleDao() }

    single { CheckoutDatabase.invoke(get()) }
    factory { (get() as CheckoutDatabase).checkoutDao() }

    single { BluetoothDatabase.invoke(get()) }
    factory { (get() as BluetoothDatabase).bluetoothDao() }

    single { ReceiptDatabase.invoke(get()) }
    factory { (get() as ReceiptDatabase).receiptDao() }
}
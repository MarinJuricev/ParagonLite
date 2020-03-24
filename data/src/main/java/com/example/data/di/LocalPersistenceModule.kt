package com.example.data.di

import com.example.data.local.ParagonDatabase
import org.koin.dsl.module

val localPersistableModule = module {
    single { ParagonDatabase.invoke(get()) }

    factory { (get() as ParagonDatabase).articleDao() }
    factory { (get() as ParagonDatabase).checkoutDao() }
    factory { (get() as ParagonDatabase).bluetoothDao() }
    factory { (get() as ParagonDatabase).receiptDao() }
}
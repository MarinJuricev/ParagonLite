package com.example.data.di

import com.example.data.article.ArticleDataBase
import com.example.data.checkout.CheckoutDatabase
import org.koin.dsl.module

val localPersistableModule = module {
    single { ArticleDataBase.invoke(get()) }
    factory { (get() as ArticleDataBase).articleDao() }

    single { CheckoutDatabase.invoke(get()) }
    factory { (get() as CheckoutDatabase).checkoutDao() }

}
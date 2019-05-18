package com.example.data.di

import com.example.data.article.ArticleDataBase
import org.koin.dsl.module

val localPersistableModule = module {
    single { ArticleDataBase.invoke(get()) }
    factory { (get() as ArticleDataBase).articleDao() }
}
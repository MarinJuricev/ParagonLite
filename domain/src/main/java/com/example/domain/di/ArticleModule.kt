package com.example.domain.di

import com.example.domain.usecase.article.CreateArticle
import com.example.domain.usecase.article.DeleteArticle
import com.example.domain.usecase.article.GetArticles
import org.koin.dsl.module

val articleModule = module {
    factory { CreateArticle(get()) }
    factory { GetArticles(get()) }
    factory { DeleteArticle(get()) }
}
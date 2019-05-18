package com.example.data.di

import com.example.data.article.ArticleRepositoryImpl
import com.example.domain.repository.IArticleRepository
import org.koin.dsl.module

val repositoryModule = module {
    factory { ArticleRepositoryImpl(get(), get()) as IArticleRepository }
}
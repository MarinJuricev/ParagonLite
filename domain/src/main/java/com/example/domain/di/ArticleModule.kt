package com.example.domain.di

import com.example.domain.usecase.CreateArticle
import org.koin.dsl.module

val articleModule = module {
    factory { CreateArticle() }
}
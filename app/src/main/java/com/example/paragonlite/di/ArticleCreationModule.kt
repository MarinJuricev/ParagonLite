package com.example.paragonlite.di

import com.example.paragonlite.ui.articles.creation.ArticleCreationViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val articleCreationFeatureModule = module {
    viewModel { ArticleCreationViewModel(get()) }
}
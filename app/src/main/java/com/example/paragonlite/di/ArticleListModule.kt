package com.example.paragonlite.di

import com.example.paragonlite.ui.articles.preview.ArticlesListViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val articleListModule = module {
    viewModel { ArticlesListViewModel(get(), get(), get()) }
}
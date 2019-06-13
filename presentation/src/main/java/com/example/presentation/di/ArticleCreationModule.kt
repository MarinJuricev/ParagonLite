package com.example.presentation.di

import com.example.presentation.ui.articles.creation.ArticleCreationViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val articleCreationFeatureModule = module {
    viewModel { ArticleCreationViewModel(get()) }
}
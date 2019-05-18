package com.example.domain.di

import com.example.domain.DispatcherProvider
import org.koin.dsl.module

val dispatcherModule = module {
    factory { DispatcherProvider }
}
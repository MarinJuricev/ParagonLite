package com.example.data.di

import com.example.data.shared.SharedPrefsService
import com.example.domain.shared.ISharedPrefsService
import org.koin.dsl.module

val prefsModule = module {
    factory { SharedPrefsService(get()) as ISharedPrefsService }
}
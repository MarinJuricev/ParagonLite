package com.example.paragonlite

import android.app.Application
import com.example.data.di.localPersistableModule
import com.example.data.di.repositoryModule
import com.example.domain.di.*
import com.example.presentation.di.*
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class ParagonApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        configureDi()
    }

    private fun configureDi() {
        startKoin {
            androidContext(this@ParagonApplication)
            modules(
                listOf(
                    dispatcherModule, articleCreationFeatureModule,
                    articleModule, localPersistableModule, repositoryModule,
                    articleListModule, checkoutModule, checkoutListModule,
                    bluetoothModule, bluetoothDomainModel, historyModule,
                    receiptModule
                )
            )
        }
    }
}
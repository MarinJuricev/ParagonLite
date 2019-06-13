package com.example.paragonlite

import android.app.Application
import com.example.data.di.localPersistableModule
import com.example.data.di.repositoryModule
import com.example.domain.di.articleModule
import com.example.domain.di.bluetoothDomainModel
import com.example.domain.di.checkoutModule
import com.example.domain.di.dispatcherModule
import com.example.presentation.di.articleCreationFeatureModule
import com.example.presentation.di.articleListModule
import com.example.presentation.di.bluetoothModule
import com.example.presentation.di.checkoutListModule
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
                    bluetoothModule, bluetoothDomainModel
                )
            )
        }
    }
}
package com.artybyte.test.whacamole

import android.app.Application
import com.artybyte.test.whacamole.di.AppModule
import com.artybyte.test.whacamole.di.NavigationModule
import com.github.terrakok.cicerone.Cicerone
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App: Application() {
    private val cicerone = Cicerone.create()
    val router get() = cicerone.router
    val navigatorHolder get() = cicerone.getNavigatorHolder()

    override fun onCreate() {
        super.onCreate()

        // Koin DI modules
        startKoin {
            androidContext(this@App)
            modules(listOf(AppModule, NavigationModule))
        }

    }
}
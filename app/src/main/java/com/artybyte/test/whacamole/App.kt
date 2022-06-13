package com.artybyte.test.whacamole

import android.app.Application
import android.content.Intent
import android.util.Log
import com.artybyte.test.whacamole.di.AppModule
import com.artybyte.test.whacamole.di.NavigationModule
import com.artybyte.test.whacamole.observe.PreferencesLoadedEventObserver
import com.artybyte.test.whacamole.ui.common.AppScreens
import com.github.terrakok.cicerone.Cicerone
import com.github.terrakok.cicerone.Router
import com.github.terrakok.cicerone.androidx.FragmentScreen
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App: Application() {

    private val cicerone = Cicerone.create()
    val router get() = cicerone.router
    val navigatorHolder get() = cicerone.getNavigatorHolder()

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@App)
            modules(listOf(AppModule, NavigationModule))
        }
    }
}
package com.artybyte.test.whacamole.di

import com.artybyte.test.whacamole.App
import com.github.terrakok.cicerone.NavigatorHolder
import com.github.terrakok.cicerone.Router
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

val NavigationModule = module {
    single<Router> { (androidApplication() as App).router }
    single<NavigatorHolder> { (androidApplication() as App).navigatorHolder }
}
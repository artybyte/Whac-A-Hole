package com.artybyte.test.whacamole.di

import android.content.Context
import android.content.SharedPreferences
import com.artybyte.test.whacamole.GameState
import com.artybyte.test.whacamole.R
import org.koin.android.ext.koin.androidApplication
import org.koin.core.qualifier.named
import org.koin.dsl.module

val AppModule = module {
    val GAME_STATE = named("AppGameState")
    val SHARED_PREFS = named("SharedPreferencesProvider")
    val SHARED_PREFS_NAME = named("SharedPreferencesName")
    val MainMenuScreen = named("MainMenuScreen")

    single { GameState(get(SHARED_PREFS_NAME), get(SHARED_PREFS)) }

    single(SHARED_PREFS_NAME) { androidApplication().getString(R.string.shared_preferences_name) }

    single<SharedPreferences>(SHARED_PREFS) { androidApplication().getSharedPreferences(get(SHARED_PREFS_NAME),
        Context.MODE_PRIVATE) }

}
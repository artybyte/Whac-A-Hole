package com.artybyte.test.whacamole

import android.annotation.SuppressLint
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.artybyte.test.whacamole.observe.PreferencesLoadedEventObserver
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity(), PreferencesLoadedEventObserver {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val gameState by inject<GameState>()
        gameState.preferencesLoadedEventObservers.add(this)
    }

    override fun preferencesLoadedEventUpdate() {
        startActivity(
            Intent(this, MainActivity::class.java)
        )
    }
}
package com.artybyte.test.whacamole

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.artybyte.test.whacamole.databinding.MainActivityBinding
import com.artybyte.test.whacamole.ui.bootscreen.BootscreenFragment
import com.artybyte.test.whacamole.ui.common.AppScreens
import com.artybyte.test.whacamole.ui.main.MainFragment
import com.github.terrakok.cicerone.Command
import com.github.terrakok.cicerone.Navigator
import com.github.terrakok.cicerone.NavigatorHolder
import com.github.terrakok.cicerone.Router
import com.github.terrakok.cicerone.androidx.AppNavigator
import org.koin.android.ext.android.inject

class MainActivity : AppCompatActivity() {

    private val navigator: Navigator = AppNavigator(this, R.id.main_fragment)
    lateinit var mainActivityBinding: MainActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainActivityBinding = MainActivityBinding.inflate(layoutInflater)
        val view = mainActivityBinding.root
        setContentView(view)

        inject<Router>().value.navigateTo(AppScreens.Screens.boot())
    }
    fun executeCommands(commands: Array<out Command>) {
        navigator.applyCommands(commands)
    }

    override fun onResumeFragments() {
        super.onResumeFragments()
        inject<NavigatorHolder>().value.setNavigator(navigator)
    }

    override fun onPause() {
        inject<NavigatorHolder>().value.removeNavigator()
        super.onPause()
    }
}
package com.artybyte.test.whacamole

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.FragmentTransaction.TRANSIT_FRAGMENT_FADE
import com.artybyte.test.whacamole.databinding.MainActivityBinding
import com.artybyte.test.whacamole.ui.bootscreen.BootscreenFragment
import com.artybyte.test.whacamole.ui.common.AppScreens
import com.artybyte.test.whacamole.ui.main.MainFragment
import com.github.terrakok.cicerone.Command
import com.github.terrakok.cicerone.Navigator
import com.github.terrakok.cicerone.NavigatorHolder
import com.github.terrakok.cicerone.Router
import com.github.terrakok.cicerone.androidx.AppNavigator
import com.github.terrakok.cicerone.androidx.FragmentScreen
import org.koin.android.ext.android.inject
import kotlin.system.exitProcess

class MainActivity : AppCompatActivity() {
    override fun onDestroy() {
        super.onDestroy()
        moveTaskToBack(true)
        exitProcess(-1)
    }

    private val navigator = object : AppNavigator(this, R.id.container) {
        override fun setupFragmentTransaction(
            screen: FragmentScreen,
            fragmentTransaction: FragmentTransaction,
            currentFragment: Fragment?,
            nextFragment: Fragment
        ) {
            fragmentTransaction.setTransition(
                TRANSIT_FRAGMENT_FADE
            )
        }
    }
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
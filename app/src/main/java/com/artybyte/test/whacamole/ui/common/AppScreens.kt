package com.artybyte.test.whacamole.ui.common

import com.artybyte.test.whacamole.ui.bootscreen.BootscreenFragment
import com.artybyte.test.whacamole.ui.main.MainFragment
import com.github.terrakok.cicerone.androidx.FragmentScreen

class AppScreens {
    object Screens {
        fun boot() = FragmentScreen { ""; BootscreenFragment() }
        fun main() = FragmentScreen { "Menu"; MainFragment() }
    }
}
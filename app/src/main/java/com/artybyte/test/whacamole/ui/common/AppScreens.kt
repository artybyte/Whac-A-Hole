package com.artybyte.test.whacamole.ui.common

import com.artybyte.test.whacamole.ui.main.MainFragment
import com.github.terrakok.cicerone.androidx.FragmentScreen

class AppScreens {
    object Screens {
        fun main() = FragmentScreen { "Menu"; MainFragment() }
    }
}
package com.artybyte.test.whacamole.ui.common

import com.artybyte.test.whacamole.ui.bootscreen.BootscreenFragment
import com.artybyte.test.whacamole.ui.gamescreen.GameScreenFragment
import com.artybyte.test.whacamole.ui.main.MainFragment
import com.artybyte.test.whacamole.ui.timeoutscreen.TimeoutScreenFragment
import com.github.terrakok.cicerone.androidx.FragmentScreen

class AppScreens {
    object Screens {
        fun boot() = FragmentScreen { ""; BootscreenFragment() }
        fun menu() = FragmentScreen { "Menu"; MainFragment() }
        fun game() = FragmentScreen { "Menu"; GameScreenFragment() }
        fun timeout() = FragmentScreen { "Menu"; TimeoutScreenFragment() }
    }
}
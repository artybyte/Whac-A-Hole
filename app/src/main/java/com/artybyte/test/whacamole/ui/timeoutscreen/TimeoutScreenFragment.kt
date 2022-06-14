package com.artybyte.test.whacamole.ui.timeoutscreen

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.artybyte.test.whacamole.GameState
import com.artybyte.test.whacamole.R
import com.artybyte.test.whacamole.databinding.GameScreenFragmentBinding
import com.artybyte.test.whacamole.databinding.MainFragmentBinding
import com.artybyte.test.whacamole.databinding.TimeoutScreenFragmentBinding
import com.artybyte.test.whacamole.ui.common.AppScreens
import com.github.terrakok.cicerone.Router
import org.koin.android.ext.android.inject

class TimeoutScreenFragment : Fragment() {

    companion object {
        fun newInstance() = TimeoutScreenFragment()
    }

    private lateinit var viewModel: TimeoutScreenViewModel
    private lateinit var binding: TimeoutScreenFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = TimeoutScreenFragmentBinding.inflate(layoutInflater)

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(TimeoutScreenViewModel::class.java)

        binding.startGameButton.setOnClickListener {
            inject<Router>().value.newRootScreen(AppScreens.Screens.game())
        }

        binding.goToMenuButton.setOnClickListener {
            inject<Router>().value.newRootScreen(AppScreens.Screens.menu())
        }

        val state by inject<GameState>()

        if (state.getGameScore() > state.getGameRecordScore()){
            binding.recordHitLabel.visibility = View.VISIBLE
            state.setGameRecordScore(state.getGameScore())
            state.writeAndSaveGameRecord()
        }

        requireActivity().runOnUiThread {
            binding.currentLabel.text =
                getString(R.string.main_menu_game_current_score_lbl) + " " + state.getGameScore()

            binding.recordLabel.text =
                getString(R.string.main_menu_game_score_lbl) + " " + state.getGameRecordScore()
        }
    }
}
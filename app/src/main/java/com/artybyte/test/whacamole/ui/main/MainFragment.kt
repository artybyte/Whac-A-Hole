package com.artybyte.test.whacamole.ui.main

import android.annotation.SuppressLint
import android.media.MediaPlayer
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import com.artybyte.test.whacamole.R
import com.artybyte.test.whacamole.databinding.MainFragmentBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.lang.Exception
import android.view.animation.ScaleAnimation
import com.artybyte.test.whacamole.GameState
import com.artybyte.test.whacamole.ui.common.AppScreens
import com.github.terrakok.cicerone.Router
import org.koin.android.ext.android.inject


class MainFragment : Fragment() {

    companion object {
        fun newInstance(): MainFragment {return MainFragment()}
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    private lateinit var viewModel: MainViewModel
    private lateinit var binding: MainFragmentBinding
    var mainMenuMusic = MediaPlayer()

    private var mainMenuPrepared = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = MainFragmentBinding.inflate(layoutInflater)

        return binding.root
    }

    fun scaleView(v: View, startScale: Float, endScale: Float) {
        val anim: Animation = ScaleAnimation(
            startScale, endScale,
            startScale, endScale,
            Animation.RELATIVE_TO_SELF, 0.5f,
            Animation.RELATIVE_TO_SELF, 0.5f
        )
        anim.fillAfter = true
        anim.duration = 1500
        v.startAnimation(anim)
    }

    private suspend fun logoAnimCycle(loop: Boolean){
        scaleView(binding.gameLabel, 1.15f, 0.85f)
        delay(1500L)
        scaleView(binding.gameLabel, 0.85f,1.15f)
        delay(1500L)
        if (loop) logoAnimCycle(loop)
    }

    @SuppressLint("SetTextI18n")
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        binding.startGameButton.setOnClickListener {
            startGame()
        }

        val state by inject<GameState>()
        binding.recordLabel.text = getString(R.string.main_menu_game_score_lbl) +
                " " + state.getGameRecordScore()

        prepareMainMenu()
    }

    private fun prepareMainMenu(){
        if (mainMenuPrepared) return;
        mainMenuPrepared = true

        try {
            mainMenuMusic = MediaPlayer.create(this.context,
                R.raw.whacamole_main_menu)
            mainMenuMusic.setOnCompletionListener {
                mainMenuMusic.seekTo(0)
                mainMenuMusic.start()
            }

            mainMenuMusic.start()

        } catch (e: Exception) { }

        CoroutineScope(Dispatchers.Default).launch {
            logoAnimCycle(true)
        }

    }

    override fun onPause() {
        super.onPause()
        mainMenuMusic.pause()
    }

    override fun onResume() {
        super.onResume()

        mainMenuMusic.seekTo(0)
        mainMenuMusic.start()
    }

    private fun startGame(){
        inject<Router>().value.newRootScreen(AppScreens.Screens.game())
    }
}
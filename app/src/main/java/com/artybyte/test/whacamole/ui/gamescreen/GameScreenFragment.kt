package com.artybyte.test.whacamole.ui.gamescreen

import android.media.MediaPlayer
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.animation.ScaleAnimation
import android.widget.ImageView
import com.artybyte.test.whacamole.R
import com.artybyte.test.whacamole.databinding.GameScreenFragmentBinding
import com.artybyte.test.whacamole.ui.common.AppScreens
import com.github.terrakok.cicerone.Router
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import java.lang.Exception

class GameScreenFragment : Fragment() {

    companion object {
        fun newInstance() = GameScreenFragment()
    }

    private lateinit var viewModel: GameScreenViewModel
    private lateinit var binding: GameScreenFragmentBinding
    private lateinit var holes: ArrayList<ImageView>
    var gameBgMusic = MediaPlayer()
    var gameHoleTapSound = MediaPlayer()
    private var elapsedGameTime = 300 // 30 secs * (1000\100)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = GameScreenFragmentBinding.inflate(layoutInflater)

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
        anim.duration = 700
        v.startAnimation(anim)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(GameScreenViewModel::class.java)

        holes = arrayListOf(
            binding.hole0,
            binding.hole1,
            binding.hole2,
            binding.hole3,
            binding.hole4,
            binding.hole5,
            binding.hole6,
            binding.hole7,
            binding.hole8
        )

        for (i in 0..8){
            holes[i].setOnClickListener {
                onHoleTapped(i, holes[i])
            }
        }

        onGameStarted()
    }

    private fun onTimerElapsed(){
        inject<Router>().value.newRootScreen(AppScreens.Screens.timeout())
    }

    private fun onHoleTapped(holeID: Int, hole: ImageView){
        gameHoleTapSound.seekTo(0)
        gameHoleTapSound.start()

        scaleView(hole, 0.88f, 1.0f)
    }

    private suspend fun gameTimer(){
        delay(100L)
        elapsedGameTime--

        requireActivity().runOnUiThread {
            binding.gameTimerLabel.text = ""+(elapsedGameTime.toDouble() / 10)
        }
        if (elapsedGameTime > 0) gameTimer() else onTimerElapsed()
    }

    private fun onGameStarted(){
        try {
            gameBgMusic = MediaPlayer.create(this.context,
                R.raw.whacamole_game_sound_bg)

            gameHoleTapSound = MediaPlayer.create(this.context,
                R.raw.empty_hole_tapped)

            gameBgMusic.setOnCompletionListener {
                gameBgMusic.seekTo(0)
                gameBgMusic.start()
            }
            gameBgMusic.start()
        } catch (e: Exception) { }

        CoroutineScope(Dispatchers.Default).launch {
            gameTimer()
        }
    }

    override fun onPause() {
        super.onPause()
        gameBgMusic.pause()
    }

    override fun onResume() {
        super.onResume()

        gameBgMusic.seekTo(0)
        gameBgMusic.start()
    }

}
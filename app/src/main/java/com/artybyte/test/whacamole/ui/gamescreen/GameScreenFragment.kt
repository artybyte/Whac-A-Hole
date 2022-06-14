package com.artybyte.test.whacamole.ui.gamescreen

import android.content.Context
import android.media.MediaPlayer
import android.os.Build
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.animation.ScaleAnimation
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.core.view.ViewGroupCompat
import androidx.core.view.marginStart
import com.artybyte.test.whacamole.GameState
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
    private lateinit var moles: ArrayList<ImageView>

    var gameBgMusic = MediaPlayer()

    var gameHoleTapSound = MediaPlayer()
    var onMoleTapped = MediaPlayer()
    var onMoleShow = MediaPlayer()
    var onMoleHide = MediaPlayer()

    private var elapsedGameTime = 300 // 30 secs * (1000\100)
    private var latestMoleID = -1
    private var canTapOnMole = true
    private var paused = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = GameScreenFragmentBinding.inflate(layoutInflater)

        return binding.root
    }

    fun scaleView(v: View, startScale: Float, endScale: Float,
                  animComplete: () -> Unit,
                  pivotX: Float=0.5f, pivotY: Float=0.5f,
                  duration: Int=700
                  ) {

        val anim: Animation = ScaleAnimation(
            startScale, endScale,
            startScale, endScale,
            Animation.RELATIVE_TO_SELF, pivotX,
            Animation.RELATIVE_TO_SELF, pivotY
        )
        anim.fillAfter = true
        anim.duration = (duration.toLong())

        CoroutineScope(Dispatchers.Default).launch {
            if (isAdded)
            requireActivity().runOnUiThread {
                v.startAnimation(anim)
            }
            delay((duration.toLong()))

            if (isAdded)
            requireActivity().runOnUiThread {
                animComplete()
            }
        }
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

        moles = arrayListOf(
            binding.gameMole0,
            binding.gameMole1,
            binding.gameMole2,
            binding.gameMole3,
            binding.gameMole4,
            binding.gameMole5,
            binding.gameMole6,
            binding.gameMole7,
            binding.gameMole8
        )

        for (i in 0..8){
            holes[i].setOnClickListener {
                onHoleTapped(i, holes[i])
            }
        }

        for (i in 0..8){
            moles[i].setOnClickListener {
                onMoleTapped(i, moles[i])
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

        if (holeID != latestMoleID) scaleView(hole, 0.88f, 1.0f, {})
    }

    private fun onMoleTapped(moleID: Int, mole: ImageView){
        if (!canTapOnMole) return;
        canTapOnMole = false

        if (mole.visibility == View.INVISIBLE) return;

        if (moleID == latestMoleID){
            vibratePhone()

            onMoleTapped.seekTo(0)
            onMoleTapped.start()

            val state by inject<GameState>()

            state.setGameScore(
                state.getGameScore() + 1
            )

            scaleView(mole, 1.0f, 0.0f, {
                mole.visibility = View.INVISIBLE
            }, 0.5f, 0.9f)
        }
    }
    private fun vibratePhone(){
        val vibrator = context?.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        if (Build.VERSION.SDK_INT >= 26) {
            vibrator.vibrate(VibrationEffect.createOneShot(100, VibrationEffect.DEFAULT_AMPLITUDE))
        } else {
            vibrator.vibrate(100)
        }
    }
    private suspend fun gameTimer(){
        if (!isAdded) return;
        delay(100L)

        if (!paused) {
            elapsedGameTime--

            val state by inject<GameState>()

            requireActivity().runOnUiThread {
                binding.gameTimerLabel.text = "${(elapsedGameTime.toDouble() / 10)}"
                binding.gameScoreLabel.text = "${state.getGameScore()}"
            }

            if (elapsedGameTime == 50) binding.gameTimerLabel.setTextColor(
                ContextCompat.getColor(
                    this.requireContext(), R.color.red
                )
            )
        }
        if (elapsedGameTime > 0) gameTimer() else onTimerElapsed()
    }

    private suspend fun placeMoleToRandomHole(){
        if (!paused) {
            val holeID = (0..8).random()
            latestMoleID = holeID

            val currentMole = moles[holeID]

            canTapOnMole = true

            if (isAdded)
                requireActivity().runOnUiThread {
                    scaleView(currentMole, 0.01f, 1.0f, {}, 0.5f, 0.9f, 100)
                    currentMole.visibility = View.VISIBLE
                    onMoleShow.seekTo(0)
                    onMoleShow.start()
                }

            delay(500L)

            if (isAdded)
                requireActivity().runOnUiThread {
                    scaleView(currentMole, 1.0f, 0.0f, {
                        onMoleHide.seekTo(0)
                        onMoleHide.start()
                        currentMole.visibility = View.INVISIBLE
                    }, 0.5f, 0.9f, 100)
                }
        }
        delay(150L)

        if (isAdded) placeMoleToRandomHole()
    }

    private fun onGameStarted(){
        val state by inject<GameState>()
        state.setGameScore(0)

        try {
            gameBgMusic = MediaPlayer.create(this.context,
                R.raw.whacamole_game_sound_bg)

            onMoleTapped = MediaPlayer.create(this.context,
                R.raw.mole_tapped)

            onMoleShow = MediaPlayer.create(this.context,
                R.raw.mole_show)

            onMoleHide = MediaPlayer.create(this.context,
                R.raw.mole_hide)

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

        CoroutineScope(Dispatchers.Default).launch {
            placeMoleToRandomHole()
        }
    }

    override fun onPause() {
        super.onPause()

        gameBgMusic.pause()
        paused = true

        onMoleHide.seekTo(0)
        onMoleHide.pause()
        onMoleShow.seekTo(0)
        onMoleShow.pause()
        onMoleTapped.seekTo(0)
        onMoleTapped.pause()
    }

    override fun onResume() {
        super.onResume()

        gameBgMusic.seekTo(0)
        gameBgMusic.start()

        paused = false

        val state by inject<GameState>()
        binding.gameTimerLabel.text = "${(elapsedGameTime.toDouble() / 10)}"
        binding.gameScoreLabel.text = "${state.getGameScore()}"
    }

}
package com.artybyte.test.whacamole.ui.bootscreen

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import com.artybyte.test.whacamole.R
import com.artybyte.test.whacamole.databinding.BootscreenFragmentBinding
import com.artybyte.test.whacamole.databinding.MainFragmentBinding
import com.artybyte.test.whacamole.ui.common.AppScreens
import com.artybyte.test.whacamole.ui.main.MainFragment
import com.artybyte.test.whacamole.ui.main.MainViewModel
import com.github.terrakok.cicerone.Router
import kotlinx.coroutines.*
import org.koin.android.ext.android.inject

class BootscreenFragment : Fragment() {
    companion object {
        fun newInstance(): BootscreenFragment { return BootscreenFragment() }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    private lateinit var viewModel: MainViewModel
    private lateinit var binding: BootscreenFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = BootscreenFragmentBinding.inflate(layoutInflater)
        return binding.root
    }

    private fun goToMenu(){
        inject<Router>().value.newRootScreen(AppScreens.Screens.menu())
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        // TODO: Use the ViewModel

        binding.main.setOnClickListener {
            goToMenu()
        }

        val appearAnim = AnimationUtils.loadAnimation(this.requireContext(), R.anim.anim_appear)
        val disappearAnim = AnimationUtils.loadAnimation(this.requireContext(), R.anim.anim_disappear)

        // boot screen animation sequence
        CoroutineScope(Dispatchers.Default).launch {
            if (!isAdded) cancel()

            this@BootscreenFragment.requireActivity().runOnUiThread {
                binding.imageView.startAnimation(appearAnim)
                binding.textFor.startAnimation(appearAnim)
            }
            delay(3200L)
            if (isAdded)
            this@BootscreenFragment.requireActivity().runOnUiThread {
                binding.imageView.startAnimation(disappearAnim)
                binding.textFor.startAnimation(disappearAnim)
            }
            delay(2000L)
            if (isAdded)
            this@BootscreenFragment.requireActivity().runOnUiThread {
                binding.textFor.visibility = View.GONE

                binding.imageView.setImageResource(R.drawable.ic_whackamole)
                binding.imageView.startAnimation(appearAnim)

                binding.textGameName.visibility = View.VISIBLE
                binding.textGameName.startAnimation(appearAnim)
            }
            delay(4000L)
            if (isAdded)
            this@BootscreenFragment.requireActivity().runOnUiThread {
                binding.imageView.startAnimation(disappearAnim)
                binding.textGameName.startAnimation(disappearAnim)
            }
            delay(2000L)
            if (isAdded)
            goToMenu()
        }
    }
}
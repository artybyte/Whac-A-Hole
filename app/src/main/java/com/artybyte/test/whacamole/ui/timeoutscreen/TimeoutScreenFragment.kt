package com.artybyte.test.whacamole.ui.timeoutscreen

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.artybyte.test.whacamole.R
import com.artybyte.test.whacamole.databinding.GameScreenFragmentBinding
import com.artybyte.test.whacamole.databinding.MainFragmentBinding
import com.artybyte.test.whacamole.databinding.TimeoutScreenFragmentBinding

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
        // TODO: Use the ViewModel
    }

}
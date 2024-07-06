package com.example.rango.screen.greetings

import android.view.LayoutInflater
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.rango.R
import com.example.rango.base.BaseFragment
import com.example.rango.databinding.FragmentGreetingsBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GreetingsFragment : BaseFragment<FragmentGreetingsBinding>() {

    override val bindingInflater: (LayoutInflater) -> FragmentGreetingsBinding =
        FragmentGreetingsBinding::inflate
    override val viewModel: GreetingsViewModel by viewModels()

    override fun initViews() {
        viewModel.disableGreetings()

        binding.cvGetStarted.setOnClickListener {
            findNavController().navigate(R.id.credentialFragment)
        }
    }

    override fun initObservers() {}


}
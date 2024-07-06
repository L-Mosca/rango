package com.example.rango.screen.splash

import android.view.LayoutInflater
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.rango.R
import com.example.rango.base.BaseFragment
import com.example.rango.databinding.FragmentSplashBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SplashFragment : BaseFragment<FragmentSplashBinding>() {
    override val bindingInflater: (LayoutInflater) -> FragmentSplashBinding =
        FragmentSplashBinding::inflate
    override val viewModel: SplashViewModel by viewModels()

    override fun initViews() {
        findNavController().popBackStack(R.id.splashFragment, true)
        viewModel.loadSplashData()
    }

    override fun initObservers() {
        viewModel.showHomeScreen.observe(viewLifecycleOwner) {
            findNavController().navigate(R.id.homeFragment)
        }

        viewModel.showCredentialScreen.observe(viewLifecycleOwner) {
            findNavController().navigate(R.id.credentialFragment)
        }

        viewModel.showGreetingsScreen.observe(viewLifecycleOwner) {
            findNavController().navigate(R.id.greetingsFragment)
        }
    }
}
package com.example.rango.screen.greetings

import android.view.LayoutInflater
import androidx.fragment.app.viewModels
import com.example.rango.base.BaseFragment
import com.example.rango.databinding.FragmentGreetingsBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GreetingsFragment : BaseFragment<FragmentGreetingsBinding>() {

    override val bindingInflater: (LayoutInflater) -> FragmentGreetingsBinding =
        FragmentGreetingsBinding::inflate
    override val viewModel: GreetingsViewModel by viewModels()

    override fun initViews() {}

    override fun initObservers() {}


}
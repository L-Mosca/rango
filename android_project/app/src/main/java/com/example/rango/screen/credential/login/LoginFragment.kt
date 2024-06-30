package com.example.rango.screen.credential.login

import android.view.LayoutInflater
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import com.example.rango.R
import com.example.rango.base.BaseFragment
import com.example.rango.databinding.FragmentLoginBinding
import com.example.rango.screen.credential.CredentialFragmentDirections
import com.example.rango.utils.navigate
import com.example.rango.utils.playGifAnimation
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : BaseFragment<FragmentLoginBinding>() {
    override val bindingInflater: (LayoutInflater) -> FragmentLoginBinding =
        FragmentLoginBinding::inflate
    override val viewModel: LoginViewModel by viewModels()

    var showLoading: ((Boolean) -> Unit)? = null

    override fun initViews() {
        playGifAnimation(binding.ivLoginGif)
        setupEditTexts()
        binding.apply {
            btLogin.setOnClickListener {
                viewModel.signIn(etLoginEmail.text.toString(), etLoginPassword.text.toString())
            }
        }
    }

    override fun initObservers() {
        viewModel.loginSuccess.observe(viewLifecycleOwner) { userData ->
            val direction =
                CredentialFragmentDirections.actionCredentialFragmentToHomeFragment(userData)
            navigate(direction)
        }

        viewModel.loginError.observe(viewLifecycleOwner) {
            showShortSnackBar(getString(R.string.login_error))
        }

        viewModel.emailError.observe(viewLifecycleOwner) { errorMessage ->
            binding.tilLoginEmail.error = errorMessage
        }

        viewModel.passwordError.observe(viewLifecycleOwner) { errorMessage ->
            binding.tilLoginPassword.error = errorMessage
        }

        viewModel.loading.observe(viewLifecycleOwner) { isLoading ->
            showLoading?.invoke(isLoading)
        }
    }

    private fun setupEditTexts() {
        with(binding) {
            etLoginEmail.addTextChangedListener {
                tilLoginEmail.error = null
            }
            etLoginPassword.addTextChangedListener {
                tilLoginPassword.error = null
            }
            etLoginPassword.setOnEditorActionListener { v, actionId, _ ->
                viewModel.handleImeAction(etLoginEmail.text.toString(), v.text.toString(), actionId)
                false
            }
        }
    }
}
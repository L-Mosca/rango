package com.example.rango.screen.credential.register

import android.view.LayoutInflater
import android.view.inputmethod.EditorInfo
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import com.example.rango.R
import com.example.rango.base.BaseFragment
import com.example.rango.databinding.FragmentRegisterBinding
import com.example.rango.screen.credential.CredentialFragmentDirections
import com.example.rango.utils.hideKeyboard
import com.example.rango.utils.navigate
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegisterFragment : BaseFragment<FragmentRegisterBinding>() {
    override val bindingInflater: (LayoutInflater) -> FragmentRegisterBinding =
        FragmentRegisterBinding::inflate
    override val viewModel: RegisterViewModel by viewModels()

    var showLoading: ((Boolean) -> Unit)? = null

    override fun initViews() {
        setupEditTexts()
        binding.btRegister.setOnClickListener { signUp() }
    }

    override fun initObservers() {
        viewModel.loading.observe(viewLifecycleOwner) { isLoading ->
            showLoading?.invoke(isLoading)
        }

        viewModel.registerError.observe(viewLifecycleOwner) {
            showShortSnackBar(getString(R.string.register_error))
        }

        viewModel.loading.observe(viewLifecycleOwner) {

        }

        viewModel.registerSuccess.observe(viewLifecycleOwner) { userData ->
            val direction =
                CredentialFragmentDirections.actionCredentialFragmentToHomeFragment(user = userData)
            navigate(direction)
        }

        viewModel.nameError.observe(viewLifecycleOwner) { errorMessage ->
            binding.tilRegisterName.error = errorMessage
        }

        viewModel.emailError.observe(viewLifecycleOwner) { errorMessage ->
            binding.tilRegisterEmail.error = errorMessage
        }

        viewModel.passwordError.observe(viewLifecycleOwner) { errorMessage ->
            binding.tilRegisterPassword.error = errorMessage
        }

        viewModel.confirmPasswordError.observe(viewLifecycleOwner) { errorMessage ->
            binding.tilRegisterConfirmPassword.error = errorMessage
        }

        viewModel.differentPasswordsError.observe(viewLifecycleOwner) { errorMessage ->
            binding.tilRegisterConfirmPassword.error = errorMessage
        }

        viewModel.passwordLength.observe(viewLifecycleOwner) {
            binding.tvMinimumCharacters.setTextColor(ContextCompat.getColor(requireContext(), it))
        }

        viewModel.passwordUpperLetter.observe(viewLifecycleOwner) {
            binding.tvUpperLetter.setTextColor(ContextCompat.getColor(requireContext(), it))
        }

        viewModel.passwordLowerLetter.observe(viewLifecycleOwner) {
            binding.tvLowerLetter.setTextColor(ContextCompat.getColor(requireContext(), it))
        }

        viewModel.passwordNumber.observe(viewLifecycleOwner) {
            binding.tvNumber.setTextColor(ContextCompat.getColor(requireContext(), it))
        }
    }

    private fun setupEditTexts() {
        with(binding) {
            etRegisterName.addTextChangedListener {
                binding.tilRegisterName.error = null
            }
            etRegisterEmail.addTextChangedListener {
                binding.tilRegisterEmail.error = null
            }
            etRegisterPassword.addTextChangedListener {
                binding.tilRegisterPassword.error = null
                viewModel.validatePasswordRequirements(it.toString())
            }
            etRegisterConfirmPassword.addTextChangedListener {
                binding.tilRegisterConfirmPassword.error = null
            }
            etRegisterConfirmPassword.setOnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_SEND) signUp()
                false
            }
        }
    }

    private fun signUp() {
        binding.apply {
            val name = etRegisterName.text.toString()
            val email = etRegisterEmail.text.toString()
            val password = etRegisterPassword.text.toString()
            val confirmPassword = etRegisterConfirmPassword.text.toString()

            viewModel.signUp(name, email, password, confirmPassword)
            hideKeyboard()
        }
    }
}
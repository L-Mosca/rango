package com.example.rango.screen.credential

import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.viewpager2.widget.ViewPager2
import com.example.rango.base.BaseFragment
import com.example.rango.databinding.FragmentCredentialBinding
import com.example.rango.domain.models.register.CredentialViewPagerConfig
import com.example.rango.domain.models.register.buildByPosition
import com.example.rango.screen.credential.login.LoginFragment
import com.example.rango.screen.credential.register.RegisterFragment
import com.example.rango.utils.hideKeyboard
import com.example.rango.utils.onBackPressed
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CredentialFragment : BaseFragment<FragmentCredentialBinding>() {
    override val bindingInflater: (LayoutInflater) -> FragmentCredentialBinding =
        FragmentCredentialBinding::inflate
    override val viewModel: CredentialViewModel by viewModels()

    override fun initViews() {
        setupViewPager()
        binding.tvCredentialRegisterButton.setOnClickListener {
            viewModel.handleRegisterClick(binding.vpCredential.currentItem)
            hideKeyboard()
        }
        onBackPressed {
            requireActivity().moveTaskToBack(true)
        }
    }

    override fun initObservers() {
        viewModel.switchViewPager.observe(viewLifecycleOwner) { newPosition ->
            binding.vpCredential.setCurrentItem(newPosition, true)
        }
    }

    private fun setupViewPager() {
        val fragments = listOf<Fragment>(LoginFragment().apply {
            showLoading = { isLoading ->
                loading(isLoading)

            }
        }, RegisterFragment().apply {
            showLoading = { isLoading ->
                loading(isLoading)
            }
        })
        val viewPagerAdapter = CredentialAdapter(childFragmentManager, lifecycle, fragments)
        binding.vpCredential.apply {
            adapter = viewPagerAdapter
            offscreenPageLimit = 2
            isSaveEnabled = false
            isUserInputEnabled = true

            registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    updateLayout(position.buildByPosition())
                }
            })
        }
    }

    private fun loading(isLoading: Boolean) {
        showLoading(binding.includeRegisterLoading, null, isLoading)
    }

    private fun updateLayout(data: CredentialViewPagerConfig) {
        with(binding) {
            tvCredentialViewPagerTitle.text = getString(data.title)
            tvCredentialDontHaveAccount.text = getString(data.actionDescription)
            tvCredentialRegisterButton.text = getString(data.action)
        }
    }
}
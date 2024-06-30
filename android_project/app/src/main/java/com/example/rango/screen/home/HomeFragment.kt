package com.example.rango.screen.home

import android.view.LayoutInflater
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import androidx.viewpager2.widget.ViewPager2.OFFSCREEN_PAGE_LIMIT_DEFAULT
import com.example.rango.R
import com.example.rango.base.BaseFragment
import com.example.rango.databinding.FragmentHomeBinding
import com.example.rango.domain.models.home.getHomeTitleByPosition
import com.example.rango.screen.home.menu.MenuFragment
import com.example.rango.screen.home.order.OrderFragment
import com.example.rango.screen.home.table.TableFragment
import com.example.rango.utils.onBackPressed
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>() {
    override val bindingInflater: (LayoutInflater) -> FragmentHomeBinding =
        FragmentHomeBinding::inflate
    override val viewModel: HomeViewModel by viewModels()

    override fun initViews() {
        setupBackPressed()
        setupViewPager()
        setupToolbar()
        setupMenu()
    }

    override fun initObservers() {
        viewModel.logoutSuccess.observe(viewLifecycleOwner) {
            findNavController().navigate(R.id.credentialFragment)
        }

        viewModel.slideUpAnimation.observe(viewLifecycleOwner) { animation ->
            binding.includeHomeMenu.clHomeMenu.startAnimation(animation)
            binding.includeHomeMenu.clHomeMenu.visibility = View.INVISIBLE
        }

        viewModel.slideDownAnimation.observe(viewLifecycleOwner) { animation ->
            binding.includeHomeMenu.clHomeMenu.startAnimation(animation)
            binding.includeHomeMenu.clHomeMenu.visibility = View.VISIBLE
        }

        viewModel.hideMenu.observe(viewLifecycleOwner) {
            binding.includeHomeMenu.clHomeMenu.visibility = View.INVISIBLE
        }
    }

    private fun setupBackPressed() {
        onBackPressed {
            requireActivity().moveTaskToBack(true)
        }
    }

    private fun setupViewPager() {
        val menuFragment = MenuFragment().apply { hideMenu = { showMenu(true) } }
        val orderFragment = OrderFragment().apply { hideMenu = { showMenu(true) } }
        val tableFragment = TableFragment().apply { hideMenu = { showMenu(true) } }
        val fragments = listOf<Fragment>(menuFragment, orderFragment, tableFragment)
        val viewPagerAdapter = HomeAdapter(childFragmentManager, lifecycle, fragments)
        binding.vpHome.apply {
            adapter = viewPagerAdapter
            offscreenPageLimit = OFFSCREEN_PAGE_LIMIT_DEFAULT
            isSaveEnabled = false
            isUserInputEnabled = false

            registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    binding.includeToolbar.tvToolBarTitle.text =
                        getString(position.getHomeTitleByPosition())
                }
            })
        }
    }

    private fun setupToolbar() {
        with(binding.includeToolbar) {
            appBarHome.setOnClickListener { showMenu(true) }
            ivToolbarMenu.setOnClickListener {
                binding.includeHomeMenu.apply {
                    showMenu(false)
                }
            }
        }
    }

    private fun setupMenu() {
        with(binding.includeHomeMenu) {
            vFork.setOnClickListener {
                switchPage(HomeAdapter.MENU_PAGE)
                showMenu(true)
            }
            vOrder.setOnClickListener {
                switchPage(HomeAdapter.ORDER_PAGE)
                showMenu(true)
            }
            vTable.setOnClickListener {
                switchPage(HomeAdapter.TABLE_PAGE)
                showMenu(true)
            }
            vLogout.setOnClickListener {
                viewModel.logout()
            }
        }
    }

    private fun showMenu(forceToClose: Boolean) {
        viewModel.setListAnimation(
            binding.includeHomeMenu.clHomeMenu.isVisible,
            requireContext(),
            forceToClose
        )
    }


    private fun switchPage(newPage: Int) = binding.vpHome.setCurrentItem(newPage, true)

}
package com.example.rango.screen.home.menu

import android.view.LayoutInflater
import android.view.View
import android.view.animation.AnimationUtils
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.viewpager2.widget.ViewPager2
import com.example.rango.R
import com.example.rango.base.BaseFragment
import com.example.rango.databinding.FragmentMenuBinding
import com.example.rango.domain.models.menu.MenuCategory
import com.example.rango.screen.home.menu.dishelist.DisheListFragment
import com.example.rango.utils.LoadingStyle
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MenuFragment : BaseFragment<FragmentMenuBinding>() {
    override val bindingInflater: (LayoutInflater) -> FragmentMenuBinding =
        FragmentMenuBinding::inflate
    override val viewModel: MenuViewModel by viewModels()

    var hideMenu: (() -> Unit)? = null

    override fun initViews() {
        binding.includeMenuTab.hsvMenu.visibility = View.INVISIBLE
        viewModel.fetchCategories()
        binding.clMenu.setOnClickListener { hideMenu?.invoke() }
    }

    override fun initObservers() {
        viewModel.categories.observe(viewLifecycleOwner) { categoryList ->
            setupTabMenu(categoryList)
        }

        viewModel.setupViewPager.observe(viewLifecycleOwner) { data ->
            setupViewPager(data.first, data.second)
        }

        viewModel.viewPagerLoading.observe(viewLifecycleOwner) { showLoading ->
            showLoading(binding.includeMenuLoading, LoadingStyle.LIST, showLoading)
        }
    }

    private fun setupTabMenu(categoryList: List<MenuCategory>) {
        binding.includeMenuTab.tlMenu.invalidate()
        for (tabName in categoryList) {
            val tab = binding.includeMenuTab.tlMenu.newTab()
            tab.text = tabName.name
            tab.id = tabName.id
            binding.includeMenuTab.tlMenu.addTab(tab)
        }
        binding.pbMenu.isVisible = false

        binding.includeMenuTab.hsvMenu.startAnimation(
            AnimationUtils.loadAnimation(
                context,
                R.anim.slide_down
            )
        )
        binding.includeMenuTab.hsvMenu.visibility = View.VISIBLE

        binding.includeMenuTab.tlMenu.addOnTabSelectedListener(object :
            TabLayout.OnTabSelectedListener {
            override fun onTabSelected(p0: TabLayout.Tab?) {}
            override fun onTabUnselected(p0: TabLayout.Tab?) {}
            override fun onTabReselected(p0: TabLayout.Tab?) {}
        })

        viewModel.fetchAllDishes(categoryList)
    }

    private fun setupViewPager(data: List<DisheListFragment>, categoryList: List<MenuCategory>) {
        val viewPagerAdapter = DisheViewPagerAdapter(childFragmentManager, lifecycle, data)
        binding.vpMenuCategory.invalidate()
        binding.vpMenuCategory.apply {
            adapter = viewPagerAdapter
            offscreenPageLimit = 6
            isSaveEnabled = true
            isUserInputEnabled = false

            registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {

                }
            })
        }

        TabLayoutMediator(binding.includeMenuTab.tlMenu, binding.vpMenuCategory) { tab, position ->
            tab.setText(categoryList[position].name)
        }.attach()

        viewModel.changeLoadingVisibility(false)
    }
}
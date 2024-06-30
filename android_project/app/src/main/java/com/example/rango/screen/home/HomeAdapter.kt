package com.example.rango.screen.home

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter

class HomeAdapter(
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle,
    private val fragments: List<Fragment>
) : FragmentStateAdapter(fragmentManager, lifecycle) {

    companion object {
        const val MENU_PAGE = 0
        const val ORDER_PAGE = 1
        const val TABLE_PAGE = 2
    }

    override fun getItemCount() = fragments.size
    override fun createFragment(position: Int) = fragments[position]
}
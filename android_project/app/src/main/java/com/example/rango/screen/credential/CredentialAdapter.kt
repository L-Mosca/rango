package com.example.rango.screen.credential

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter

class CredentialAdapter(
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle,
    private val fragments: List<Fragment>
) : FragmentStateAdapter(fragmentManager, lifecycle) {

    companion object {
        const val SIGN_IN_PAGE = 0
        const val SIGN_UP_PAGE = 1
    }

    override fun getItemCount() = fragments.size
    override fun createFragment(position: Int) = fragments[position]
}
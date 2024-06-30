package com.example.rango.domain.models.home

import androidx.annotation.StringRes
import com.example.rango.R
import com.example.rango.screen.home.HomeAdapter

data class HomeViewPagerConfig(@StringRes val title: Int)

fun Int.getHomeTitleByPosition(): Int {
    return when (this) {
        HomeAdapter.MENU_PAGE -> R.string.menu
        HomeAdapter.ORDER_PAGE -> R.string.orders
        HomeAdapter.TABLE_PAGE -> R.string.tables
        else -> R.string.orders
    }
}
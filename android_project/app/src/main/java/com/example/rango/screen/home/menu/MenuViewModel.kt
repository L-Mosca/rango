package com.example.rango.screen.home.menu

import android.os.Handler
import android.os.Looper
import com.example.rango.base.BaseViewModel
import com.example.rango.base.SingleLiveData
import com.example.rango.domain.models.menu.MenuCategory
import com.example.rango.domain.models.menu.orderByCategory
import com.example.rango.domain.models.order.Order
import com.example.rango.domain.repositories.menu.MenuRepositoryContract
import com.example.rango.domain.repositories.order.OrderRepositoryContract
import com.example.rango.screen.home.menu.dishelist.DisheListFragment
import com.example.rango.utils.AppConstants
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MenuViewModel @Inject constructor(
    private val menuRepository: MenuRepositoryContract,
    private val orderRepository: OrderRepositoryContract
) :
    BaseViewModel() {

    val categories = SingleLiveData<List<MenuCategory>>()
    val setupViewPager = SingleLiveData<Pair<List<DisheListFragment>, List<MenuCategory>>>()
    val viewPagerLoading = SingleLiveData<Boolean>()
    val order = SingleLiveData<Order>()

    fun observeOrder() {
        defaultLaunch {
            orderRepository.observeOrder().collect { data ->
                order.postValue(data ?: Order.buildDefaultOrder())
            }
        }
    }

    fun fetchCategories() {
        Handler(Looper.getMainLooper()).postDelayed({
            defaultLaunch {
                val categories = menuRepository.fetchCategories()
                if (categories.isNotEmpty()) {
                    this.categories.postValue(categories)
                }
            }
        }, 1000)
    }

    fun fetchAllDishes(categoryList: List<MenuCategory>) {
        defaultLaunch {
            viewPagerLoading.postValue(true)
            val allDishes = menuRepository.fetchDisheList()
            val dishesByCategory = allDishes.orderByCategory()

            val categories = AppConstants.CATEGORY_LIST
            val fragments = categories.map { category ->
                val items = dishesByCategory.first { it.category == category }
                DisheListFragment.newInstance(items)
            }

            setupViewPager.postValue(Pair(fragments, categoryList))
        }
    }

    fun changeLoadingVisibility(showLoading: Boolean) {
        viewPagerLoading.postValue(showLoading)
    }
}
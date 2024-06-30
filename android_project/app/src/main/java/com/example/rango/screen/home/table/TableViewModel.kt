package com.example.rango.screen.home.table

import com.example.rango.base.BaseViewModel
import com.example.rango.base.SingleLiveData
import com.example.rango.domain.models.menu.Table
import com.example.rango.domain.models.order.Order
import com.example.rango.domain.repositories.menu.MenuRepositoryContract
import com.example.rango.domain.repositories.order.OrderRepositoryContract
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class TableViewModel @Inject constructor(
    private val menuRepository: MenuRepositoryContract,
    private val orderRepository: OrderRepositoryContract
) :
    BaseViewModel() {

    val tableList = SingleLiveData<List<Table>>()
    val showEmptyPlaceHolder = SingleLiveData<Boolean>()
    val orders = SingleLiveData<List<Order>>()

    fun fetchTables() {
        defaultLaunch {
            val orders = orderRepository.fetchOnlineOrders()
            this.orders.postValue(orders)
            val data = menuRepository.fetchTables()
            if (data.isEmpty()) showEmptyPlaceHolder.postValue(true)
            else tableList.postValue(data)
        }
    }
}
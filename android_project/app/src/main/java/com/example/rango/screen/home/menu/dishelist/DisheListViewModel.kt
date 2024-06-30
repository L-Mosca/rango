package com.example.rango.screen.home.menu.dishelist

import com.example.rango.base.BaseViewModel
import com.example.rango.base.SingleLiveData
import com.example.rango.domain.models.menu.DisheItem
import com.example.rango.domain.models.order.Order
import com.example.rango.domain.repositories.order.OrderRepositoryContract
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DisheListViewModel @Inject constructor(
    private val orderRepository: OrderRepositoryContract
) :
    BaseViewModel() {

    val showAlertDialog = SingleLiveData<Unit>()
    val updateItemInAdapter = SingleLiveData<Pair<Int, Int>>()
    val orderData = SingleLiveData<Order>()

    fun addItemToOrder(item: DisheItem) {
        defaultLaunch {
            orderRepository.addNewItemToOrder(item)
            fetchOrder()
        }
    }

    fun removeItemToOrder(item: DisheItem) {
        defaultLaunch {
            orderRepository.removeItemToOrder(item)
            fetchOrder()
        }
    }

    fun fetchOrder() {
        defaultLaunch {
            val data = orderRepository.fetchOrders()
            data?.let { orderData.postValue(it) }
        }
    }

}
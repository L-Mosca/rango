package com.example.rango.screen.home.table.dialog

import com.example.rango.base.BaseViewModel
import com.example.rango.base.SingleLiveData
import com.example.rango.domain.models.order.OrderList
import com.example.rango.domain.repositories.order.OrderRepositoryContract
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class OrderDetailViewModel @Inject constructor(private val orderRepository: OrderRepositoryContract) :
    BaseViewModel() {

    val closeOrderSuccess = SingleLiveData<Unit>()

    fun closeOrder(data: OrderList) {
        defaultLaunch {
            orderRepository.closeOrder(data)
            closeOrderSuccess.postValue(Unit)
        }
    }
}
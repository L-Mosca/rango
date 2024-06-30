package com.example.rango.screen.home.order

import android.annotation.SuppressLint
import com.example.rango.R
import com.example.rango.base.BaseViewModel
import com.example.rango.base.SingleLiveData
import com.example.rango.domain.models.menu.DisheItem
import com.example.rango.domain.models.menu.Table
import com.example.rango.domain.models.menu.isADefaultTable
import com.example.rango.domain.models.order.Order
import com.example.rango.domain.models.order.isADefaultOrder
import com.example.rango.domain.repositories.menu.MenuRepositoryContract
import com.example.rango.domain.repositories.order.OrderRepositoryContract
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class OrderViewModel @Inject constructor(
    private val orderRepository: OrderRepositoryContract,
    private val menuRepository: MenuRepositoryContract
) :
    BaseViewModel() {

    val showOrderData = SingleLiveData<Order>()
    val showEmptyPlaceHolder = SingleLiveData<Boolean>()
    val tableData = SingleLiveData<Table>()
    val needTable = SingleLiveData<Unit>()
    val updateOrderValue = SingleLiveData<Double>()
    val updateItemInAdapter = SingleLiveData<Pair<Int, Int>>()
    val deleteAdapterItem = SingleLiveData<Int>()
    val showErrorMessage = SingleLiveData<Int>()
    val orderSuccess = SingleLiveData<Unit>()
    val orderMessage = SingleLiveData<String>()

    @SuppressLint("NullSafeMutableLiveData")
    fun fetchOrder() {
        defaultLaunch {
            val data = orderRepository.fetchOrders()

            verifyTable(data?.table)
            verifyOrderValue(data)
            orderMessage.postValue(data?.message)

            if (data != null && !data.isADefaultOrder()) {
                val collator = java.text.Collator.getInstance()
                collator.strength = java.text.Collator.PRIMARY
                val list = data.items.sortedWith(compareBy(collator) { it.name })
                data.items.clear()
                data.items.addAll(list)

                showOrderData.postValue(data)
                showEmptyPlaceHolder.postValue(false)
            } else showEmptyPlaceHolder.postValue(true)
        }
    }

    @SuppressLint("NullSafeMutableLiveData")
    private fun verifyTable(table: Table?) {
        if (table == null || table.isADefaultTable()) needTable.postValue(Unit)
        else tableData.postValue(table)
    }

    private fun verifyOrderValue(order: Order?) {
        var totalValue = 0.0
        order?.items?.forEach { dishe ->
            totalValue += dishe.price * dishe.quantity
        }
        updateOrderValue.postValue(totalValue)
    }

    fun updateTableData(table: Table) {
        defaultLaunch {
            menuRepository.setTable(table)
            tableData.postValue(table)
        }
    }

    fun addItemToOrder(item: DisheItem) {
        defaultLaunch {
            orderRepository.addNewItemToOrder(item)
            fetchOrder()
        }
    }

    fun removeItemToOrder(item: DisheItem) {
        defaultLaunch {
            if (item.quantity == 1) {
                orderRepository.deleteItemToOrder(item)
            } else {
                orderRepository.removeItemToOrder(item)
            }
            fetchOrder()
        }
    }

    fun deleteItemToOrder(item: DisheItem) {
        defaultLaunch {
            orderRepository.deleteItemToOrder(item)
            fetchOrder()
        }
    }

    fun submitOrder() {
        defaultLaunch {
            val order = orderRepository.fetchOrders()
            when {
                order == null || order.isADefaultOrder() -> {
                    showErrorMessage.postValue(R.string.invalid_order)
                    return@defaultLaunch
                }

                order.items.isEmpty() -> {
                    showErrorMessage.postValue(R.string.invalid_order)
                    return@defaultLaunch
                }

                order.table.isADefaultTable() -> {
                    showErrorMessage.postValue(R.string.select_table)
                    return@defaultLaunch
                }

                else -> {
                    val success = orderRepository.submitOrder()
                    if (success) orderSuccess.postValue(Unit)
                    else showErrorMessage.postValue(R.string.order_error)
                }
            }
        }
    }

    fun saveMessage(message: String) {
        defaultLaunch {
            orderRepository.setMessage(message)
            fetchOrder()
        }
    }
}
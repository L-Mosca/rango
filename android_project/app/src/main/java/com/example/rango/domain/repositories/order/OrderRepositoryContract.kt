package com.example.rango.domain.repositories.order

import com.example.rango.domain.models.menu.DisheItem
import com.example.rango.domain.models.order.Order
import com.example.rango.domain.models.order.OrderList
import kotlinx.coroutines.flow.Flow

interface OrderRepositoryContract {

    suspend fun addNewItemToOrder(newItem: DisheItem)

    suspend fun removeItemToOrder(itemRemoved: DisheItem)

    suspend fun fetchOrders(): Order?

    suspend fun submitOrder(): Boolean

    suspend fun clearOrder()

    suspend fun createNewOrder(newOrder: Order)

    suspend fun observeOrder(): Flow<Order?>

    suspend fun deleteItemToOrder(item: DisheItem)

    suspend fun setMessage(message: String)

    suspend fun fetchOnlineOrders(): List<Order>

    suspend fun closeOrder(orderList: OrderList)
}
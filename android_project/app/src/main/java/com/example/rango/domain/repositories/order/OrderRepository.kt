package com.example.rango.domain.repositories.order

import com.example.rango.domain.models.menu.DisheItem
import com.example.rango.domain.models.order.Order
import com.example.rango.domain.models.order.OrderList
import com.example.rango.domain.models.order.isADefaultOrder
import com.example.rango.domain.models.order.toOrders
import com.example.rango.utils.ApiConstants
import com.example.rangodomain.local.datastore.PreferencesDataStoreContract
import com.google.firebase.database.ktx.database
import com.google.firebase.database.snapshots
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class OrderRepository @Inject constructor(private val preferencesDataStore: PreferencesDataStoreContract) :
    OrderRepositoryContract {

    var order = Order.buildDefaultOrder()

    override suspend fun addNewItemToOrder(newItem: DisheItem) {
        if (order.items.find { it.id == newItem.id } != null) {
            // has this item in order
            val index = order.items.indexOfFirst { newItem.id == it.id }
            order.items[index].quantity += 1
            order.value = order.value.plus(newItem.price)
            preferencesDataStore.setOrder(order)
        } else {
            // its a new item in order
            newItem.quantity = 1
            order.items.add(newItem)
            order.value = order.value.plus(newItem.price)
            preferencesDataStore.setOrder(order)
        }
    }

    override suspend fun removeItemToOrder(itemRemoved: DisheItem) {
        if (order.items.find { it.id == itemRemoved.id } != null) {
            val index = order.items.indexOfFirst { it.id == itemRemoved.id }
            order.items[index].quantity -= 1
            if (order.items[index].quantity == 0) order.items.removeAt(index)
            if (order.value > 0.0) order.value = order.value.minus(itemRemoved.price)
            preferencesDataStore.setOrder(order)
        }
    }

    override suspend fun fetchOrders(): Order? {
        val data = preferencesDataStore.getOrder()
        if (data != null && !data.isADefaultOrder()) order = data

        return data
    }

    override suspend fun submitOrder(): Boolean {
        val data = preferencesDataStore.getOrder()
        if (data != null && !data.isADefaultOrder()) {
            val database = Firebase.database
            val ref = database.reference.child("/${ApiConstants.ORDERS}").push()
            val uniqueId = ref.key
            data.id = uniqueId ?: "test"
            data.waiter = preferencesDataStore.getUserData()?.name ?: ""
            ref.setValue(data.toMap())
            preferencesDataStore.setOrder(Order.buildDefaultOrder())
            order = preferencesDataStore.getOrder()!!
            return true
        } else {
            return false
        }
    }

    override suspend fun clearOrder() {
        preferencesDataStore.setOrder(Order.buildDefaultOrder())
    }

    override suspend fun createNewOrder(newOrder: Order) {
        preferencesDataStore.setOrder(newOrder)
        this.order = newOrder
    }

    override suspend fun observeOrder(): Flow<Order?> = preferencesDataStore.observeOrder()
    override suspend fun deleteItemToOrder(item: DisheItem) {
        order.items.remove(item)
        if (order.value > 0) order.value = order.value.minus(item.price)
        preferencesDataStore.setOrder(order)
    }

    override suspend fun setMessage(message: String) {
        preferencesDataStore.setMessage(message)
    }

    override suspend fun fetchOnlineOrders(): List<Order> {
        val database = Firebase.database
        val firebaseData = database.getReference("/${ApiConstants.ORDERS}")
        val raw = firebaseData.snapshots.first().value as HashMap<*, *>?
        val orderList = raw.toOrders()
        return orderList
    }

    override suspend fun closeOrder(orderList: OrderList) {
        val database = Firebase.database
        val ref = database.reference.child("/${ApiConstants.CLOSED_ORDERS}").push()
        val uniqueId = ref.key
        val user = preferencesDataStore.getUserData()?.name
        orderList.id = uniqueId ?: "test"
        ref.setValue(orderList.toMap(user ?: ""))
        orderList.orderList.forEach { removeOrderFromDB(it.id) }
    }

    private fun removeOrderFromDB(id: String) {
        val database = Firebase.database.reference
        database.child("/${ApiConstants.ORDERS}").child(id).removeValue()
    }
}
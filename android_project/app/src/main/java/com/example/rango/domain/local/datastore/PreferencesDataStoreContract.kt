package com.example.rango.domain.local.datastore

import com.example.rango.domain.models.menu.DisheItem
import com.example.rango.domain.models.menu.Table
import com.example.rango.domain.models.order.Order
import com.example.rango.domain.models.user.User
import kotlinx.coroutines.flow.Flow

interface PreferencesDataStoreContract {

    suspend fun saveUserData(user: User?)

    suspend fun getUserData(): User?

    suspend fun setTable(tableData: Table?)

    suspend fun getTable(): Table?

    suspend fun getOrder(): Order?

    suspend fun setOrder(orderData: Order?)

    suspend fun addOrderItem(dishe: DisheItem)

    suspend fun removeOrderItem(dishe: DisheItem)

    suspend fun clearOrder()

    suspend fun observeOrder(): Flow<Order?>

    suspend fun setMessage(message: String)

    suspend fun showGreetings(): Boolean
    suspend fun showGreetings(showGreetings: Boolean)
}
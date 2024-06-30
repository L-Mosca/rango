@file:Suppress("UNCHECKED_CAST")

package com.example.rango.domain.models.order

import android.os.Parcelable
import com.example.rango.domain.models.menu.DisheItem
import com.example.rango.domain.models.menu.Table
import com.example.rango.domain.models.menu.isADefaultTable
import com.example.rango.domain.models.menu.toDishItemList
import com.google.firebase.database.Exclude
import kotlinx.parcelize.Parcelize

@Parcelize
data class Order(
    var id: String = "",
    var table: Table,
    var items: MutableList<DisheItem> = mutableListOf(),
    var value: Double = 0.0,
    var message: String = "",
    var waiter: String = ""
) : Parcelable {

    companion object {
        fun buildDefaultOrder(): Order =
            Order(
                id = "-1",
                table = Table.buildADefaultTable(),
                items = mutableListOf(),
                value = 0.0,
                message = "",
                waiter = ""
            )
    }

    @Exclude
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "id" to id,
            "table" to table.toMap(),
            "items" to items.map { it.toMap() },
            "value" to value,
            "message" to message,
            "waiter" to waiter,
        )
    }
}

fun Order.isADefaultOrder(): Boolean =
    this.id == "-1" && items.isEmpty() && value == 0.0 && table.isADefaultTable()


fun HashMap<*, *>?.toOrders(): List<Order> {
    val list = mutableListOf<Order>()
    if (this == null) return list
    this.forEach { (_, item) ->
        if (item != null) {
            val data = item as Map<*, *>
            val id = data["id"] as String
            val items = (data["items"] as ArrayList<HashMap<*, *>>).toDishItemList()
            val value = data["value"] as Long
            val message = data["message"] as String?
            val waiter = data["waiter"] as String?

            val table = data["table"] as Map<*, *>
            val tableId = table["id"] as Long
            val tableName = table["name"] as String
            val tableData = Table(id = tableId.toInt(), name = tableName)

            val order = Order(
                id = id,
                table = tableData,
                items = items.toMutableList(),
                value = value.toDouble(),
                message = message ?: "",
                waiter = waiter ?: ""
            )
            list.add(order)
        }
    }
    return list.sortedBy { it.id }
}

@Parcelize
data class OrderList(
    var id: String = "",
    var orderList: List<Order>,
) : Parcelable {

    companion object {
        fun buildDefaultOrderList(): OrderList = OrderList(orderList = emptyList())

        fun newInstance(order: List<Order>): OrderList = OrderList(orderList = order)
    }

    @Exclude
    fun toMap(waiter: String): Map<String, Any?> {
        val dishe = orderList.map { it.items.map { it.id } }.toString()
        val value = orderList.sumOf { it.value }
        return mapOf(
            "id" to id,
            "orderList" to dishe,
            "value" to value,
            "waiter" to waiter,
        )
    }
}
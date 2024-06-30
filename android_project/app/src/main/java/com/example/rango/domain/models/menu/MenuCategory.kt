package com.example.rango.domain.models.menu

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class MenuCategory(
    val id: Int,
    val name: String?
) : Parcelable

fun HashMap<*, *>?.toMenuCategories(): List<MenuCategory> {
    val list = mutableListOf<MenuCategory>()
    if (this == null) return list
    this.forEach { (_, category) ->
        if (category != null) {
            val data = category as Map<*, *>
            val id = data["id"] as Long
            val name = data["name"] as String
            val map = MenuCategory(id = id.toInt(), name = name)
            list.add(map)
        }
    }
    return list.sortedBy { it.id }
}
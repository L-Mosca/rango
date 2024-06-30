package com.example.rango.domain.models.menu

import android.os.Parcelable
import com.google.firebase.database.Exclude
import kotlinx.parcelize.Parcelize

@Parcelize
data class Table(
    val id: Int,
    val name: String
) : Parcelable {

    companion object {
        fun buildADefaultTable(): Table = Table(id = -1, name = "")
    }

    @Exclude
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "id" to id,
            "name" to name
        )
    }
}

fun HashMap<*, *>?.toTables(): List<Table> {
    val list = mutableListOf<Table>()
    if (this == null) return list
    this.forEach { (_, item) ->
        if (item != null) {
            val data = item as Map<*, *>
            val id = data["id"] as Long
            val name = data["name"] as String
            val table = Table(id = id.toInt(), name = name)
            list.add(table)
        }
    }
    return list.sortedBy { it.id }
}

fun Table.isADefaultTable(): Boolean = this.id == -1
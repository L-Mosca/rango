package com.example.rango.domain.models.menu

import android.os.Parcelable
import com.google.firebase.database.Exclude
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.parcelize.Parcelize

@Parcelize
data class DisheItem(
    val id: Int,
    val name: String,
    val price: Double,
    val image: String,
    val category: Int,
    var quantity: Int = 0,
) : Parcelable {

    companion object {
        fun newInstance(disheItem: DisheItem): DisheItem = DisheItem(
            id = disheItem.id,
            name = disheItem.name,
            price = disheItem.price,
            image = disheItem.image,
            category = disheItem.category,
            quantity = 1
        )
    }

    @Exclude
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "id" to id,
            "name" to name,
            "price" to price,
            "image" to image,
            "category" to category,
            "quantity" to quantity
        )
    }
}


@Parcelize
data class DisheList(
    val list: List<DisheItem>,
    val category: Int,
) : Parcelable

fun ArrayList<HashMap<*, *>>.toDishItemList(): List<DisheItem> {
    val raw = Gson().toJson(this)
    val itemType = object : TypeToken<List<DisheItem>>() {}.type
    val dishes: List<DisheItem> = Gson().fromJson(raw, itemType)
    return dishes
}

fun HashMap<*, *>?.toDisheList(): List<DisheItem> {
    val list = mutableListOf<DisheItem>()
    if (this == null) return list

    this.forEach { (_, item) ->
        if (item != null) {
            val data = item as Map<*, *>
            val id = (data["id"] as Long).toInt()
            val name = data["name"] as String
            val price = (data["price"] as Long).toDouble()
            val image = data["image"] as String
            val category = (data["category"] as Long).toInt()
            val quantity = 0
            list.add(
                DisheItem(
                    id = id,
                    name = name,
                    price = price,
                    image = image,
                    category = category,
                    quantity = quantity
                )
            )
        }
    }
    val collator = java.text.Collator.getInstance()
    collator.strength = java.text.Collator.PRIMARY

    return list.sortedWith(compareBy(collator) { it.name })
}

fun List<DisheItem>.orderByCategory(): List<DisheList> {
    val list = mutableListOf<DisheList>()
    if (this.isNotEmpty()) {
        val groupedByCategory: Map<Int, List<DisheItem>> = this.groupBy { it.category }
        groupedByCategory.values.forEach { disheItems ->
            list.add(DisheList(disheItems, disheItems.first().category))
        }
    }
    return list
}
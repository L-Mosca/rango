package com.example.rango.screen.home.menu.dishelist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.updateLayoutParams
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.rango.R
import com.example.rango.base.BaseListAdapter
import com.example.rango.base.ViewHolder
import com.example.rango.databinding.AdapterDishItemBinding
import com.example.rango.domain.models.menu.DisheItem
import com.example.rango.domain.models.order.Order
import com.example.rango.utils.convertToCurrency
import com.example.rango.utils.floatToDp
import com.squareup.picasso.Picasso

class DisheAdapter : BaseListAdapter<AdapterDishItemBinding, DisheItem>(DisheDiffUtil()) {

    companion object {
        const val FIRST_VIEW = 0
        const val DEFAULT_VIEW = 1
        const val LAST_VIEW = 2
    }

    class DisheDiffUtil : DiffUtil.ItemCallback<DisheItem>() {
        override fun areItemsTheSame(oldItem: DisheItem, newItem: DisheItem): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: DisheItem, newItem: DisheItem): Boolean =
            oldItem == newItem
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder<AdapterDishItemBinding> {
        val binding =
            AdapterDishItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        if (viewType == FIRST_VIEW) {
            val marginDp = floatToDp(parent.context, 50f)
            binding.root.updateLayoutParams<RecyclerView.LayoutParams> {
                topMargin = marginDp.toInt()
            }
        }
        return ViewHolder(binding)
    }

    override fun getItemViewType(position: Int) = when (position) {
        0 -> FIRST_VIEW
        currentList.lastIndex -> LAST_VIEW
        else -> DEFAULT_VIEW
    }

    override val bindingInflater: (LayoutInflater, ViewGroup) -> AdapterDishItemBinding
        get() = { layoutInflater, viewGroup ->
            AdapterDishItemBinding.inflate(
                layoutInflater,
                viewGroup,
                false
            )
        }

    var addItem: ((Int) -> Unit)? = null
    var removeItem: ((Int) -> Unit)? = null

    var orderData: Order = Order.buildDefaultOrder()

    override fun onBindViewHolder(
        holder: ViewHolder<AdapterDishItemBinding>,
        data: DisheItem,
        position: Int
    ) {
        holder.binding.apply {
            Picasso.get().load(data.image).into(ivDishe)
            tvDisheName.text = data.name
            tvDisheTotalPrice.text = data.price.convertToCurrency()

            val itemInOrder = orderData.items.filter { it.id == data.id }
            if (orderData.items.isNotEmpty() && itemInOrder.isNotEmpty()) {
                data.quantity = itemInOrder.first().quantity
                tvDisheQuantity.text = data.quantity.toString()
            } else {
                data.quantity = 0
                tvDisheQuantity.text = data.quantity.toString()
            }

            val disheValue = (data.quantity * data.price).convertToCurrency()
            tvDisheTotalValue.text = root.context.getString(R.string.dishe_total_value, disheValue)

            ivSum.setOnClickListener {
                addItem?.invoke(position)
            }
            ivMinus.setOnClickListener {
                if (data.quantity > 0) {
                    removeItem?.invoke(position)
                }
            }
        }
    }
}
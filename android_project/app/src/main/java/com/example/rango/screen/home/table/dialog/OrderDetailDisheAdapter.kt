package com.example.rango.screen.home.table.dialog

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.example.rango.base.BaseListAdapter
import com.example.rango.base.ViewHolder
import com.example.rango.databinding.AdapterOrderDetailDishesBinding
import com.example.rango.domain.models.menu.DisheItem
import com.example.rango.utils.convertToCurrency
import com.squareup.picasso.Picasso

class OrderDetailDisheAdapter :
    BaseListAdapter<AdapterOrderDetailDishesBinding, DisheItem>(OrderDetailDisheDiffUtil()) {

    class OrderDetailDisheDiffUtil : DiffUtil.ItemCallback<DisheItem>() {
        override fun areItemsTheSame(oldItem: DisheItem, newItem: DisheItem): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: DisheItem, newItem: DisheItem): Boolean =
            oldItem == newItem
    }

    override val bindingInflater: (LayoutInflater, ViewGroup) -> AdapterOrderDetailDishesBinding
        get() = { layoutInflater, viewGroup ->
            AdapterOrderDetailDishesBinding.inflate(
                layoutInflater,
                viewGroup,
                false
            )
        }

    override fun onBindViewHolder(
        holder: ViewHolder<AdapterOrderDetailDishesBinding>,
        data: DisheItem,
        position: Int
    ) {
        holder.binding.apply {
            Picasso.get().load(data.image).into(ivDishe)
            tvDisheName.text = data.name
            tvDisheQuantity.text = data.quantity.toString()
            tvDisheTotalPrice.text = data.price.convertToCurrency()
        }
    }
}
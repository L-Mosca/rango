package com.example.rango.screen.home.menu.dishelist.bottomsheet

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.example.rango.base.BaseListAdapter
import com.example.rango.base.ViewHolder
import com.example.rango.databinding.AdapterChooseTableBinding
import com.example.rango.domain.models.menu.Table

class ChooseTableAdapter :
    BaseListAdapter<AdapterChooseTableBinding, Table>(ChooseTableDiffUtil()) {

    class ChooseTableDiffUtil : DiffUtil.ItemCallback<Table>() {
        override fun areItemsTheSame(oldItem: Table, newItem: Table): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Table, newItem: Table): Boolean =
            oldItem == newItem
    }

    override val bindingInflater: (LayoutInflater, ViewGroup) -> AdapterChooseTableBinding
        get() = { layoutInflater, viewGroup ->
            AdapterChooseTableBinding.inflate(
                layoutInflater,
                viewGroup,
                false
            )
        }

    var onTableSelected: ((Table) -> Unit)? = null

    override fun onBindViewHolder(
        holder: ViewHolder<AdapterChooseTableBinding>,
        data: Table,
        position: Int
    ) {
        holder.binding.apply {
            rlChooseTable.setOnClickListener { onTableSelected?.invoke(data) }
            tvChooseTableNumber.text = data.name
        }
    }
}
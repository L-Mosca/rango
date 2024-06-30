package com.example.rango.screen.home.table

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.core.view.updateLayoutParams
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.rango.base.BaseListAdapter
import com.example.rango.base.ViewHolder
import com.example.rango.databinding.AdapterTableBinding
import com.example.rango.domain.models.menu.Table
import com.example.rango.domain.models.order.Order
import com.example.rango.domain.models.order.OrderList
import com.example.rango.utils.floatToDp

class TableAdapter : BaseListAdapter<AdapterTableBinding, Table>(TableDiffUtil()) {

    companion object {
        const val FIRST_VIEW = 0
        const val DEFAULT_VIEW = 1
        const val LAST_VIEW = 2
    }

    class TableDiffUtil : DiffUtil.ItemCallback<Table>() {
        override fun areItemsTheSame(oldItem: Table, newItem: Table): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Table, newItem: Table): Boolean =
            oldItem == newItem
    }

    override val bindingInflater: (LayoutInflater, ViewGroup) -> AdapterTableBinding
        get() = { layoutInflater, viewGroup ->
            AdapterTableBinding.inflate(
                layoutInflater,
                viewGroup,
                false
            )
        }

    var orderList = mutableListOf<Order>()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder<AdapterTableBinding> {
        val binding =
            AdapterTableBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        if (viewType == FIRST_VIEW) {
            val marginDp = floatToDp(parent.context, 40f)
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

    var onShowDetailsClicked: ((OrderList) -> Unit)? = null

    override fun onBindViewHolder(
        holder: ViewHolder<AdapterTableBinding>,
        data: Table,
        position: Int
    ) {
        holder.binding.apply {
            tvTableOrder.text = data.name

            val hasOrder = orderList.filter { it.table == data }
            tvShowOrderDetails.isVisible = hasOrder.isNotEmpty()

            tvShowOrderDetails.setOnClickListener {
                onShowDetailsClicked?.invoke(
                    OrderList.newInstance(
                        hasOrder
                    )
                )
            }
        }
    }
}
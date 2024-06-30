package com.example.rango.screen.home.table.dialog

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.updateLayoutParams
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.rango.R
import com.example.rango.base.BaseListAdapter
import com.example.rango.base.ViewHolder
import com.example.rango.databinding.AdapterOrderDetailBinding
import com.example.rango.domain.models.order.Order
import com.example.rango.utils.convertToCurrency
import com.example.rango.utils.floatToDp

class OrderDetailAdapter :
    BaseListAdapter<AdapterOrderDetailBinding, Order>(OrderDetailDiffUtil()) {

    companion object {
        const val FIRST_VIEW = 0
        const val DEFAULT_VIEW = 1
        const val LAST_VIEW = 2
    }

    class OrderDetailDiffUtil : DiffUtil.ItemCallback<Order>() {
        override fun areItemsTheSame(oldItem: Order, newItem: Order): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Order, newItem: Order): Boolean =
            oldItem == newItem
    }

    override val bindingInflater: (LayoutInflater, ViewGroup) -> AdapterOrderDetailBinding
        get() = { layoutInflater, viewGroup ->
            AdapterOrderDetailBinding.inflate(
                layoutInflater,
                viewGroup,
                false
            )
        }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder<AdapterOrderDetailBinding> {
        val binding =
            AdapterOrderDetailBinding.inflate(LayoutInflater.from(parent.context), parent, false)

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

    override fun onBindViewHolder(
        holder: ViewHolder<AdapterOrderDetailBinding>,
        data: Order,
        position: Int
    ) {
        holder.binding.apply {
            tvOrderDetailTitle.text = root.context.getString(R.string.order_title, data.id)
            tvOrderDetailTable.text = data.table.name
            tvOrderDetailTotalValue.text =
                root.context.getString(R.string.dishe_total_value, data.value.convertToCurrency())
            tvOrderDetailWaiter.text = root.context.getString(R.string.waiter, data.waiter)

            val orderDetailDisheAdapter = OrderDetailDisheAdapter()
            rvOrderDetailDishes.adapter = orderDetailDisheAdapter
            orderDetailDisheAdapter.submitList(data.items)
        }
    }
}
package com.example.rango.screen.home.table.dialog

import android.os.Bundle
import android.view.LayoutInflater
import androidx.fragment.app.viewModels
import com.example.rango.R
import com.example.rango.base.BaseBottomSheetDialogFragment
import com.example.rango.databinding.DialogFragmentOrderDetailBinding
import com.example.rango.domain.models.order.OrderList
import com.example.rango.utils.convertToCurrency
import dagger.hilt.android.AndroidEntryPoint

@Suppress("DEPRECATION")
@AndroidEntryPoint
class OrderDetailDialog : BaseBottomSheetDialogFragment<DialogFragmentOrderDetailBinding>() {

    override val bindingInflater: (LayoutInflater) -> DialogFragmentOrderDetailBinding =
        DialogFragmentOrderDetailBinding::inflate

    override val viewModel: OrderDetailViewModel by viewModels()

    companion object {
        private const val ORDER_DETAIL_EXTRA = "OrderDetailDialog.orderData"

        fun newInstance(order: OrderList): OrderDetailDialog = OrderDetailDialog().apply {
            arguments = Bundle().apply {
                putParcelable(ORDER_DETAIL_EXTRA, order)
            }
        }
    }

    var order: OrderList = OrderList.buildDefaultOrderList()
    var onOrderClosed: (() -> Unit)? = null

    override fun initViews() {
        val adapter = OrderDetailAdapter()
        binding.rvOrderDetail.adapter = adapter
        adapter.submitList(order.orderList)
        binding.btCloseOrder.text =
            getString(R.string.close_order, order.orderList.sumOf { it.value }.convertToCurrency())
        binding.btCloseOrder.setOnClickListener { viewModel.closeOrder(order) }
    }

    override fun initObservers() {
        viewModel.closeOrderSuccess.observe(viewLifecycleOwner) {
            onOrderClosed?.invoke()
        }
    }

    override fun getBundleArguments() {
        order = arguments?.getParcelable(ORDER_DETAIL_EXTRA)!!
    }
}
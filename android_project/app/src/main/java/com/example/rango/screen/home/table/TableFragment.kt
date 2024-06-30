package com.example.rango.screen.home.table

import android.view.LayoutInflater
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import com.example.rango.R
import com.example.rango.base.BaseFragment
import com.example.rango.databinding.FragmentTableBinding
import com.example.rango.domain.models.menu.Table
import com.example.rango.domain.models.order.OrderList
import com.example.rango.screen.home.table.dialog.OrderDetailDialog
import com.example.rango.utils.LoadingStyle
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TableFragment : BaseFragment<FragmentTableBinding>() {
    override val bindingInflater: (LayoutInflater) -> FragmentTableBinding =
        FragmentTableBinding::inflate

    override val viewModel: TableViewModel by viewModels()

    var hideMenu: (() -> Unit)? = null
    private val adapter = TableAdapter()

    override fun initViews() {
        binding.clTable.setOnClickListener { hideMenu?.invoke() }
    }

    override fun initObservers() {
        viewModel.orders.observe(viewLifecycleOwner) { orderList ->
            adapter.orderList.clear()
            adapter.orderList.addAll(orderList)
        }

        viewModel.tableList.observe(viewLifecycleOwner) { tableList ->
            setupAdapter(tableList)
        }

        viewModel.showEmptyPlaceHolder.observe(viewLifecycleOwner) { showEmptyPlaceHolder ->
            showEmptyPlaceHolder(showEmptyPlaceHolder)
        }

        viewModel.loading.observe(viewLifecycleOwner) { isLoading ->
            showLoading(binding.includeTableLoading, LoadingStyle.LIST, isLoading)
        }
    }

    private fun setupAdapter(dataList: List<Table>) {
        binding.rvTables.adapter = adapter
        adapter.submitList(dataList)
        adapter.onShowDetailsClicked = { orderList ->
            showOrderDetailsDialog(orderList)
        }
    }

    private fun showOrderDetailsDialog(order: OrderList) {
        val dialog = OrderDetailDialog.newInstance(order)
        dialog.onOrderClosed = {
            dialog.dismiss()
            showShortToast(getString(R.string.order_close_success))
            viewModel.fetchTables()
        }
        dialog.show(childFragmentManager, dialog.tag)
    }

    private fun showEmptyPlaceHolder(showEmptyPlaceHolder: Boolean) {
        binding.includeEmptyTable.llEmptyTable.isVisible = showEmptyPlaceHolder
    }

    override fun onResume() {
        super.onResume()
        viewModel.fetchTables()
    }
}
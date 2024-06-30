package com.example.rango.screen.home.order

import android.view.LayoutInflater
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import com.example.rango.R
import com.example.rango.base.BaseFragment
import com.example.rango.databinding.FragmentOrderBinding
import com.example.rango.domain.models.menu.DisheItem
import com.example.rango.domain.models.order.Order
import com.example.rango.screen.home.menu.dishelist.bottomsheet.ChooseTableDialogFragment
import com.example.rango.screen.home.order.dialog.OrderMessageDialogFragment
import com.example.rango.utils.LoadingStyle
import com.example.rango.utils.convertToCurrency
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OrderFragment : BaseFragment<FragmentOrderBinding>() {
    override val bindingInflater: (LayoutInflater) -> FragmentOrderBinding =
        FragmentOrderBinding::inflate

    override val viewModel: OrderViewModel by viewModels()

    var hideMenu: (() -> Unit)? = null

    private val adapter = OrderAdapter()

    override fun initViews() {
        binding.clOrder.setOnClickListener { hideMenu?.invoke() }
        binding.tvOrderTable.setOnClickListener { showTableDialog() }
        binding.btSubmitOrder.setOnClickListener { viewModel.submitOrder() }
        binding.includeEmptyOrder.llEmptyOrder.setOnClickListener { }
        binding.tvMessage.setOnClickListener { showMessageDialog() }
    }

    override fun initObservers() {
        viewModel.orderMessage.observe(viewLifecycleOwner) {
            binding.tvMessage.text = it.ifEmpty { getString(R.string.add_order_message) }
        }

        viewModel.orderSuccess.observe(viewLifecycleOwner) {
            showShortSnackBar(getString(R.string.order_success))
            viewModel.fetchOrder()
        }

        viewModel.showErrorMessage.observe(viewLifecycleOwner) { stringRes ->
            showShortSnackBar(getString(stringRes))
        }

        viewModel.deleteAdapterItem.observe(viewLifecycleOwner) { position ->
            adapter.currentList.removeAt(position)
            adapter.notifyItemRemoved(position)
        }

        viewModel.updateItemInAdapter.observe(viewLifecycleOwner) { data ->
            adapter.currentList[data.first].quantity = data.second
            adapter.notifyItemChanged(data.first)
        }

        viewModel.updateOrderValue.observe(viewLifecycleOwner) { value ->
            binding.tvOrderTotalValue.text = value.convertToCurrency()
        }

        viewModel.needTable.observe(viewLifecycleOwner) {
            binding.tvOrderTable.text = getString(R.string.select_table)
            binding.tvOrderTable.setTextColor(requireContext().getColor(R.color.red))
        }

        viewModel.tableData.observe(viewLifecycleOwner) { table ->
            binding.tvOrderTable.text = table.name
            binding.tvOrderTable.setTextColor(requireContext().getColor(R.color.orange_500))
        }

        viewModel.showOrderData.observe(viewLifecycleOwner) { order ->
            adapter.orderData = order
            setupInitialData(order)
        }

        viewModel.showEmptyPlaceHolder.observe(viewLifecycleOwner) { showEmptyPlaceHolder ->
            showEmptyPlaceHolder(showEmptyPlaceHolder)
        }

        viewModel.loading.observe(viewLifecycleOwner) { isLoading ->
            showLoading(binding.includeOrderLoading, LoadingStyle.LIST, isLoading)
        }
    }

    private fun setupInitialData(order: Order) {
        setupAdapter(order.items)
    }

    private fun setupAdapter(itemList: List<DisheItem>) {
        binding.rvOrder.adapter = adapter
        adapter.submitList(itemList)
        adapter.addItem = { position ->
            viewModel.addItemToOrder(adapter.currentList[position])
        }
        adapter.removeItem = { position ->
            viewModel.removeItemToOrder(adapter.currentList[position])
        }
        adapter.deleteItem = { position ->
            viewModel.deleteItemToOrder(adapter.currentList[position])
        }
    }

    private fun showEmptyPlaceHolder(showEmptyPlaceHolder: Boolean) {
        binding.apply {
            includeEmptyOrder.llEmptyOrder.isVisible = showEmptyPlaceHolder
        }
    }

    private fun showTableDialog() {
        val dialog = ChooseTableDialogFragment()
        dialog.onTableSelected = { table ->
            viewModel.updateTableData(table)
        }
        dialog.isCancelable = true
        dialog.show(requireActivity().supportFragmentManager, dialog.tag)
    }

    private fun showMessageDialog() {
        val actualMessage =
            if (binding.tvMessage.text.toString() == getString(R.string.add_order_message)) ""
            else binding.tvMessage.text.toString()
        val dialog = OrderMessageDialogFragment.newInstance(actualMessage)
        dialog.onConfirmClicked = { text ->
            val message = text.ifEmpty { getString(R.string.add_order_message) }
            binding.tvMessage.text = message
            viewModel.saveMessage(message)
            dialog.dismiss()
        }
        dialog.show(childFragmentManager, dialog.tag)
    }

    override fun onResume() {
        super.onResume()
        viewModel.fetchOrder()
    }
}
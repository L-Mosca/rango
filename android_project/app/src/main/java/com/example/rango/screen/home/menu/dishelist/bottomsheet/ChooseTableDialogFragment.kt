package com.example.rango.screen.home.menu.dishelist.bottomsheet

import android.view.LayoutInflater
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.rango.base.BaseBottomSheetDialogFragment
import com.example.rango.databinding.DialogFragmentChooseTableBinding
import com.example.rango.domain.models.menu.Table
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ChooseTableDialogFragment :
    BaseBottomSheetDialogFragment<DialogFragmentChooseTableBinding>() {

    override val bindingInflater: (LayoutInflater) -> DialogFragmentChooseTableBinding =
        DialogFragmentChooseTableBinding::inflate

    override val viewModel: ChooseTableViewModel by viewModels()

    private val adapter = ChooseTableAdapter()

    var onTableSelected: ((Table) -> Unit)? = null

    override fun initViews() {
        viewModel.fetchTables()
    }

    override fun initObservers() {
        viewModel.tableList.observe(viewLifecycleOwner) { tableList ->
            setupAdapter(tableList)
        }

        viewModel.tableSelected.observe(viewLifecycleOwner) { table ->
            onTableSelected?.invoke(table)
            dismiss()
        }
    }

    override fun getBundleArguments() {}

    private fun setupAdapter(data: List<Table>) {
        viewLifecycleOwner.lifecycleScope.launch {
            binding.rvChooseTable.adapter = adapter
            adapter.submitList(data)
            adapter.onTableSelected = { table ->
                viewModel.selectTable(table)
            }
        }
    }
}
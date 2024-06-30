package com.example.rango.screen.home.menu.dishelist

import android.os.Bundle
import android.view.LayoutInflater
import androidx.fragment.app.viewModels
import com.example.rango.base.BaseFragment
import com.example.rango.databinding.FragmentDisheListBinding
import com.example.rango.domain.models.menu.DisheList
import com.example.rango.screen.home.menu.dishelist.bottomsheet.ChooseTableDialogFragment
import dagger.hilt.android.AndroidEntryPoint

@Suppress("DEPRECATION")
@AndroidEntryPoint
class DisheListFragment : BaseFragment<FragmentDisheListBinding>() {

    override val bindingInflater: (LayoutInflater) -> FragmentDisheListBinding =
        FragmentDisheListBinding::inflate
    override val viewModel: DisheListViewModel by viewModels()

    companion object {
        private const val DISHE_LIST_EXTRA = "DisheListFragment.data"

        fun newInstance(dataList: DisheList): DisheListFragment = DisheListFragment().apply {
            arguments = Bundle().apply {
                putParcelable(DISHE_LIST_EXTRA, dataList)
            }
        }
    }

    private lateinit var disheList: DisheList
    private val adapter = DisheAdapter()

    override fun initViews() {}

    override fun initObservers() {
        viewModel.showAlertDialog.observe(viewLifecycleOwner) {
            showTableDialog()
        }

        viewModel.updateItemInAdapter.observe(viewLifecycleOwner) { data ->
            adapter.currentList[data.first].quantity = data.second
            adapter.notifyItemChanged(data.first)
        }

        viewModel.orderData.observe(viewLifecycleOwner) { orderData ->
            adapter.orderData = orderData
            setupAdapter()
        }
    }

    private fun setupAdapter() {
        binding.rvDisheList.adapter = adapter
        binding.rvDisheList.itemAnimator = null
        adapter.submitList(disheList.list)
        adapter.addItem = { position ->
            viewModel.addItemToOrder(adapter.currentList[position])
        }
        adapter.removeItem = { position ->
            viewModel.removeItemToOrder(adapter.currentList[position])
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        disheList = arguments?.getParcelable(DISHE_LIST_EXTRA)!!
    }

    private fun showTableDialog() {
        val dialog = ChooseTableDialogFragment()
        dialog.isCancelable = false
        dialog.show(requireActivity().supportFragmentManager, dialog.tag)
    }

    override fun onResume() {
        super.onResume()
        viewModel.fetchOrder()
    }
}
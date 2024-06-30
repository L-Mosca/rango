package com.example.rango.screen.home.order.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.viewModels
import com.example.rango.R
import com.example.rango.base.BaseBottomSheetDialogFragment
import com.example.rango.databinding.DialogFragmentOrderMessageBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OrderMessageDialogFragment :
    BaseBottomSheetDialogFragment<DialogFragmentOrderMessageBinding>() {

    override val bindingInflater: (LayoutInflater) -> DialogFragmentOrderMessageBinding =
        DialogFragmentOrderMessageBinding::inflate
    override val viewModel: OrderMessageViewModel by viewModels()

    companion object {

        private const val ORDER_MESSAGE_EXTRA = "OrderMessageDialogFragment.message"

        fun newInstance(message: String): OrderMessageDialogFragment =
            OrderMessageDialogFragment().apply {
                arguments = Bundle().apply {
                    putString(ORDER_MESSAGE_EXTRA, message)
                }
            }
    }


    var onConfirmClicked: ((String) -> Unit)? = null
    var message = ""

    override fun initViews() {
        binding.etOrderMessage.setText(message)
        binding.etOrderMessage.setOnEditorActionListener { v, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEND) {
                onConfirmClicked?.invoke(v.text.toString())
            }
            false
        }

        binding.btSaveMessage.setOnClickListener {
            onConfirmClicked?.invoke(binding.etOrderMessage.text.toString())
        }
    }

    override fun initObservers() {}

    override fun getBundleArguments() {
        message = arguments?.getString(ORDER_MESSAGE_EXTRA) ?: getString(R.string.observation)
    }
}
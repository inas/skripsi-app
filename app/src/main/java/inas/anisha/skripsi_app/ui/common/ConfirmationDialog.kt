package inas.anisha.skripsi_app.ui.common

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import inas.anisha.skripsi_app.R
import inas.anisha.skripsi_app.databinding.FragmentConfirmationCommonBinding

class ConfirmationDialog : DialogFragment() {
    private lateinit var mBinding: FragmentConfirmationCommonBinding
    private var mCallback: ConfirmationDialogListener? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding =
            DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_confirmation_common,
                container,
                false
            )

        arguments?.getString(ARG_TITLE).let { mBinding.textviewTitle.text = it }
        arguments?.getString(ARG_MESSAGE).let { mBinding.textviewMessage.text = it }
        arguments?.getString(ARG_BUTTON).let { mBinding.buttonConfirm.text = it }

        mBinding.buttonClose.setOnClickListener { dismiss() }
        mBinding.buttonConfirm.setOnClickListener { mCallback?.onConfirmed() }

        return mBinding.root
    }

    fun setConfirmationDialogListener(callback: ConfirmationDialogListener) {
        mCallback = callback
    }

    interface ConfirmationDialogListener {
        fun onConfirmed()
    }

    companion object {
        const val TAG = "CONFIRMATION_DIALOG"
        const val ARG_TITLE = "ARG_TITLE"
        const val ARG_MESSAGE = "ARG_MESSAGE"
        const val ARG_BUTTON = "ARG_BUTTON"
    }
}
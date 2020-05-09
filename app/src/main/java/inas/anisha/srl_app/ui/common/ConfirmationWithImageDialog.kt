package inas.anisha.srl_app.ui.common

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import inas.anisha.srl_app.R
import inas.anisha.srl_app.databinding.FragmentConfirmationWithImageCommonBinding

class ConfirmationWithImageDialog : DialogFragment() {
    private lateinit var mBinding: FragmentConfirmationWithImageCommonBinding
    private var mCallback: ConfirmationDialogListener? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding =
            DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_confirmation_with_image_common,
                container,
                false
            )

        arguments?.getString(ARG_TITLE)?.let { mBinding.textviewTitle.text = it }
        arguments?.getString(ARG_MESSAGE)?.let { mBinding.textviewMessage.text = it }
        arguments?.getString(ARG_BUTTON)?.let { mBinding.buttonConfirm.text = it }
        arguments?.getInt(ARG_BACKGROUND)?.let { mBinding.imageview.setImageResource(it) }

        mBinding.buttonClose.setOnClickListener { dismiss() }
        mBinding.buttonConfirm.setOnClickListener { mCallback?.onConfirmed() }

        return mBinding.root
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        return dialog
    }

    fun setConfirmationDialogListener(callback: ConfirmationDialogListener) {
        mCallback = callback
    }

    interface ConfirmationDialogListener {
        fun onConfirmed()
    }

    companion object {
        const val TAG = "ADD_SCHOOL_SCHEDULE_DIALOG"
        const val ARG_TITLE = "ARG_TITLE"
        const val ARG_MESSAGE = "ARG_MESSAGE"
        const val ARG_BUTTON = "ARG_BUTTON"
        const val ARG_BACKGROUND = "ARG_BACKGROUND"
    }
}
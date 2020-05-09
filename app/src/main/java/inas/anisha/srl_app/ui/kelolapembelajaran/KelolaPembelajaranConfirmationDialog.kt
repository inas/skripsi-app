package inas.anisha.srl_app.ui.kelolapembelajaran

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import inas.anisha.srl_app.R
import inas.anisha.srl_app.databinding.FragmentConfirmationKelolaPembelajaranBinding

class KelolaPembelajaranConfirmationDialog : DialogFragment() {
    private lateinit var mBinding: FragmentConfirmationKelolaPembelajaranBinding
    private var mCallback: ConfirmationDialogListener? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding =
            DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_confirmation_kelola_pembelajaran,
                container,
                false
            )

        if (arguments?.getBoolean(ARG_IS_SUPPORTING_TARGET_EMPTY) == true) mBinding.textviewSupportingTarget.visibility =
            View.VISIBLE
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
        const val TAG = "KELOLA_PEMBELAJARAN_CONFIRMATION_DIALOG"
        const val ARG_IS_SUPPORTING_TARGET_EMPTY = "ARG_IS_SUPPORTING_TARGET_EMPTY"
    }
}
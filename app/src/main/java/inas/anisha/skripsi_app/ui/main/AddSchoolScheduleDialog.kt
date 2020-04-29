package inas.anisha.skripsi_app.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import inas.anisha.skripsi_app.R
import inas.anisha.skripsi_app.databinding.FragmentAddSchoolScheduleDialogBinding

class AddSchoolScheduleDialog : DialogFragment() {
    private lateinit var mBinding: FragmentAddSchoolScheduleDialogBinding
    private var mCallback: ConfirmationDialogListener? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding =
            DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_add_school_schedule_dialog,
                container,
                false
            )

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
        const val TAG = "ADD_SCHOOL_SCHEDULE_DIALOG"
    }
}
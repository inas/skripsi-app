package inas.anisha.skripsi_app.ui.evaluation

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import inas.anisha.skripsi_app.R
import inas.anisha.skripsi_app.databinding.FragmentEvaluationListDialogBinding

class EvaluationListDialog : DialogFragment() {
    private lateinit var mBinding: FragmentEvaluationListDialogBinding
    private var mCallback: ConfirmationDialogListener? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding =
            DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_evaluation_list_dialog,
                container,
                false
            )

        arguments?.getString(ARG_TITLE)?.let { mBinding.textviewName.text = it }

        val adapter =
            EvaluationListAdapter()
        mBinding.recyclerViewItems.adapter = adapter

        arguments?.getStringArrayList(ARG_ITEM)?.let { items ->
            arguments?.getIntegerArrayList(ARG_BOOLEAN)?.let { icons ->
                adapter.setList(items.zip(icons))
            }
        }

        mBinding.imageviewClose.setOnClickListener { dismiss() }

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
        const val ARG_ITEM = "ARG_ITEM"
        const val ARG_BOOLEAN = "ARG_BOOLEAN"
    }
}
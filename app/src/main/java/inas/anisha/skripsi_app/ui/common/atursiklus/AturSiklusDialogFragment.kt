package inas.anisha.skripsi_app.ui.common.atursiklus

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import inas.anisha.skripsi_app.R
import inas.anisha.skripsi_app.databinding.FragmentAturSiklusBinding

class AturSiklusDialogFragment : DialogFragment() {
    private lateinit var mBinding: FragmentAturSiklusBinding
    private var mCallback: OnSiklusChosenListener? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_atur_siklus, container, false)

        return mBinding.root
    }

    fun setOnSiklusChosenListener(callback: OnSiklusChosenListener) {
        mCallback = callback
    }

    interface OnSiklusChosenListener {
        fun onTargetAdded(cycleTime: Pair<Int, Int>)
    }

    companion object {
        const val TAG = "ATUR_SIKLUS_DIALOG"
    }
}
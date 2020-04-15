package inas.anisha.skripsi_app.ui.common.atursiklus

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
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

        mBinding.buttonClose.setOnClickListener { dismiss() }

        requireContext().let { context ->
            val frequencyAdapter: ArrayAdapter<String> =
                ArrayAdapter<String>(context, R.layout.dropdown_atur_siklus, FREQUENCY)
            mBinding.dropdownFrequency.setAdapter(frequencyAdapter)

            val oneToThirty = mutableListOf<Int>().apply {
                for (i in 1..30) {
                    this.add(i)
                }
            }
            val durationAdapter: ArrayAdapter<Int> =
                ArrayAdapter<Int>(context, R.layout.dropdown_atur_siklus, oneToThirty)
            mBinding.dropdownDuration.setAdapter(durationAdapter)

        }

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

        private val FREQUENCY = mutableListOf("Harian", "Mingguan", "Bulanan")
    }
}
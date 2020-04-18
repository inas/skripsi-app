package inas.anisha.skripsi_app.ui.common.atursiklus

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import inas.anisha.skripsi_app.R
import inas.anisha.skripsi_app.constant.SkripsiConstant
import inas.anisha.skripsi_app.databinding.FragmentSiklusAturBinding

class AturSiklusDialogFragment : DialogFragment() {
    private lateinit var mBinding: FragmentSiklusAturBinding
    private var mCallback: OnSiklusChosenListener? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_siklus_atur, container, false)

        mBinding.buttonClose.setOnClickListener { dismiss() }
        mBinding.buttonAtur.setOnClickListener { setCycleTime() }

        requireContext().let { context ->
            val frequencyAdapter: ArrayAdapter<String> =
                ArrayAdapter<String>(context, R.layout.item_dropdown, FREQUENCY)
            mBinding.dropdownFrequency.setAdapter(frequencyAdapter)

            val oneToThirty = mutableListOf<Int>().apply {
                for (i in 1..30) {
                    this.add(i)
                }
            }
            val durationAdapter: ArrayAdapter<Int> =
                ArrayAdapter<Int>(context, R.layout.item_dropdown, oneToThirty)
            mBinding.dropdownDuration.setAdapter(durationAdapter)

        }

        arguments?.getString(ARG_FREQUENCY, FREQUENCY[0])
            .let { mBinding.dropdownFrequency.setText(it, false) }
        arguments?.getString(ARG_DURATION, "1").let { mBinding.dropdownDuration.setText(it, false) }

        return mBinding.root
    }

    fun setOnSiklusChosenListener(callback: OnSiklusChosenListener) {
        mCallback = callback
    }

    fun setCycleTime() {
        val frequency = when (mBinding.dropdownFrequency.text.toString()) {
            FREQUENCY[0] -> SkripsiConstant.CYCLE_FREQUENCY_DAILY
            FREQUENCY[1] -> SkripsiConstant.CYCLE_FREQUENCY_WEEKLY
            else -> SkripsiConstant.CYCLE_FREQUENCY_MONTHLY
        }

        mCallback?.onTargetAdded(Pair(frequency, mBinding.dropdownDuration.text.toString().toInt()))
        dismiss()
    }

    interface OnSiklusChosenListener {
        fun onTargetAdded(cycleTime: Pair<Int, Int>)
    }

    companion object {
        const val TAG = "ATUR_SIKLUS_DIALOG"

        const val ARG_DURATION = "ARG_DURATION"
        const val ARG_FREQUENCY = "ARG_FREQUENCY"

        val FREQUENCY = mutableListOf("Harian", "Mingguan", "Bulanan")
    }
}
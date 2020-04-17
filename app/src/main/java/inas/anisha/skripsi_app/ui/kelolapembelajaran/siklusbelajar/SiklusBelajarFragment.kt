package inas.anisha.skripsi_app.ui.kelolapembelajaran.siklusbelajar

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import inas.anisha.skripsi_app.R
import inas.anisha.skripsi_app.constant.SkripsiConstant
import inas.anisha.skripsi_app.databinding.FragmentSiklusBelajarBinding
import inas.anisha.skripsi_app.ui.common.atursiklus.AturSiklusDialogFragment
import inas.anisha.skripsi_app.ui.kelolapembelajaran.KelolaPembelajaranViewModel

class SiklusBelajarFragment : Fragment() {

    private lateinit var mBinding: FragmentSiklusBelajarBinding
    private lateinit var mViewModel: KelolaPembelajaranViewModel

    private var customCycleTime: Pair<Int, Int>? = null
    private var recCycleTime0 = Pair(SkripsiConstant.CYCLE_FREQUENCY_WEEKLY, 1)
    private var recCycleTime1 = Pair(SkripsiConstant.CYCLE_FREQUENCY_WEEKLY, 2)
    private var recCycleTime2 = Pair(SkripsiConstant.CYCLE_FREQUENCY_MONTHLY, 1)
    private var recCycleTime3 = Pair(SkripsiConstant.CYCLE_FREQUENCY_MONTHLY, 2)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requireActivity().let {
            mViewModel = ViewModelProviders.of(it).get(KelolaPembelajaranViewModel::class.java)
        }
        mViewModel.cycleTime = recCycleTime0
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_siklus_belajar, container, false)

        setClickListener()
        selectTarget(true, false, false, false, false)

        return mBinding.root
    }

    fun setClickListener() {
        mBinding.buttonCycle0.setOnClickListener { selectTarget(true, false, false, false, false) }
        mBinding.buttonCycle1.setOnClickListener { selectTarget(false, true, false, false, false) }
        mBinding.buttonCycle2.setOnClickListener { selectTarget(false, false, true, false, false) }
        mBinding.buttonCycle3.setOnClickListener { selectTarget(false, false, false, true, false) }
        mBinding.buttonCycleCustom.setOnClickListener {
            selectTarget(
                false,
                false,
                false,
                false,
                true
            )
        }

        mBinding.textviewCustom.setOnClickListener { openAturSiklusDialog() }
    }


    fun selectTarget(
        isCycle0Selected: Boolean,
        isCycle1Selected: Boolean,
        isCycle2Selected: Boolean,
        isCycle3Selected: Boolean,
        isCycle4Selected: Boolean
    ) {
        mBinding.isCycle0Selected = isCycle0Selected
        mBinding.isCycle1Selected = isCycle1Selected
        mBinding.isCycle2Selected = isCycle2Selected
        mBinding.isCycle3Selected = isCycle3Selected
        mBinding.isCycle4Selected = isCycle4Selected

        when {
            isCycle0Selected -> mViewModel.cycleTime = recCycleTime0
            isCycle1Selected -> mViewModel.cycleTime = recCycleTime1
            isCycle2Selected -> mViewModel.cycleTime = recCycleTime2
            isCycle3Selected -> mViewModel.cycleTime = recCycleTime3
            else -> customCycleTime?.let { mViewModel.cycleTime = it }
        }
    }

    fun openAturSiklusDialog() {
        val aturSiklusDialog = AturSiklusDialogFragment().apply {
            arguments = Bundle().apply {
                customCycleTime?.let {
                    putString(AturSiklusDialogFragment.ARG_FREQUENCY, getFrequencyText(it.first))
                    putString(AturSiklusDialogFragment.ARG_DURATION, it.second.toString())
                }

            }

            setOnSiklusChosenListener(object : AturSiklusDialogFragment.OnSiklusChosenListener {
                override fun onTargetAdded(cycleTime: Pair<Int, Int>) {
                    customCycleTime = cycleTime

                    mBinding.buttonCycleCustom.visibility = View.VISIBLE
                    mBinding.buttonCycleCustom.text =
                        cycleTime.second.toString() + " " + getFrequencyText(cycleTime.first)

                    selectTarget(false, false, false, false, true)
                }
            })
        }
        aturSiklusDialog.show(childFragmentManager, AturSiklusDialogFragment.TAG)
    }

    fun getFrequencyText(cycleFrequency: Int): String {
        return when (cycleFrequency) {
            SkripsiConstant.CYCLE_FREQUENCY_DAILY -> AturSiklusDialogFragment.FREQUENCY[0]
            SkripsiConstant.CYCLE_FREQUENCY_WEEKLY -> AturSiklusDialogFragment.FREQUENCY[1]
            else -> AturSiklusDialogFragment.FREQUENCY[2]
        }
    }

    companion object {
        private const val ADDED_TARGET = "ADDED_TARGET"
        private const val REC_TARGET_0 = "REC_TARGET_0"
        private const val REC_TARGET_1 = "REC_TARGET_1"
    }
}
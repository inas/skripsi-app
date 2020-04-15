package inas.anisha.skripsi_app.ui.kelolapembelajaran.siklusbelajar

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import inas.anisha.skripsi_app.R
import inas.anisha.skripsi_app.databinding.FragmentSiklusBelajarBinding
import inas.anisha.skripsi_app.ui.common.atursiklus.AturSiklusDialogFragment
import inas.anisha.skripsi_app.ui.kelolapembelajaran.KelolaPembelajaranViewModel

class SiklusBelajarFragment : Fragment() {

    private lateinit var mBinding: FragmentSiklusBelajarBinding
    private lateinit var mViewModel: KelolaPembelajaranViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mViewModel = ViewModelProviders.of(this).get(KelolaPembelajaranViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_siklus_belajar, container, false)

        initViews()
        setClickListener()

        return mBinding.root
    }

    fun initViews() {
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
    }

    fun openAturSiklusDialog() {
        val aturSiklusDialog = AturSiklusDialogFragment()
        aturSiklusDialog.show(childFragmentManager, AturSiklusDialogFragment.TAG)
    }

    companion object {
        private const val ADDED_TARGET = "ADDED_TARGET"
        private const val REC_TARGET_0 = "REC_TARGET_0"
        private const val REC_TARGET_1 = "REC_TARGET_1"
    }
}
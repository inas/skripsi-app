package inas.anisha.srl_app.ui.main.perjalanan

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProviders
import inas.anisha.srl_app.R
import inas.anisha.srl_app.data.db.entity.CycleEntity
import inas.anisha.srl_app.databinding.FragmentCycleDetailBinding


class CycleDetailDialog : DialogFragment() {

    private lateinit var mBinding: FragmentCycleDetailBinding
    private lateinit var mViewModel: CycleViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.FullScreenDialogStyle)
        mViewModel = ViewModelProviders.of(this).get(CycleViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding =
            DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_cycle_detail,
                container,
                false
            )
        mBinding.lifecycleOwner = this

        val cycle = arguments?.getParcelable<CycleEntity>(ARG_CYCLE)
        cycle?.let {
            mViewModel.fromEntity(it)
            mBinding.viewModel = mViewModel

            mBinding.recyclerView.adapter = CompletedTargetsAdapter().apply {
                setTargets(it.completedTargetList)
            }
        }

        mBinding.imageviewClose.setOnClickListener { dismiss() }

        return mBinding.root
    }

    companion object {
        const val TAG = "CYCLE_DETAIL_DIALOG"

        const val ARG_CYCLE = "ARG_CYCLE"
    }
}
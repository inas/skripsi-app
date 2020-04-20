package inas.anisha.skripsi_app.ui.main.target

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import inas.anisha.skripsi_app.R
import inas.anisha.skripsi_app.constant.SkripsiConstant
import inas.anisha.skripsi_app.databinding.FragmentPageTargetBinding
import inas.anisha.skripsi_app.ui.common.tambahTarget.TambahTargetPendukungDialog
import inas.anisha.skripsi_app.ui.common.tambahTarget.TambahTargetUtamaDialog
import inas.anisha.skripsi_app.ui.kelolapembelajaran.targetpendukung.TargetPendukungViewModel
import inas.anisha.skripsi_app.ui.kelolapembelajaran.targetutama.TargetUtamaViewModel
import java.text.SimpleDateFormat
import java.util.*

class TargetFragment : Fragment() {

    private lateinit var mBinding: FragmentPageTargetBinding
    private lateinit var mViewModel: TargetViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mViewModel = ViewModelProviders.of(this).get(TargetViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_page_target, container, false)
        mBinding.viewModel = mViewModel
        mBinding.lifecycleOwner = this

        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mBinding.buttonAdd.setOnClickListener { openAddTargetPendukungDialog() }
        mBinding.layoutTargetUtama.layout.setOnClickListener { openModifyMainTargetDialog() }

        initMainTarget()
        initCycleTime()
        initSupportingTarget()
    }

    fun initMainTarget() {
        mViewModel.getMainTarget().observe(this, Observer {
            mBinding.layoutTargetUtama.viewModel = mViewModel.getMainTargetViewModel(it)
        })
    }

    fun initCycleTime() {
        val cycleTime = mViewModel.getCycleTime()
        val dateFormat = SimpleDateFormat("d LLLL yyyy", Locale.getDefault())
        val evaluationDate = mViewModel.getEvaluationDate()
        if (evaluationDate == -1L) {
            mBinding.textviewCycleDescription.visibility = View.GONE
        } else {
            mBinding.textviewCycleDescription.text =
                "Evaluasi berikutnya: " + dateFormat.format(mViewModel.getEvaluationDate())
        }

        val frequency = when (cycleTime.first) {
            SkripsiConstant.CYCLE_FREQUENCY_DAILY -> "Harian"
            SkripsiConstant.CYCLE_FREQUENCY_WEEKLY -> "Mingguan"
            else -> "Bulanan"
        }

        mBinding.textviewCycle.text =
            "" + (if (cycleTime.second == 1) "" else (" " + cycleTime.second + " ")) + frequency
    }

    fun initSupportingTarget() {
        val adapterIncompleteTarget = TargetPendukungRecyclerViewAdapter().apply {
            setItemListener(object : TargetPendukungRecyclerViewAdapter.ItemListener {
                override fun onItemClick(viewModel: TargetPendukungViewModel) {
                    openTargetPendukungDetail(viewModel.id)
                }
            })
            setHasStableIds(true)
        }
        mBinding.recyclerviewTargetIncomplete.adapter = adapterIncompleteTarget

        mViewModel.getIncompleteSupportingTargets().observe(this, Observer { targets ->
            mBinding.hasIncompleteSupportingTargets = targets.isNotEmpty()
            adapterIncompleteTarget.setTargets(targets.reversed())
        })

        val adapterCompletedTarget = TargetPendukungRecyclerViewAdapter().apply {
            setItemListener(object : TargetPendukungRecyclerViewAdapter.ItemListener {
                override fun onItemClick(viewModel: TargetPendukungViewModel) {
                    openTargetPendukungDetail(viewModel.id)
                }
            })
            setHasStableIds(true)
        }
        mBinding.recyclerviewTargetCompleted.adapter = adapterCompletedTarget

        mViewModel.getCompletedSupportingTargets().observe(this, Observer { targets ->
            mBinding.hasCompletedSupportingTargets = targets.isNotEmpty()
            adapterCompletedTarget.setTargets(targets.reversed())
        })

    }

    fun openAddTargetPendukungDialog() {
        val tambahTargetDialog = TambahTargetPendukungDialog()
        tambahTargetDialog.setOnTargetAddedListener(object :
            TambahTargetPendukungDialog.OnTargetModifiedListener {
            override fun onTargetModified(target: TargetPendukungViewModel) {
                mViewModel.addOrUpdateSupportingTarget(target)
            }
        })

        tambahTargetDialog.show(childFragmentManager, TambahTargetPendukungDialog.TAG)
    }

    fun openModifyMainTargetDialog() {
        val tambahTargetDialog = TambahTargetUtamaDialog().apply {
            arguments = Bundle().apply {
                putString("name", mViewModel.mainTarget.name)
                putString("note", mViewModel.mainTarget.note)
                mViewModel.mainTarget.date?.timeInMillis?.let { putLong("date", it) }
            }
        }

        tambahTargetDialog.setOnTargetModifiedListener(object :
            TambahTargetUtamaDialog.OnTargetModifiedListener {
            override fun onTargetModified(target: TargetUtamaViewModel) {
                mViewModel.updateMainTarget(target)
            }
        })

        tambahTargetDialog.show(childFragmentManager, TambahTargetUtamaDialog.TAG)
    }

    fun openTargetPendukungDetail(targetId: Long) {
        val intent = Intent(activity, TargetPendukungDetailActivity::class.java).apply {
            putExtra(
                TargetPendukungDetailActivity.EXTRA_ID,
                targetId
            )
        }
        startActivity(intent)
    }

    fun deleteTarget(target: TargetPendukungViewModel) {
        mViewModel.deleteSupportingTarget(target.id)
    }

    fun reInitData() {
        initCycleTime()
    }

    companion object {
        const val TAG = "TARGET_FRAGMENT"
    }
}
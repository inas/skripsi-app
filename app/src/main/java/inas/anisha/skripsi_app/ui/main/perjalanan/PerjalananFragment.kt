package inas.anisha.skripsi_app.ui.main.perjalanan

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import inas.anisha.skripsi_app.R
import inas.anisha.skripsi_app.data.db.entity.CycleEntity
import inas.anisha.skripsi_app.databinding.FragmentPagePerjalananBinding
import inas.anisha.skripsi_app.ui.common.ConfirmationDialog
import java.util.*


class PerjalananFragment : Fragment() {

    lateinit var mBinding: FragmentPagePerjalananBinding
    lateinit var mViewModel: PerjalananViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mViewModel = ViewModelProviders.of(this).get(PerjalananViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_page_perjalanan, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initUserData()
        initCycleStats()
        initCycleHistory()
        initMainTarget()
    }

    private fun initUserData() {
        mBinding.textviewName.text = mViewModel.getUserName()
        mBinding.textviewGradeAndStudy.text =
            mViewModel.getUserGrade() + " " + mViewModel.getUserStudy()
    }

    private fun initCycleStats() {
        mViewModel.getCurrentCycleTasks().observe(this, Observer { tasks ->
            val completedTasks = tasks.filter { it.isCompleted }
            val completenessValue = "" + completedTasks.size + "/" + tasks.size
            mBinding.textviewTaskValue.text = completenessValue

            val currentTime = Calendar.getInstance()
            val pastTasks = tasks.filter { it.endDate < currentTime }
            val onTimeTasks = pastTasks.filter { it.isOnTime }
            if (pastTasks.isNotEmpty()) {
                val percentage = onTimeTasks.size * 100 / pastTasks.size
                mBinding.textviewTimeValue.text = "" + percentage + "%"
            } else {
                mBinding.textviewTimeValue.text = "100%"
            }

        })

        mViewModel.getSupportingTargets().observe(this, Observer { targets ->
            val completedTargets = targets.filter { it.isCompleted }
            if (targets.isNotEmpty()) {
                val percentage = completedTargets.size * 100 / targets.size
                mBinding.textviewTargetValue.text = "" + percentage + "%"
            } else {
                mBinding.textviewTargetValue.text = "0%"
            }
        })
    }

    private fun initCycleHistory() {
        val adapter = PerjalananAdapter().apply {
            setItemListener(object : PerjalananAdapter.ItemListener {
                override fun onItemClick(position: Int) {
                    showCycleReflection(mViewModel.cycleHistory[position])
                }
            })
        }
        mBinding.recyclerviewPerjalanan.adapter = adapter

        mViewModel.getAllCycle().observe(this, Observer { cycles ->
            mViewModel.cycleHistory = cycles
            val cycleHistoryText =
                cycles.map { entity -> "Siklus " + entity.number + " - " + entity.completion + "%" }
            adapter.setContent(cycleHistoryText)
        })
    }

    private fun showCycleReflection(cycle: CycleEntity) {
        ConfirmationDialog().apply {
            arguments = Bundle().apply {
                putString(ConfirmationDialog.ARG_TITLE, "Refleksi Siklus " + cycle.number)
                putString(
                    ConfirmationDialog.ARG_MESSAGE,
                    cycle.reflection
                )
                putString(ConfirmationDialog.ARG_BUTTON, "OK")
            }

            setConfirmationDialogListener(object :
                ConfirmationDialog.ConfirmationDialogListener {
                override fun onConfirmed() {
                    dismiss()
                }
            })
        }.show(childFragmentManager, ConfirmationDialog.TAG)
    }

    private fun initMainTarget() {
        mBinding.layoutMainTarget.viewLineMiddle.visibility = View.GONE
        mBinding.layoutMainTarget.isCurrentCycle = true
        mBinding.layoutMainTarget.imageviewItem.setImageDrawable(
            resources.getDrawable(
                R.drawable.bg_main_target,
                null
            )
        )
        mViewModel.getMainTarget().observe(this, Observer {
            if (it != null) {
                mBinding.layoutMainTarget.textviewContent.text = it.name
            }
        })
    }

    fun reInitData() {
        initUserData()
    }

    companion object {
        const val TAG = "PERJALANAN_FRAGMENT"
    }
}
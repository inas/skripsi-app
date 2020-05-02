package inas.anisha.skripsi_app.ui.main.target

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.PopupMenu
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
import inas.anisha.skripsi_app.ui.main.schedule.schedule.AddScheduleDialog
import inas.anisha.skripsi_app.ui.main.schedule.schedule.ScheduleViewModel
import inas.anisha.skripsi_app.ui.main.schedule.school.AddSchoolClassDialog
import inas.anisha.skripsi_app.ui.main.schedule.school.SchoolClassViewModel
import inas.anisha.skripsi_app.utils.CalendarUtil

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
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mBinding.layoutTargetUtama.layout.setOnClickListener { openModifyMainTargetDialog() }

        initAddButton()
        initMainTarget()
        initCycleTime()
        initSupportingTarget()
    }

    fun initAddButton() {
        mBinding.buttonAdd.setOnClickListener { button ->
            requireContext().let {
                val popup = PopupMenu(it, button)
                popup.menuInflater.inflate(R.menu.add_button_menu, popup.menu)
                popup.setOnMenuItemClickListener {
                    when (it.itemId) {
                        R.id.action_target -> openAddSupportingTargetDialog()
                        R.id.action_schedule -> openAddScheduleDialog()
                        R.id.action_school_class -> openAddSchoolClassDialog()
                    }
                    true
                }
                popup.show()
            }
        }
    }

    fun initMainTarget() {
        mViewModel.getMainTarget().observe(this, Observer {
            if (it != null) mBinding.layoutTargetUtama.viewModel =
                mViewModel.getMainTargetViewModel(it)
        })
    }

    fun initCycleTime() {
        val cycleTime = mViewModel.getCycleTime()
        val evaluationDate = mViewModel.getEvaluationDate()
        if (evaluationDate == -1L) {
            mBinding.textviewCycleDescription.visibility = View.GONE
        } else {
            mBinding.textviewCycleDescription.text =
                "Evaluasi berikutnya: " + CalendarUtil.getDateDisplayDate(mViewModel.getEvaluationDate())
        }

        val frequency = SkripsiConstant.getCycleFrequencyString(cycleTime.first)

        mBinding.textviewCycle.text =
            (if (cycleTime.second == 1) "" else ("" + cycleTime.second + " ")) + frequency
    }

    fun initSupportingTarget() {
        val adapterIncompleteTarget = TargetPendukungAdapter().apply {
            setItemListener(object : TargetPendukungAdapter.ItemListener {
                override fun onItemClick(viewModel: TargetPendukungViewModel) {
                    openTargetPendukungDetail(viewModel.id)
                }
            })
            setHasStableIds(true)
        }
        mBinding.recyclerviewTargetIncomplete.adapter = adapterIncompleteTarget


        val adapterCompletedTarget = TargetPendukungAdapter().apply {
            setItemListener(object : TargetPendukungAdapter.ItemListener {
                override fun onItemClick(viewModel: TargetPendukungViewModel) {
                    openTargetPendukungDetail(viewModel.id)
                }
            })
            setHasStableIds(true)
        }
        mBinding.recyclerviewTargetCompleted.adapter = adapterCompletedTarget


        mViewModel.getSupportingTargets().observe(this, Observer { targets ->
            val completedTargets = mutableListOf<TargetPendukungViewModel>()
            val incompleteTargets = mutableListOf<TargetPendukungViewModel>()
            targets.forEach {
                if (it.isCompleted) completedTargets.add(it) else incompleteTargets.add(
                    it
                )
            }

            mBinding.hasIncompleteSupportingTargets = incompleteTargets.isNotEmpty()
            adapterIncompleteTarget.setTargets(incompleteTargets.reversed().toMutableList())

            mBinding.hasCompletedSupportingTargets = completedTargets.isNotEmpty()
            adapterCompletedTarget.setTargets(completedTargets.reversed().toMutableList())

            if (targets.isNotEmpty()) {
                val percentage = completedTargets.size * 100 / targets.size
                mViewModel.updateCurrentCycleCompleteness(percentage)
            }
        })

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

    fun openAddSupportingTargetDialog() {
        TambahTargetPendukungDialog().apply {
            setOnTargetAddedListener(object :
                TambahTargetPendukungDialog.OnTargetModifiedListener {
                override fun onTargetModified(target: TargetPendukungViewModel) {
                    mViewModel.addSupportingTarget(target)
                }
            })
        }.show(childFragmentManager, TambahTargetPendukungDialog.TAG)
    }

    fun openAddScheduleDialog() {
        AddScheduleDialog().apply {
            setAddScheduleDialogListener(object :
                AddScheduleDialog.AddScheduleDialogListener {
                override fun onScheduleModified(schedule: ScheduleViewModel) {
                    mViewModel.addSchedule(schedule)
                }
            })
        }.show(childFragmentManager, AddScheduleDialog.TAG)
    }

    fun openAddSchoolClassDialog() {
        AddSchoolClassDialog().apply {
            setAddSchoolDialogCallback(object :
                AddSchoolClassDialog.AddSchoolDialogCallback {
                override fun onSubmit(viewModel: SchoolClassViewModel) {
                    mViewModel.addSchoolClass(viewModel)
                }
            })
        }.show(childFragmentManager, AddSchoolClassDialog.TAG)
    }

    fun reInitData() {
        initCycleTime()
    }

    companion object {
        const val TAG = "TARGET_FRAGMENT"
    }
}
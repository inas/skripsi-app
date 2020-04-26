package inas.anisha.skripsi_app.ui.main.schedule

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.PopupMenu
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import inas.anisha.skripsi_app.R
import inas.anisha.skripsi_app.databinding.FragmentPageScheduleBinding
import inas.anisha.skripsi_app.ui.common.tambahTarget.TambahTargetPendukungDialog
import inas.anisha.skripsi_app.ui.kelolapembelajaran.targetpendukung.TargetPendukungViewModel
import inas.anisha.skripsi_app.ui.main.schedule.displaytask.DisplayTaskFragment

class ScheduleFragment : Fragment() {
    lateinit var mBinding: FragmentPageScheduleBinding
    lateinit var mViewModel: SchedulePageViewModel

    lateinit var activeFragment: Fragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mViewModel = ViewModelProviders.of(this).get(SchedulePageViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_page_schedule, container, false)

        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAddButton()

        activeFragment = DisplayTaskFragment()
        childFragmentManager.beginTransaction()
            .add(R.id.layout_placeholder, activeFragment, DisplayTaskFragment.TAG).commit()

        mBinding.layoutDisplay.setOnClickListener { displayButton ->
            requireContext().let {
                val popup = PopupMenu(it, displayButton)
                popup.menuInflater.inflate(R.menu.schedule_display_menu, popup.menu)
                popup.setOnMenuItemClickListener {
                    when (it.itemId) {
                        R.id.action_week -> {
                        }
                        R.id.action_day -> {
                        }
                        R.id.action_tasks -> {
                        }
                        R.id.action_school -> {
                        }
                    }
                    true
                }
                popup.show()
            }
        }
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

    companion object {
        val DISPLAY = mutableListOf("Pekan", "Hari", "Tugas", "Sekolah")
    }
}
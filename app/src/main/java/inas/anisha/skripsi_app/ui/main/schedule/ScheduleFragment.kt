package inas.anisha.skripsi_app.ui.main.schedule

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.PopupMenu
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProviders
import inas.anisha.skripsi_app.R
import inas.anisha.skripsi_app.databinding.FragmentPageScheduleBinding
import inas.anisha.skripsi_app.ui.common.tambahTarget.TambahTargetPendukungDialog
import inas.anisha.skripsi_app.ui.kelolapembelajaran.targetpendukung.TargetPendukungViewModel
import inas.anisha.skripsi_app.ui.main.schedule.displayday.DisplayDayFragment
import inas.anisha.skripsi_app.ui.main.schedule.displayschool.DisplaySchoolClassFragment
import inas.anisha.skripsi_app.ui.main.schedule.displaytask.DisplayTaskFragment
import inas.anisha.skripsi_app.ui.main.schedule.displayweek.DisplayWeekFragment
import inas.anisha.skripsi_app.ui.main.schedule.schedule.AddScheduleDialog
import inas.anisha.skripsi_app.ui.main.schedule.schedule.ScheduleViewModel
import inas.anisha.skripsi_app.ui.main.schedule.school.AddSchoolClassDialog
import inas.anisha.skripsi_app.ui.main.schedule.school.SchoolClassViewModel

class ScheduleFragment : Fragment() {
    lateinit var mBinding: FragmentPageScheduleBinding
    lateinit var mViewModel: SchedulePageViewModel
    lateinit var mFragmentManager: FragmentManager

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

        activeFragment = DisplayWeekFragment()
        childFragmentManager.beginTransaction()
            .add(R.id.layout_placeholder, activeFragment, DisplayWeekFragment.TAG).commit()

        mBinding.layoutDisplay.setOnClickListener { displayButton ->
            requireContext().let { context ->
                val popup = PopupMenu(context, displayButton)
                popup.menuInflater.inflate(R.menu.schedule_display_menu, popup.menu)
                popup.setOnMenuItemClickListener {
                    when (it.itemId) {
                        R.id.action_week -> {
                            childFragmentManager.beginTransaction().remove(activeFragment).commit()
                            activeFragment = DisplayWeekFragment()
                            childFragmentManager.beginTransaction()
                                .add(
                                    R.id.layout_placeholder,
                                    activeFragment,
                                    DisplayDayFragment.TAG
                                ).commit()
                        }
                        R.id.action_day -> {
                            childFragmentManager.beginTransaction().remove(activeFragment).commit()
                            activeFragment = DisplayDayFragment()
                            childFragmentManager.beginTransaction()
                                .add(
                                    R.id.layout_placeholder,
                                    activeFragment,
                                    DisplayDayFragment.TAG
                                ).commit()
                        }
                        R.id.action_tasks -> {
                            childFragmentManager.beginTransaction().remove(activeFragment).commit()
                            activeFragment = DisplayTaskFragment()
                            childFragmentManager.beginTransaction()
                                .add(
                                    R.id.layout_placeholder,
                                    activeFragment,
                                    DisplayTaskFragment.TAG
                                ).commit()
                        }
                        R.id.action_school -> {
                            childFragmentManager.beginTransaction().remove(activeFragment).commit()
                            activeFragment = DisplaySchoolClassFragment()
                            childFragmentManager.beginTransaction()
                                .add(
                                    R.id.layout_placeholder,
                                    activeFragment,
                                    DisplaySchoolClassFragment.TAG
                                ).commit()
                        }
                    }

                    mBinding.textviewDisplay.text = it.title
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

    fun openSchoolScheduleDisplay() {
        childFragmentManager.beginTransaction().remove(activeFragment).commit()
        activeFragment = DisplaySchoolClassFragment()
        childFragmentManager.beginTransaction()
            .add(
                R.id.layout_placeholder,
                activeFragment,
                DisplaySchoolClassFragment.TAG
            ).commit()

        mBinding.textviewDisplay.text = "Sekolah"
    }

    fun reIntitData() {
        if (activeFragment is DisplayWeekFragment) {
            (activeFragment as DisplayWeekFragment).reInitData()
        }
    }

    companion object {
        val DISPLAY = mutableListOf("Pekan", "Hari", "Tugas", "Sekolah")
    }
}
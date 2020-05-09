package inas.anisha.srl_app.ui.main.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.PopupMenu
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import inas.anisha.srl_app.R
import inas.anisha.srl_app.constant.SkripsiConstant
import inas.anisha.srl_app.data.db.entity.ScheduleEntity
import inas.anisha.srl_app.databinding.FragmentPageHomeBinding
import inas.anisha.srl_app.ui.common.tambahTarget.TambahTargetPendukungDialog
import inas.anisha.srl_app.ui.kelolapembelajaran.targetpendukung.TargetPendukungViewModel
import inas.anisha.srl_app.ui.main.schedule.schedule.AddScheduleDialog
import inas.anisha.srl_app.ui.main.schedule.schedule.ScheduleDetailActivity
import inas.anisha.srl_app.ui.main.schedule.schedule.ScheduleViewModel
import inas.anisha.srl_app.ui.main.schedule.school.AddSchoolClassDialog
import inas.anisha.srl_app.ui.main.schedule.school.SchoolClassDetailActivity
import inas.anisha.srl_app.ui.main.schedule.school.SchoolClassViewModel
import inas.anisha.srl_app.utils.CalendarUtil
import kotlinx.android.synthetic.main.item_besok.view.*
import java.util.*


class HomeFragment : Fragment() {

    lateinit var mBinding: FragmentPageHomeBinding
    lateinit var mViewModel: HomeViewModel

    lateinit var scheduleTimelineAdapter: ScheduleTimelineAdapter
    lateinit var importantTasksAdapter: ImportantScheduleAdapter
    lateinit var importantTestsAdapter: ImportantScheduleAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mViewModel = ViewModelProviders.of(this).get(HomeViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_page_home, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initAddButton()
        initSchedule()
        initImportantSchedule()
        initTomorrowsSchedule()
        observeData()
    }

    fun reInitData() {
        setTodaysDate()
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

    fun initSchedule() {
        setTodaysDate()

        scheduleTimelineAdapter = ScheduleTimelineAdapter()
        mBinding.recyclerviewJadwal.adapter = scheduleTimelineAdapter
    }

    fun initImportantSchedule() {
        importantTasksAdapter = ImportantScheduleAdapter().apply {
            setHasStableIds(true)
            setItemListener(object : ImportantScheduleAdapter.ItemListener {
                override fun onItemClick(scheduleId: Long) {
                    openScheduleDetail(scheduleId)
                }
            })
        }
        mBinding.recyclerviewImportantTasks.adapter = importantTasksAdapter

        importantTestsAdapter = ImportantScheduleAdapter().apply {
            setHasStableIds(true)
            setItemListener(object : ImportantScheduleAdapter.ItemListener {
                override fun onItemClick(scheduleId: Long) {
                    openScheduleDetail(scheduleId)
                }
            })
        }
        mBinding.recyclerviewImportantTests.adapter = importantTestsAdapter
    }

    fun initTomorrowsSchedule() {
        mBinding.layoutItemActivities.textview_description.text = "Kegiatan"
        mBinding.layoutItemClass.textview_description.text = "Kelas"
        mBinding.layoutItemTask.textview_description.text = "Tugas"
        mBinding.layoutItemTest.textview_description.text = "Ujian"
    }

    fun setTodaysDate() {
        Calendar.getInstance().let { today ->
            val oldDate = mViewModel.currentDate.value
            if (oldDate?.get(Calendar.HOUR_OF_DAY) != today.get(Calendar.HOUR_OF_DAY) && oldDate?.get(
                    Calendar.MINUTE
                ) != today.get(Calendar.MINUTE)
            ) {
                mViewModel.currentDate.value = today
                mBinding.textviewDate.text = CalendarUtil.getDateDisplayDayDate(today)
            }
        }
    }

    fun observeData() {
        mViewModel.getTodaysSchedule().observe(this, Observer { schedule ->
            val activities = mutableListOf<ScheduleEntity>()
            val tasks = mutableListOf<ScheduleEntity>()
            val tests = mutableListOf<ScheduleEntity>()

            schedule.forEach {
                when (it.type) {
                    SkripsiConstant.SCHEDULE_TYPE_ACTIVITY -> activities.add(it)
                    SkripsiConstant.SCHEDULE_TYPE_TASK -> tasks.add(it)
                    SkripsiConstant.SCHEDULE_TYPE_TEST -> tests.add(it)
                }
            }

            mViewModel.todaysActivities = activities
            mViewModel.todaysTasks = tasks
            mViewModel.todaysTests = tests

            updateSchedule()
            updateImportantSchedule()
        })

        mViewModel.getTodaysClasses().observe(this, Observer { classes ->
            mViewModel.todaysClasses = classes
            updateSchedule()
        })

        mViewModel.getTomorrowsClassesCount().observe(this, Observer { count ->
            mViewModel.tomorrowsClassCount = count
            mBinding.textviewTomorrowEmpty.visibility =
                if (mViewModel.tomorrowsScheduleCount + mViewModel.tomorrowsClassCount == 0) View.VISIBLE else View.GONE

            updateTomorrowsItem(
                mBinding.layoutItemClass,
                mBinding.layoutItemClass.textview_count,
                count
            )
        })


        mViewModel.getTomorrowsSchedule().observe(this, Observer { schedule ->
            val activities = mutableListOf<ScheduleEntity>()
            val tasks = mutableListOf<ScheduleEntity>()
            val tests = mutableListOf<ScheduleEntity>()

            schedule.forEach {
                when (it.type) {
                    SkripsiConstant.SCHEDULE_TYPE_ACTIVITY -> activities.add(it)
                    SkripsiConstant.SCHEDULE_TYPE_TASK -> tasks.add(it)
                    SkripsiConstant.SCHEDULE_TYPE_TEST -> tests.add(it)
                }
            }

            mViewModel.tomorrowsScheduleCount = activities.size + tasks.size + tests.size

            updateTomorrowsItem(
                mBinding.layoutItemActivities,
                mBinding.layoutItemActivities.textview_count,
                activities.size
            )
            updateTomorrowsItem(
                mBinding.layoutItemTask,
                mBinding.layoutItemTask.textview_count,
                tasks.size
            )
            updateTomorrowsItem(
                mBinding.layoutItemTest,
                mBinding.layoutItemTest.textview_count,
                tests.size
            )

            mBinding.textviewTomorrowEmpty.visibility =
                if (mViewModel.tomorrowsScheduleCount + mViewModel.tomorrowsClassCount == 0) View.VISIBLE else View.GONE
        })

        mViewModel.getCycleTasks().observe(this, Observer { tasks ->
            val completedTasks = tasks.filter { it.isCompleted }
            val completenessValue = "" + completedTasks.size + "/" + tasks.size

            mBinding.textviewProgressDescription.text =
                completenessValue + " tugas terselesaikan di siklus ini"
            if (tasks.isNotEmpty()) {
                mBinding.progressbar.progress = completedTasks.size * 100 / tasks.size
            } else {
                mBinding.progressbar.progress = 100
            }

        })
    }

    fun updateSchedule() {
        mBinding.hasTodaysActivities = mViewModel.todaysActivities.isNotEmpty()
        mBinding.hasTodaysClass = mViewModel.todaysClasses.isNotEmpty()

        val scheduleViewModels = mViewModel.getTodaysScheduleViewModels()
        scheduleTimelineAdapter.setList(scheduleViewModels)
        scheduleTimelineAdapter.setItemListener(object : ScheduleTimelineAdapter.ItemListener {
            override fun onItemClick(type: Int, id: Long) {
                if (type == SkripsiConstant.SCHEDULE_TIMELINE_TYPE_ACTIVITY) {
                    openScheduleDetail(id)
                } else {
                    openSchoolClassDetail(id)
                }
            }
        })
    }

    fun updateImportantSchedule() {
        mBinding.hasTodaysTasks = mViewModel.todaysTasks.isNotEmpty()
        mBinding.hasTodaysTests = mViewModel.todaysTests.isNotEmpty()

        val importantTaskViewModels =
            mViewModel.getImportantScheduleViewModels(mViewModel.todaysTasks)
        val importantTestsViewModels =
            mViewModel.getImportantScheduleViewModels(mViewModel.todaysTests)

        importantTasksAdapter.setList(importantTaskViewModels)
        importantTestsAdapter.setList(importantTestsViewModels)
    }

    fun updateTomorrowsItem(view: View, textView: TextView, count: Int) {
        if (count > 0) {
            view.visibility = View.VISIBLE
            textView.text = "" + count
        } else {
            view.visibility = View.GONE
        }
    }

    fun openSchoolClassDetail(schoolClassId: Long) {
        val intent = Intent(activity, SchoolClassDetailActivity::class.java).apply {
            putExtra(
                SchoolClassDetailActivity.EXTRA_ID,
                schoolClassId
            )
        }
        startActivity(intent)
    }

    fun openScheduleDetail(scheduleId: Long) {
        val intent = Intent(activity, ScheduleDetailActivity::class.java).apply {
            putExtra(
                ScheduleDetailActivity.EXTRA_ID,
                scheduleId
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
}
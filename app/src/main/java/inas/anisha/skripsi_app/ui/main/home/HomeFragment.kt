package inas.anisha.skripsi_app.ui.main.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import inas.anisha.skripsi_app.R
import inas.anisha.skripsi_app.constant.SkripsiConstant
import inas.anisha.skripsi_app.data.db.entity.ScheduleEntity
import inas.anisha.skripsi_app.databinding.FragmentPageHomeBinding
import inas.anisha.skripsi_app.utils.CalendarUtil
import kotlinx.android.synthetic.main.item_besok.view.*
import java.util.*

class HomeFragment : Fragment() {

    lateinit var mBinding: FragmentPageHomeBinding
    lateinit var mViewModel: HomeViewModel

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

        observeData()
        initSchedule()
        initImportantSchedule()
        initTomorrowsSchedule()
    }

    fun reInitData() {
        setTodaysDate()
    }

    fun initSchedule() {
        setTodaysDate()
        // TODO init adapter for todays schedule
    }

    fun initImportantSchedule() {
        // TODO init adapter for todays important tasks and tests
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
            if (oldDate?.get(Calendar.DAY_OF_YEAR) != today.get(Calendar.DAY_OF_YEAR)) {
                mViewModel.currentDate.value = today
                mBinding.textviewDate.text = CalendarUtil.getDateDisplay(today)
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
        })

        mViewModel.getCycleTasks().observe(this, Observer { tasks ->
            val completedTasks = tasks.filter { it.isCompleted }
            val completenessValue = "" + completedTasks.size + "/" + tasks.size
            mBinding.progressbar.progress = completedTasks.size * 100 / tasks.size
            mBinding.textviewProgressDescription.text =
                completenessValue + " tugas terselesaikan di siklus ini"
        })
    }

    fun updateSchedule() {
        val scheduleViewModels = mViewModel.getTodaysScheduleViewModels()
        // TODO map into viewmodel and show on recyclerview
    }

    fun updateImportantSchedule() {
        val importantTaskViewModels =
            mViewModel.getImportantScheduleViewModels(mViewModel.todaysTasks)
        val importantTestsViewModels =
            mViewModel.getImportantScheduleViewModels(mViewModel.todaysTests)
        // TODO map into viewmodel and show on recyclerview
    }

    fun updateTomorrowsItem(view: View, textView: TextView, count: Int) {
        if (count > 0) {
            view.visibility = View.VISIBLE
            textView.text = "" + count
        } else {
            view.visibility = View.GONE
        }
    }
}
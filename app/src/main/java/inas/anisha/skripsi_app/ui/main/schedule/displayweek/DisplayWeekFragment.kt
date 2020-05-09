package inas.anisha.skripsi_app.ui.main.schedule.displayweek

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import inas.anisha.skripsi_app.R
import inas.anisha.skripsi_app.constant.SkripsiConstant
import inas.anisha.skripsi_app.databinding.FragmentDisplayWeekBinding
import inas.anisha.skripsi_app.databinding.ItemScheduleBlockDisplayWeekBinding
import inas.anisha.skripsi_app.databinding.ItemScheduleBlockIndicatorBinding
import inas.anisha.skripsi_app.databinding.ItemScheduleBlockTimeSmallBinding
import inas.anisha.skripsi_app.ui.main.schedule.displayday.ScheduleBlockViewModel
import inas.anisha.skripsi_app.ui.main.schedule.schedule.ScheduleDetailActivity
import inas.anisha.skripsi_app.ui.main.schedule.school.SchoolClassDetailActivity
import inas.anisha.skripsi_app.utils.CalendarUtil.Companion.toMonthString
import inas.anisha.skripsi_app.utils.ViewUtil
import java.util.*

class DisplayWeekFragment : Fragment() {

    lateinit var mBinding: FragmentDisplayWeekBinding
    lateinit var mViewModel: DisplayWeekViewModel

    private var scheduleMondayObservable: LiveData<List<ScheduleBlockViewModel>>? = null
    private var scheduleTuesdayObservable: LiveData<List<ScheduleBlockViewModel>>? = null
    private var scheduleWednesdayObservable: LiveData<List<ScheduleBlockViewModel>>? = null
    private var scheduleThursdayObservable: LiveData<List<ScheduleBlockViewModel>>? = null
    private var scheduleFridayObservable: LiveData<List<ScheduleBlockViewModel>>? = null
    private var scheduleSaturdayObservable: LiveData<List<ScheduleBlockViewModel>>? = null
    private var scheduleSundayObservable: LiveData<List<ScheduleBlockViewModel>>? = null

    private var schoolMondayObservable: LiveData<List<ScheduleBlockViewModel>>? = null
    private var schoolTuesdayObservable: LiveData<List<ScheduleBlockViewModel>>? = null
    private var schoolWednesdayObservable: LiveData<List<ScheduleBlockViewModel>>? = null
    private var schoolThursdayObservable: LiveData<List<ScheduleBlockViewModel>>? = null
    private var schoolFridayObservable: LiveData<List<ScheduleBlockViewModel>>? = null
    private var schoolSaturdayObservable: LiveData<List<ScheduleBlockViewModel>>? = null

    private var dateOfWeekObservable: LiveData<Calendar>? = null

    private var indicatorView: View? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mViewModel = ViewModelProviders.of(this).get(DisplayWeekViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding =
            DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_display_week,
                container,
                false
            )

        mViewModel.setDatesOfWeek(
            Calendar.getInstance().apply { set(Calendar.DAY_OF_WEEK, Calendar.MONDAY) })
        removeObservers()
        observeSchedule()

        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mBinding.tvDate.text = mViewModel.mondayOfWeek.value?.toMonthString() ?: ""

        mBinding.ivPrevious.setOnClickListener {
            val newDate = Calendar.getInstance().apply {
                timeInMillis = mViewModel.mondayOfWeek.value?.timeInMillis ?: 0
                add(Calendar.WEEK_OF_YEAR, -1)
            }
            mViewModel.setDatesOfWeek(newDate)
            updateDates()
        }

        mBinding.ivNext.setOnClickListener {
            val newDate = Calendar.getInstance().apply {
                timeInMillis = mViewModel.mondayOfWeek.value?.timeInMillis ?: 0
                add(Calendar.WEEK_OF_YEAR, 1)
            }
            mViewModel.setDatesOfWeek(newDate)
            updateDates()
        }

        displayTime()
        updateDates()
        updateIndicatorViewPosition()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        removeObservers()
    }

    fun removeObservers() {
        scheduleMondayObservable?.removeObservers(this)
        scheduleTuesdayObservable?.removeObservers(this)
        scheduleWednesdayObservable?.removeObservers(this)
        scheduleThursdayObservable?.removeObservers(this)
        scheduleFridayObservable?.removeObservers(this)
        scheduleSaturdayObservable?.removeObservers(this)
        scheduleSundayObservable?.removeObservers(this)

        schoolMondayObservable?.removeObservers(this)
        schoolTuesdayObservable?.removeObservers(this)
        schoolWednesdayObservable?.removeObservers(this)
        schoolThursdayObservable?.removeObservers(this)
        schoolFridayObservable?.removeObservers(this)
        schoolSaturdayObservable?.removeObservers(this)

        dateOfWeekObservable?.removeObservers(this)
    }

    fun reInitData() {
        mViewModel.setDatesOfWeek(
            Calendar.getInstance().apply { set(Calendar.DAY_OF_WEEK, Calendar.MONDAY) })
        mBinding.tvDate.text = mViewModel.mondayOfWeek.value?.toMonthString() ?: ""
    }
    
    fun observeSchedule() {
        schoolMondayObservable = mViewModel.getSchoolSchedule(Calendar.MONDAY).apply {
            observe(this@DisplayWeekFragment, Observer {
                updateSchoolDisplay(Calendar.MONDAY, it)
            })
        }

        schoolTuesdayObservable = mViewModel.getSchoolSchedule(Calendar.TUESDAY).apply {
            observe(this@DisplayWeekFragment, Observer {
                updateSchoolDisplay(Calendar.TUESDAY, it)
            })
        }

        schoolWednesdayObservable = mViewModel.getSchoolSchedule(Calendar.WEDNESDAY).apply {
            observe(this@DisplayWeekFragment, Observer {
                updateSchoolDisplay(Calendar.WEDNESDAY, it)
            })
        }

        schoolThursdayObservable = mViewModel.getSchoolSchedule(Calendar.THURSDAY).apply {
            observe(this@DisplayWeekFragment, Observer {
                updateSchoolDisplay(Calendar.THURSDAY, it)
            })
        }

        schoolFridayObservable = mViewModel.getSchoolSchedule(Calendar.FRIDAY).apply {
            observe(this@DisplayWeekFragment, Observer {
                updateSchoolDisplay(Calendar.FRIDAY, it)
            })
        }

        schoolSaturdayObservable = mViewModel.getSchoolSchedule(Calendar.SATURDAY).apply {
            observe(this@DisplayWeekFragment, Observer {
                updateSchoolDisplay(Calendar.SATURDAY, it)
            })
        }

//        scheduleObservable = mViewModel.getSchedule().apply {
//            observe(this@DisplayWeekFragment, Observer {
//                mViewModel.schedule = it
//                updateDisplay()
//            })
//        }
    }

    fun updateSchoolDisplay(dayOfWeek: Int, schoolVms: List<ScheduleBlockViewModel>) {

        val schoolDayLayout: RelativeLayout = when (dayOfWeek) {
            2 -> mBinding.layoutSchoolMonday
            3 -> mBinding.layoutSchoolTuesday
            4 -> mBinding.layoutSchoolWednesday
            5 -> mBinding.layoutSchoolThursday
            6 -> mBinding.layoutSchoolFriday
            7 -> mBinding.layoutSchoolSaturday
            else -> mBinding.layoutSchoolSunday
        }
        schoolDayLayout.removeAllViewsInLayout()

        val blockWidth: Int = mBinding.layoutSchedule.width / 7
        val inflater = LayoutInflater.from(context)
        schoolVms.forEach { schedule ->
            val block: ItemScheduleBlockDisplayWeekBinding =
                DataBindingUtil.inflate(
                    inflater,
                    R.layout.item_schedule_block_display_week,
                    null,
                    false
                )
            block.tvName.text = schedule.name

            val param: RelativeLayout.LayoutParams =
                RelativeLayout.LayoutParams(blockWidth, schedule.height)
            param.topMargin = schedule.positionX

            val view = block.root.apply {
                layoutParams = param
                setBackgroundResource(schedule.bgResource)
                setOnClickListener { openDetail(schedule.type, schedule.id) }
            }

            schoolDayLayout.addView(view)
        }

        updateIndicatorViewPosition()
    }

    fun displayTime() {
        mBinding.layoutTime.removeAllViewsInLayout()

        val inflater = LayoutInflater.from(context)

        var timeWidth: Int
        var timeHeight: Int
        requireContext().let {
            timeWidth = ViewUtil.dpToPx(it, SkripsiConstant.TIME_WIDTH_SMALL.toDouble())
            timeHeight = ViewUtil.dpToPx(it, SkripsiConstant.BLOCK_HEIGHT_WEEK_DISPLAY.toDouble())
        }

        for (i in 0..23) {
            val time: ItemScheduleBlockTimeSmallBinding =
                DataBindingUtil.inflate(
                    inflater,
                    R.layout.item_schedule_block_time_small,
                    null,
                    false
                )
            time.tvTime.text = i.toString()

            time.root.apply {
                requireContext().let { context ->
                    val params = RelativeLayout.LayoutParams(timeWidth, timeHeight)
                    params.topMargin = ViewUtil.dpToPx(
                        context,
                        (SkripsiConstant.BLOCK_HEIGHT_WEEK_DISPLAY * i).toDouble()
                    )
                    layoutParams = params
                }
                mBinding.layoutTime.addView(this)
            }
        }

        val indicator: ItemScheduleBlockIndicatorBinding =
            DataBindingUtil.inflate(inflater, R.layout.item_schedule_block_indicator, null, false)
        indicatorView = indicator.root
        requireContext().let { context ->
            val width = ViewUtil.dpToPx(context, SkripsiConstant.INDICATOR_WIDTH_SMALL.toDouble())
            val height =
                ViewUtil.dpToPx(context, SkripsiConstant.BLOCK_HEIGHT_WEEK_DISPLAY.toDouble())
            val hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
            val params = RelativeLayout.LayoutParams(width, height)
            params.addRule(RelativeLayout.ALIGN_PARENT_END)
            params.topMargin =
                ViewUtil.dpToPx(
                    context,
                    (SkripsiConstant.BLOCK_HEIGHT_WEEK_DISPLAY * hour).toDouble()
                )
            indicatorView?.layoutParams = params
        }
        mBinding.layoutTime.addView(indicatorView)

        updateIndicatorViewPosition()
    }

    fun updateIndicatorViewPosition() {
        requireContext().let { context ->
            val width = ViewUtil.dpToPx(context, SkripsiConstant.INDICATOR_WIDTH_SMALL.toDouble())
            val height =
                ViewUtil.dpToPx(context, SkripsiConstant.BLOCK_HEIGHT_WEEK_DISPLAY.toDouble())
            val hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
            val params = RelativeLayout.LayoutParams(width, height)
            params.addRule(RelativeLayout.ALIGN_PARENT_END)
            params.topMargin =
                ViewUtil.dpToPx(
                    context,
                    (SkripsiConstant.BLOCK_HEIGHT_WEEK_DISPLAY * hour).toDouble()
                )
            indicatorView?.layoutParams = params
        }

        val hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
        requireContext().let {
            mBinding.scrollView.smoothScrollTo(
                0, ViewUtil.dpToPx(
                    it,
                    (SkripsiConstant.BLOCK_HEIGHT_WEEK_DISPLAY * hour).toDouble()
                )
            )
        }
    }


    fun updateDates() {
        mBinding.layoutDateMonday.tvDate.text =
            mViewModel.mondayOfWeek.value?.get(Calendar.DAY_OF_MONTH).toString()
        mBinding.layoutDateMonday.tvDay.text = "Sen"

        mBinding.layoutDateTuesday.tvDate.text =
            mViewModel.tuesdayOfWeek.value?.get(Calendar.DAY_OF_MONTH).toString()
        mBinding.layoutDateTuesday.tvDay.text = "Sel"

        mBinding.layoutDateWednesday.tvDate.text =
            mViewModel.wednesdayOfWeek.value?.get(Calendar.DAY_OF_MONTH).toString()
        mBinding.layoutDateWednesday.tvDay.text = "Rab"

        mBinding.layoutDateThursday.tvDate.text =
            mViewModel.thursdayOfWeek.value?.get(Calendar.DAY_OF_MONTH).toString()
        mBinding.layoutDateThursday.tvDay.text = "Kam"

        mBinding.layoutDateFriday.tvDate.text =
            mViewModel.fridayOfWeek.value?.get(Calendar.DAY_OF_MONTH).toString()
        mBinding.layoutDateFriday.tvDay.text = "Jum"

        mBinding.layoutDateSaturday.tvDate.text =
            mViewModel.saturdayOfWeek.value?.get(Calendar.DAY_OF_MONTH).toString()
        mBinding.layoutDateSaturday.tvDay.text = "Sab"

        mBinding.layoutDateSunday.tvDate.text =
            mViewModel.sundayOfWeek.value?.get(Calendar.DAY_OF_MONTH).toString()
        mBinding.layoutDateSunday.tvDay.text = "Min"
    }

    fun openDetail(type: Int, id: Long) {
        if (type == SkripsiConstant.SCHEDULE_TYPE_SCHOOL) openSchoolScheduleDetail(id) else openScheduleDetail(
            id
        )
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

    fun openSchoolScheduleDetail(schoolScheduleId: Long) {
        val intent = Intent(activity, SchoolClassDetailActivity::class.java).apply {
            putExtra(
                SchoolClassDetailActivity.EXTRA_ID,
                schoolScheduleId
            )
        }
        startActivity(intent)
    }

    companion object {
        const val TAG = "DISPLAY_WEEK_FRAGMENT"
    }
}
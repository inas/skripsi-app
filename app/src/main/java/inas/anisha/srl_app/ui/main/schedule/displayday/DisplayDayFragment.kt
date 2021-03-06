package inas.anisha.srl_app.ui.main.schedule.displayday

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
import inas.anisha.srl_app.R
import inas.anisha.srl_app.constant.SkripsiConstant
import inas.anisha.srl_app.databinding.FragmentDisplayDayBinding
import inas.anisha.srl_app.databinding.ItemScheduleBlockBinding
import inas.anisha.srl_app.databinding.ItemScheduleBlockIndicatorBinding
import inas.anisha.srl_app.databinding.ItemScheduleBlockTimeBinding
import inas.anisha.srl_app.ui.main.schedule.schedule.ScheduleDetailActivity
import inas.anisha.srl_app.ui.main.schedule.school.SchoolClassDetailActivity
import inas.anisha.srl_app.utils.CalendarUtil.Companion.toDayDateString
import inas.anisha.srl_app.utils.ViewUtil
import java.util.*

class DisplayDayFragment : Fragment() {

    lateinit var mBinding: FragmentDisplayDayBinding
    lateinit var mViewModel: DisplayDayViewModel

    private var scheduleObservable: LiveData<List<ScheduleBlockViewModel>>? = null
    private var schoolObservable: LiveData<List<ScheduleBlockViewModel>>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mViewModel = ViewModelProviders.of(this).get(DisplayDayViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding =
            DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_display_day,
                container,
                false
            )

        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mViewModel.displayedDate.value = Calendar.getInstance()
        mBinding.tvDate.text = mViewModel.displayedDate.value?.toDayDateString() ?: ""

        mBinding.ivPrevious.setOnClickListener {
            val newDate = Calendar.getInstance().apply {
                timeInMillis = mViewModel.displayedDate.value?.timeInMillis ?: 0
                add(Calendar.DAY_OF_MONTH, -1)
            }
            mViewModel.displayedDate.value = newDate
            mBinding.tvDate.text = mViewModel.displayedDate.value?.toDayDateString() ?: ""
        }

        mBinding.ivNext.setOnClickListener {
            val newDate = Calendar.getInstance().apply {
                timeInMillis = mViewModel.displayedDate.value?.timeInMillis ?: 0
                add(Calendar.DAY_OF_MONTH, 1)
            }
            mViewModel.displayedDate.value = newDate
            mBinding.tvDate.text = mViewModel.displayedDate.value?.toDayDateString() ?: ""
        }

        observeSchedule()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        scheduleObservable?.removeObservers(this)
        schoolObservable?.removeObservers(this)
    }

    fun observeSchedule() {
        schoolObservable = mViewModel.getSchoolSchedule().apply {
            observe(this@DisplayDayFragment, Observer {
                mViewModel.schoolSchedule = it
                updateDisplay()
            })
        }

        scheduleObservable = mViewModel.getSchedule().apply {
            observe(this@DisplayDayFragment, Observer {
                mViewModel.schedule = it
                updateDisplay()
            })
        }
    }

    fun updateDisplay() {
        mViewModel.updateDisplay(mBinding.layoutSchedule.width)

        mBinding.layoutSchedule.removeAllViewsInLayout()
        mBinding.layoutTime.removeAllViewsInLayout()

        val inflater = LayoutInflater.from(context)

        mViewModel.allSchedule.forEach { schedule ->
            val block: ItemScheduleBlockBinding =
                DataBindingUtil.inflate(inflater, R.layout.item_schedule_block, null, false)
            block.tvName.text = schedule.name

            val param: RelativeLayout.LayoutParams =
                RelativeLayout.LayoutParams(schedule.width, schedule.height)
            param.marginStart = schedule.marginStart
            param.topMargin = schedule.positionX

            val view = block.root.apply {
                layoutParams = param
                setBackgroundResource(schedule.bgResource)
                setOnClickListener { openDetail(schedule.type, schedule.id) }
            }

            mBinding.layoutSchedule.addView(view)
        }

        for (i in 0..23) {
            val time: ItemScheduleBlockTimeBinding =
                DataBindingUtil.inflate(inflater, R.layout.item_schedule_block_time, null, false)
            time.tvTime.text = i.toString()

            time.root.apply {
                requireContext().let { context ->
                    val width = ViewUtil.dpToPx(context, SkripsiConstant.TIME_WIDTH.toDouble())
                    val height = ViewUtil.dpToPx(context, SkripsiConstant.BLOCK_HEIGHT.toDouble())
                    val params = RelativeLayout.LayoutParams(width, height)
                    params.topMargin =
                        ViewUtil.dpToPx(context, (SkripsiConstant.BLOCK_HEIGHT * i).toDouble())
                    layoutParams = params
                }
                mBinding.layoutTime.addView(this)
            }
        }

        val indicator: ItemScheduleBlockIndicatorBinding =
            DataBindingUtil.inflate(inflater, R.layout.item_schedule_block_indicator, null, false)
        val indicatorView = indicator.root
        requireContext().let { context ->
            val width = ViewUtil.dpToPx(context, SkripsiConstant.INDICATOR_WIDTH.toDouble())
            val height = ViewUtil.dpToPx(context, SkripsiConstant.BLOCK_HEIGHT.toDouble())
            val hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
            val params = RelativeLayout.LayoutParams(width, height)
            params.addRule(RelativeLayout.ALIGN_PARENT_END)
            params.topMargin =
                ViewUtil.dpToPx(context, (SkripsiConstant.BLOCK_HEIGHT * hour).toDouble())
            indicatorView.layoutParams = params
        }
        mBinding.layoutTime.addView(indicatorView)

        val hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
        requireContext().let {
            mBinding.scrollView.smoothScrollTo(
                0, ViewUtil.dpToPx(
                    it,
                    (SkripsiConstant.BLOCK_HEIGHT * hour).toDouble()
                )
            )
        }
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
        const val TAG = "DISPLAY_DAY_FRAGMENT"
    }
}
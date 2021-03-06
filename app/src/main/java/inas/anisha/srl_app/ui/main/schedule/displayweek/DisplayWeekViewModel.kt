package inas.anisha.srl_app.ui.main.schedule.displayweek

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import inas.anisha.srl_app.R
import inas.anisha.srl_app.constant.SkripsiConstant
import inas.anisha.srl_app.data.Repository
import inas.anisha.srl_app.ui.main.schedule.displayday.ScheduleBlockViewModel
import inas.anisha.srl_app.utils.CalendarUtil.Companion.getNextMidnight
import inas.anisha.srl_app.utils.CalendarUtil.Companion.getNextXDay
import inas.anisha.srl_app.utils.CalendarUtil.Companion.getPreviousMidnight
import inas.anisha.srl_app.utils.ViewUtil
import java.util.*

class DisplayWeekViewModel(val mApplication: Application) : AndroidViewModel(mApplication) {

    val mRepository: Repository = Repository.getInstance(mApplication)
    val mondayOfWeek: MutableLiveData<Calendar> =
        MutableLiveData(Calendar.getInstance().apply { set(Calendar.DAY_OF_WEEK, Calendar.MONDAY) })

    val tuesdayOfWeek: MutableLiveData<Calendar> = MutableLiveData(Calendar.getInstance().apply {
        set(Calendar.DAY_OF_WEEK, Calendar.MONDAY)
        add(Calendar.DAY_OF_MONTH, 1)
    })
    val wednesdayOfWeek: MutableLiveData<Calendar> = MutableLiveData(Calendar.getInstance().apply {
        set(Calendar.DAY_OF_WEEK, Calendar.MONDAY)
        add(Calendar.DAY_OF_MONTH, 2)
    })
    val thursdayOfWeek: MutableLiveData<Calendar> = MutableLiveData(Calendar.getInstance().apply {
        set(Calendar.DAY_OF_WEEK, Calendar.MONDAY)
        add(Calendar.DAY_OF_MONTH, 3)
    })

    val fridayOfWeek: MutableLiveData<Calendar> = MutableLiveData(Calendar.getInstance().apply {
        set(Calendar.DAY_OF_WEEK, Calendar.MONDAY)
        add(Calendar.DAY_OF_MONTH, 4)
    })

    val saturdayOfWeek: MutableLiveData<Calendar> = MutableLiveData(Calendar.getInstance().apply {
        set(Calendar.DAY_OF_WEEK, Calendar.MONDAY)
        add(Calendar.DAY_OF_MONTH, 5)
    })

    val sundayOfWeek: MutableLiveData<Calendar> = MutableLiveData(Calendar.getInstance().apply {
        set(Calendar.DAY_OF_WEEK, Calendar.MONDAY)
        add(Calendar.DAY_OF_MONTH, 6)
    })

    fun setDatesOfWeek(date: Calendar) {
        mondayOfWeek.value = date
        tuesdayOfWeek.value = date.getNextXDay(1)
        wednesdayOfWeek.value = date.getNextXDay(2)
        thursdayOfWeek.value = date.getNextXDay(3)
        fridayOfWeek.value = date.getNextXDay(4)
        saturdayOfWeek.value = date.getNextXDay(5)
        sundayOfWeek.value = date.getNextXDay(6)
    }

    fun getSchoolSchedule(dayOfWeek: Int): LiveData<List<ScheduleBlockViewModel>> =
        Transformations.map(
            mRepository.getSchoolClassesSorted(dayOfWeek)
        ) {
            it.map {
                ScheduleBlockViewModel().apply {
                    id = it.id
                    name = it.name
                    type = SkripsiConstant.SCHEDULE_TYPE_SCHOOL
                    startMinute = it.startMinuteOfDay
                    endMinute = it.endMinuteOfDay
                    bgResource = R.drawable.bg_block_school_week_display
                    height = ViewUtil.dpToPx(
                        mApplication,
                        (endMinute - startMinute) * SkripsiConstant.MINUTE_HEIGHT_WEEK_DISPLAY
                    )
                    positionX = ViewUtil.dpToPx(
                        mApplication,
                        SkripsiConstant.MINUTE_HEIGHT_WEEK_DISPLAY * startMinute
                    )
                }
            }
        }

    fun getSchedule(date: MutableLiveData<Calendar>): LiveData<List<ScheduleBlockViewModel>> =
        Transformations.switchMap(date) {
            Transformations.map(
                mRepository.getScheduleSorted(it.getPreviousMidnight(), it.getNextMidnight())
            ) { list ->
                list.map {
                    ScheduleBlockViewModel().apply {
                        id = it.id
                        name = it.name
                        type = it.type
                        startMinute = it.startMinuteOfDay
                        endMinute = it.endMinuteOfDay
                        if (type == SkripsiConstant.SCHEDULE_TYPE_TASK) endMinute += 30
                        bgResource = when (type) {
                            SkripsiConstant.SCHEDULE_TYPE_TASK -> R.drawable.bg_block_task_week_display
                            SkripsiConstant.SCHEDULE_TYPE_TEST -> R.drawable.bg_block_test_week_display
                            else -> R.drawable.bg_block_activity_week_display
                        }
                        height = ViewUtil.dpToPx(
                            mApplication,
                            (endMinute - startMinute) * SkripsiConstant.MINUTE_HEIGHT_WEEK_DISPLAY
                        )
                        positionX = ViewUtil.dpToPx(
                            mApplication,
                            SkripsiConstant.MINUTE_HEIGHT_WEEK_DISPLAY * startMinute
                        )
                    }
                }
            }
        }

//    fun updateDisplay(blockWidth: Int) {
//        allSchedule = schedule + schoolSchedule
//        allSchedule =
//            allSchedule.sortedWith(compareBy({ it.startMinute }, { -it.endMinute }, { -it.type }))
//
//        var prev: ScheduleBlockViewModel? = null
//        allSchedule.forEachIndexed { index, current ->
//            prev?.let {
//                if (current.startMinute == it.startMinute) {
//                    current.exactScheduleCount = it.exactScheduleCount + 1
//                    current.exactScheduleOrder = it.exactScheduleOrder + 1
//                    current.overlappingScheduleCount = it.overlappingScheduleCount
//                    incrementPrevExactCount(index - 1)
//                } else if (isOverlapping(current, it)) {
//                    current.overlappingScheduleCount = it.overlappingScheduleCount + 1
//                }
//            }
//            prev = current
//        }
//
//        allSchedule.forEach {
//            val overlapShiftTotal = ViewUtil.dpToPx(
//                mApplication,
//                (it.overlappingScheduleCount * SkripsiConstant.OVERLAP_SHIFT).toDouble()
//            )
//            if (it.exactScheduleCount > 0) {
//                it.width = (blockWidth - overlapShiftTotal) / (it.exactScheduleCount + 1)
//                it.marginStart = overlapShiftTotal + (it.exactScheduleOrder * it.width)
//            } else {
//                it.width = blockWidth - overlapShiftTotal
//                it.marginStart = overlapShiftTotal
//            }
//
//            it.height = ViewUtil.dpToPx(
//                mApplication,
//                (it.endMinute - it.startMinute) * SkripsiConstant.MINUTE_HEIGHT
//            )
//            it.positionX =
//                ViewUtil.dpToPx(mApplication, SkripsiConstant.MINUTE_HEIGHT * it.startMinute)
//        }
//
//        allSchedule
//
//    }
//
//    fun incrementPrevExactCount(index: Int) {
//        if (allSchedule[index].exactScheduleCount > 0 && index > 0) incrementPrevExactCount(index - 1)
//        allSchedule[index].exactScheduleCount++
//    }
//
//    fun isOverlapping(item1: ScheduleBlockViewModel, item2: ScheduleBlockViewModel) =
//        item1.startMinute < item2.endMinute && item1.endMinute > item2.startMinute
}
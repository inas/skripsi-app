package inas.anisha.skripsi_app.ui.main.schedule.displayday

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import inas.anisha.skripsi_app.R
import inas.anisha.skripsi_app.constant.SkripsiConstant
import inas.anisha.skripsi_app.data.Repository
import inas.anisha.skripsi_app.utils.CalendarUtil.Companion.getNextMidnight
import inas.anisha.skripsi_app.utils.CalendarUtil.Companion.getPreviousMidnight
import inas.anisha.skripsi_app.utils.ViewUtil
import java.util.*

class DisplayDayViewModel(val mApplication: Application) : AndroidViewModel(mApplication) {

    val mRepository: Repository = Repository.getInstance(mApplication)
    val displayedDate: MutableLiveData<Calendar> = MutableLiveData(Calendar.getInstance())

    var schoolSchedule: List<ScheduleBlockViewModel> = mutableListOf()
    var schedule: List<ScheduleBlockViewModel> = mutableListOf()
    var allSchedule: List<ScheduleBlockViewModel> = mutableListOf()

    fun getSchoolSchedule(): LiveData<List<ScheduleBlockViewModel>> =
        Transformations.switchMap(displayedDate) { date ->
            Transformations.map(
                mRepository.getSchoolClassesSorted(date.get(Calendar.DAY_OF_WEEK))
            ) {
                it.map {
                    ScheduleBlockViewModel().apply {
                        id = it.id
                        name = it.name
                        type = SkripsiConstant.SCHEDULE_TYPE_SCHOOL
                        startMinute = it.startMinuteOfDay
                        endMinute = it.endMinuteOfDay
                        bgResource = R.drawable.bg_block_school
                    }
                }
            }
        }

    fun getSchedule(): LiveData<List<ScheduleBlockViewModel>> =
        Transformations.switchMap(displayedDate) { date ->
            Transformations.map(
                mRepository.getScheduleSorted(date.getPreviousMidnight(), date.getNextMidnight())
            ) {
                it.map {
                    ScheduleBlockViewModel().apply {
                        id = it.id
                        name = it.name
                        type = it.type
                        startMinute = it.startMinuteOfDay
                        endMinute = it.endMinuteOfDay
                        if (type == SkripsiConstant.SCHEDULE_TYPE_TASK) endMinute += 30
                        bgResource = when (type) {
                            SkripsiConstant.SCHEDULE_TYPE_TASK -> R.drawable.bg_block_task
                            SkripsiConstant.SCHEDULE_TYPE_TEST -> R.drawable.bg_block_test
                            else -> R.drawable.bg_block_activity
                        }
                    }
                }
            }
        }

    fun updateDisplay(blockWidth: Int) {
        allSchedule = schedule + schoolSchedule
        allSchedule =
            allSchedule.sortedWith(compareBy({ it.startMinute }, { -it.endMinute }, { -it.type }))

        var prev: ScheduleBlockViewModel? = null
        allSchedule.forEachIndexed { index, current ->
            prev?.let {
                if (current.startMinute == it.startMinute) {
                    current.exactScheduleCount = it.exactScheduleCount + 1
                    current.exactScheduleOrder = it.exactScheduleOrder + 1
                    current.overlappingScheduleCount = it.overlappingScheduleCount
                    incrementPrevExactCount(index - 1)
                } else if (isOverlapping(current, it)) {
                    current.overlappingScheduleCount = it.overlappingScheduleCount + 1
                }
            }
            prev = current
        }

        allSchedule.forEach {
            val overlapShiftTotal = ViewUtil.dpToPx(
                mApplication,
                (it.overlappingScheduleCount * SkripsiConstant.OVERLAP_SHIFT).toDouble()
            )
            if (it.exactScheduleCount > 0) {
                it.width = (blockWidth - overlapShiftTotal) / (it.exactScheduleCount + 1)
                it.marginStart = overlapShiftTotal + (it.exactScheduleOrder * it.width)
            } else {
                it.width = blockWidth - overlapShiftTotal
                it.marginStart = overlapShiftTotal
            }

            it.height = ViewUtil.dpToPx(
                mApplication,
                (it.endMinute - it.startMinute) * SkripsiConstant.MINUTE_HEIGHT
            )
            it.positionX =
                ViewUtil.dpToPx(mApplication, SkripsiConstant.MINUTE_HEIGHT * it.startMinute)
        }

        allSchedule

    }

    fun incrementPrevExactCount(index: Int) {
        if (allSchedule[index].exactScheduleCount > 0 && index > 0) incrementPrevExactCount(index - 1)
        allSchedule[index].exactScheduleCount++
    }

    fun isOverlapping(item1: ScheduleBlockViewModel, item2: ScheduleBlockViewModel) =
        item1.startMinute < item2.endMinute && item1.endMinute > item2.startMinute
}
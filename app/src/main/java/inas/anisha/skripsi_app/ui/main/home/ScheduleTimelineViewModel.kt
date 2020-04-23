package inas.anisha.skripsi_app.ui.main.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ScheduleTimelineViewModel : ViewModel() {
    var isStartIncludeCurrentSchedule: MutableLiveData<Boolean> = MutableLiveData(false)
    var isEndIncludeCurrentSchedule: MutableLiveData<Boolean> = MutableLiveData(false)
    var isLastSchedule: MutableLiveData<Boolean> = MutableLiveData(false)
    var hasImmediateSchedule: MutableLiveData<Boolean> = MutableLiveData(false)

    var id: Long = 0
    var name: String = ""
    var startMinute: Int = 0
    var endMinute: Int = 0
    var startTimeText: String = ""
    var endTimeText: String = ""
}

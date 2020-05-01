package inas.anisha.skripsi_app.ui.updatetarget

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import inas.anisha.skripsi_app.constant.SkripsiConstant
import inas.anisha.skripsi_app.data.Repository
import inas.anisha.skripsi_app.data.db.entity.CycleEntity
import inas.anisha.skripsi_app.data.db.entity.TargetUtamaEntity
import inas.anisha.skripsi_app.ui.kelolapembelajaran.targetpendukung.TargetPendukungViewModel
import inas.anisha.skripsi_app.ui.kelolapembelajaran.targetutama.TargetUtamaViewModel
import inas.anisha.skripsi_app.utils.CalendarUtil.Companion.toDateString
import java.util.*

class UpdateTargetViewModel(application: Application) : AndroidViewModel(application) {

    private val mRepository = Repository.getInstance(application)

    var mainTarget: TargetUtamaViewModel = TargetUtamaViewModel()
    var supportingTargets: MutableList<TargetPendukungViewModel> = mutableListOf()

    var cycleNumber: Int = 0
    var frequency: Int = SkripsiConstant.CYCLE_FREQUENCY_DAILY
    var duration: Int = 1
    var evaluationDate: Calendar = Calendar.getInstance()

    var cycleTimeString: MutableLiveData<String> = MutableLiveData("")
    var evaluationDateString: MutableLiveData<String> = MutableLiveData("")

    fun getMainTarget(): LiveData<TargetUtamaEntity> = mRepository.getMainTarget()
    fun saveMainTarget(target: TargetUtamaViewModel) {
        mainTarget = target
        mRepository.setMainTarget(mainTarget.toEntity())
    }

    fun getCycleCount(): LiveData<Int> = mRepository.getCycleCount()

    fun getCycleTime() = mRepository.getCycleTime()
    fun setCycleTime(cycleTime: Pair<Int, Int>) {
        frequency = cycleTime.first
        duration = cycleTime.second
        setEvaluationDate(frequency, duration)

        val frequencyString = SkripsiConstant.getCycleFrequencyString(frequency)
        val durationString = if (duration == 1) "" else ("$duration ")
        cycleTimeString.value = durationString + frequencyString
    }

    fun setEvaluationDate(newFrequency: Int, newDuration: Int) {
        val amount = when (newFrequency) {
            SkripsiConstant.CYCLE_FREQUENCY_DAILY -> Calendar.DATE
            SkripsiConstant.CYCLE_FREQUENCY_WEEKLY -> Calendar.WEEK_OF_YEAR
            else -> Calendar.MONTH
        }

        frequency = newFrequency
        duration = newDuration
        evaluationDate = Calendar.getInstance().apply { add(amount, newDuration) }

        evaluationDateString.value = "Evaluasi berikutnya: " + evaluationDate.toDateString()
    }

    fun addNewCycle() = mRepository.addCycle(CycleEntity(0, cycleNumber, 0))

    fun getSupportingTargets(): LiveData<List<TargetPendukungViewModel>> {
        return Transformations.map(mRepository.getSupportingTargets()) { data ->
            data.map { TargetPendukungViewModel().fromEntity(it) }
        }
    }

    fun addOrUpdateSupportingTarget(target: TargetPendukungViewModel) =
        mRepository.addSupportingTarget(target.toEntity())

}
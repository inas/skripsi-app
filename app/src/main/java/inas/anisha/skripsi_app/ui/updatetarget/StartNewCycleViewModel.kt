package inas.anisha.skripsi_app.ui.updatetarget

import android.app.AlarmManager
import android.app.Application
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import inas.anisha.skripsi_app.constant.SkripsiConstant
import inas.anisha.skripsi_app.data.Repository
import inas.anisha.skripsi_app.data.db.entity.CycleEntity
import inas.anisha.skripsi_app.data.db.entity.TargetUtamaEntity
import inas.anisha.skripsi_app.ui.common.AlarmReceiver
import inas.anisha.skripsi_app.ui.kelolapembelajaran.targetpendukung.TargetPendukungViewModel
import inas.anisha.skripsi_app.ui.kelolapembelajaran.targetutama.TargetUtamaViewModel
import inas.anisha.skripsi_app.utils.CalendarUtil.Companion.getPreviousMidnight
import inas.anisha.skripsi_app.utils.CalendarUtil.Companion.toDateString
import java.util.*

class StartNewCycleViewModel(val mApplication: Application) : AndroidViewModel(mApplication) {

    private val mRepository = Repository.getInstance(mApplication)

    var mainTarget: TargetUtamaViewModel = TargetUtamaViewModel()
    var supportingTargets: MutableList<TargetPendukungViewModel> = mutableListOf()

    var cycleNumber: Int = 0
    var frequency: Int = SkripsiConstant.CYCLE_FREQUENCY_DAILY
    var duration: Int = 1
    var evaluationDate: Calendar = Calendar.getInstance()

    var cycleTimeString: MutableLiveData<String> = MutableLiveData("")
    var evaluationDateString: MutableLiveData<String> = MutableLiveData("")
    var shouldShowSupportingTargets: MutableLiveData<Boolean> = MutableLiveData(false)

    fun getMainTarget(): LiveData<TargetUtamaEntity> = mRepository.getMainTarget()
    fun saveMainTarget() {
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

    fun addNewCycle() {
        scheduleNotification(mApplication, evaluationDate.getPreviousMidnight())
        setEvaluationDate(frequency, duration)
        mRepository.setCycleStartDate(Calendar.getInstance().timeInMillis)
        mRepository.setEvaluationDate(evaluationDate.timeInMillis)
        mRepository.setCycleTime(Pair(frequency, duration))
        mRepository.addCycle(CycleEntity(0, cycleNumber + 1, 0, ""))
    }

    fun getSupportingTargets(): LiveData<List<TargetPendukungViewModel>> {
        return Transformations.map(mRepository.getSupportingTargets()) { data ->
            data.map {
                TargetPendukungViewModel().fromEntity(it).apply {
                    isCompleted = false
                }
            }
        }
    }

    fun replaceSupportingTarget() =
        mRepository.replaceSupportingTarget(*supportingTargets.map { it.toEntity() }.toTypedArray())

    fun startNewCycle() {
        addNewCycle()
        saveMainTarget()
        replaceSupportingTarget()
        mRepository.setShouldShowEvaluationReport(true)
        mRepository.setShouldShowEndOfCycleWarning(true)
    }

    fun scheduleNotification(context: Context, evaluationDate: Calendar) {
        val reminderTime = evaluationDate.apply { add(Calendar.HOUR_OF_DAY, -5) }

        if (reminderTime > Calendar.getInstance()) {
            val alarmManager: AlarmManager =
                context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            val pendingIntent = createPendingIntent(context)
            alarmManager.set(AlarmManager.RTC_WAKEUP, reminderTime.timeInMillis, pendingIntent)
        }
    }

    private fun createPendingIntent(context: Context): PendingIntent {

        val intent = Intent(context, AlarmReceiver::class.java)
        intent.action = AlarmReceiver.ACTION_NOTIFICATION
        intent.putExtra(
            AlarmReceiver.EXTRA_TITLE,
            "Besok siklus ke " + (cycleNumber + 1) + " akan berakhir"
        )
        intent.putExtra(
            AlarmReceiver.EXTRA_CONTENT,
            "Jangan lupa untuk menandai target yang sudah kamu capai"
        )

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            AlarmReceiver.CYCLE_NOTIFICATION,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
        return pendingIntent
    }

}
package inas.anisha.skripsi_app.ui.kelolapembelajaran

import android.app.AlarmManager
import android.app.Application
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import inas.anisha.skripsi_app.constant.SkripsiConstant
import inas.anisha.skripsi_app.data.Repository
import inas.anisha.skripsi_app.data.db.entity.CycleEntity
import inas.anisha.skripsi_app.data.db.entity.TargetPendukungEntity
import inas.anisha.skripsi_app.data.db.entity.TargetUtamaEntity
import inas.anisha.skripsi_app.ui.common.AlarmReceiver
import inas.anisha.skripsi_app.ui.kelolapembelajaran.targetpendukung.TargetPendukungViewModel
import inas.anisha.skripsi_app.ui.kelolapembelajaran.targetutama.TargetUtamaViewModel
import inas.anisha.skripsi_app.utils.CalendarUtil.Companion.getPreviousMidnight
import java.util.*

class KelolaPembelajaranViewModel(val mApplication: Application) : AndroidViewModel(mApplication) {

    var mRepository: Repository = Repository.getInstance(mApplication)

    var mainTarget: TargetUtamaViewModel = TargetUtamaViewModel()
    var cycleTime: Pair<Int, Int> = Pair(0, 0)
    var supportingTargets: MutableList<TargetPendukungViewModel> = mutableListOf()

    var page: MutableLiveData<Int> = MutableLiveData(0)

    fun saveDataToRepository() {
        val mainTargetDataModel =
            TargetUtamaEntity(0, mainTarget.name, mainTarget.note, mainTarget.date)

        val supportingTargetDataModels = mutableListOf<TargetPendukungEntity>()
        supportingTargets.forEach {
            supportingTargetDataModels.add(TargetPendukungEntity(0, it.name, it.note, it.time))
        }

        mRepository.setShouldNotShowKelolaPembelajaran()
        mRepository.setMainTarget(mainTargetDataModel)
        mRepository.addSupportingTarget(*supportingTargetDataModels.toTypedArray())
        mRepository.setCycleTime(cycleTime)

        val evaluationDate = calculateDate(cycleTime)
        mRepository.setEvaluationDate(evaluationDate.timeInMillis)
        scheduleNotification(mApplication, evaluationDate.getPreviousMidnight())
        mRepository.setCycleStartDate(Calendar.getInstance().timeInMillis)

        val targetCompletion = if (supportingTargetDataModels.size == 0) 100 else 0
        mRepository.addCycle(CycleEntity(0, 1, targetCompletion))
    }

    fun calculateDate(cycleTime: Pair<Int, Int>): Calendar {
        val amount = when (cycleTime.first) {
            SkripsiConstant.CYCLE_FREQUENCY_DAILY -> Calendar.DATE
            SkripsiConstant.CYCLE_FREQUENCY_WEEKLY -> Calendar.WEEK_OF_YEAR
            else -> Calendar.MONTH
        }

        return Calendar.getInstance().apply { add(amount, cycleTime.second) }
    }

    fun scheduleNotification(context: Context, evaluationDate: Calendar) {
        val reminderTime = evaluationDate.apply { add(Calendar.HOUR_OF_DAY, -7) }

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
        intent.putExtra(AlarmReceiver.EXTRA_TITLE, "Besok siklus ke 1 akan berakhir")
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
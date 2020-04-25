package inas.anisha.skripsi_app.data

import android.app.Application
import androidx.lifecycle.LiveData
import inas.anisha.skripsi_app.constant.SkripsiConstant
import inas.anisha.skripsi_app.data.db.AppDatabase
import inas.anisha.skripsi_app.data.db.dao.*
import inas.anisha.skripsi_app.data.db.entity.*
import inas.anisha.skripsi_app.utils.CalendarUtil.Companion.toMinuteOfDay
import inas.anisha.skripsi_app.utils.CalendarUtil.Companion.toPreviousMidnight
import io.reactivex.Observable
import io.reactivex.functions.BiFunction
import io.reactivex.schedulers.Schedulers
import java.util.*

class Repository(application: Application) {

    var sharedPreference: AppPreference
    var targetUtamaDao: TargetUtamaDao
    var targetPendukungDao: TargetPendukungDao
    var cycleDao: CycleDao
    var schoolClassDao: SchoolClassDao
    var scheduleDao: ScheduleDao

    init {
        sharedPreference = AppPreference.getInstance(application)

        val db = AppDatabase.getDatabase(application)
        targetUtamaDao = db.targetUtamaDao()
        targetPendukungDao = db.targetPendukungDao()
        cycleDao = db.cycleDao()
        schoolClassDao = db.schoolClassDao()
        scheduleDao = db.scheduleDao()
    }

    // region shared preferenceex
    fun shouldShowKelolaPembelajaran() = sharedPreference.shouldShowKelolaPembelajaran()
    fun setShouldNotShowKelolaPembelajaran() = sharedPreference.setShouldNotShowKelolaPembelajaran()

    fun getMainTarget() = targetUtamaDao.getTarget()

    fun setMainTarget(target: TargetUtamaEntity) {
        Observable.fromCallable {
            targetUtamaDao.deleteOldTarget()
            targetUtamaDao.add(target)
        }.subscribeOn(Schedulers.io()).subscribe()

    }

    fun getSupportingTarget(targetId: Long): LiveData<TargetPendukungEntity> =
        targetPendukungDao.get(targetId)

    fun getSupportingTargets(): LiveData<List<TargetPendukungEntity>> = targetPendukungDao.getAll()
    fun getSupportingTargetsByCompleteness(isCompleted: Boolean): LiveData<List<TargetPendukungEntity>> =
        targetPendukungDao.getByCompleteness(isCompleted)

    fun deleteSupportingTargets(targetId: Long) {
        Observable.fromCallable { targetPendukungDao.delete(targetId) }
            .subscribeOn(Schedulers.io()).subscribe()
    }

    fun addSupportingTarget(vararg target: TargetPendukungEntity) {
        Observable.fromCallable { targetPendukungDao.add(*target) }
            .subscribeOn(Schedulers.io()).subscribe()
    }

    fun updateSupportingTarget(target: TargetPendukungEntity) =
        Observable.fromCallable { targetPendukungDao.update(target) }
            .subscribeOn(Schedulers.io()).subscribe()

    fun getCurrentCycle(): LiveData<CycleEntity> = cycleDao.getLatest()
    fun getCycles(): LiveData<List<CycleEntity>> = cycleDao.getAll()
    fun addCycle(cycle: CycleEntity) =
        Observable.fromCallable { cycleDao.add(cycle) }
            .subscribeOn(Schedulers.io()).subscribe()

    fun updateCurrentCycleCompleteness(percentage: Int) =
        Observable.fromCallable { cycleDao.updateCurrentCycleCompleteness(percentage) }
            .subscribeOn(Schedulers.io()).subscribe()

    fun getCycleTime() = sharedPreference.getCycleTime()
    fun setCycleTime(cycleTime: Pair<Int, Int>) = sharedPreference.setCycleTime(cycleTime)

    fun getEvaluationDate(): Long = sharedPreference.getEvaluationDate()
    fun setEvaluationDate(evaluationDate: Long) = sharedPreference.setEvaluationDate(evaluationDate)

    fun getUserName(): String = sharedPreference.getUserName()
    fun setUserName(name: String) = sharedPreference.setUserName(name)

    fun getUserGrade(): String = sharedPreference.getUserGrade()
    fun setUserGrade(grade: String) = sharedPreference.setUserGrade(grade)

    fun getUserStudy(): String = sharedPreference.getUserStudy()
    fun setUserStudy(study: String) = sharedPreference.setUserStudy(study)

    fun getSchoolClasses(dayOfWeek: Int): LiveData<List<SchoolClassEntity>> =
        schoolClassDao.getAll(dayOfWeek)

    fun getSchoolClassesSorted(dayOfWeek: Int): LiveData<List<SchoolClassEntity>> =
        schoolClassDao.getAllSorted(dayOfWeek)

    fun getSchoolCount(dayOfWeek: Int): LiveData<Int> = schoolClassDao.getCount(dayOfWeek)

    fun getTasks(): LiveData<List<ScheduleEntity>> =
        scheduleDao.getAll(SkripsiConstant.SCHEDULE_TYPE_TASK)

    fun getCurrentCycleTasks(): LiveData<List<ScheduleEntity>> =
        scheduleDao.getAll(
            SkripsiConstant.SCHEDULE_TYPE_TASK,
            Calendar.getInstance().apply { timeInMillis = getEvaluationDate() }.toPreviousMidnight()
        )

    fun getSchedule(scheduleId: Long): LiveData<ScheduleEntity> =
        scheduleDao.get(scheduleId)

    fun getOverlappingEntity(
        start: Calendar,
        end: Calendar,
        scheduleId: Long,
        classId: Long
    ): Observable<Pair<List<ScheduleEntity>, List<SchoolClassEntity>>> {
        return Observable.zip(
            Observable.fromCallable {
                (scheduleDao.getOverlappingEntity(
                    SkripsiConstant.SCHEDULE_TYPE_ACTIVITY,
                    start,
                    end,
                    scheduleId
                ))
            }.subscribeOn(Schedulers.io()),
            Observable.fromCallable {
                (schoolClassDao.getOverlappingEntity(
                    start.get(Calendar.DAY_OF_WEEK),
                    start.toMinuteOfDay(),
                    end.toMinuteOfDay(),
                    classId
                ))
            }.subscribeOn(Schedulers.io()),
            BiFunction { schedule: List<ScheduleEntity>, schoolClass: List<SchoolClassEntity> ->
                Pair(schedule, schoolClass)
            }
        )
    }

    fun getSchedule(start: Calendar, end: Calendar): LiveData<List<ScheduleEntity>> =
        scheduleDao.getAll(start, end)

    fun getScheduleSorted(start: Calendar, end: Calendar): LiveData<List<ScheduleEntity>> =
        scheduleDao.getAllSorted(start, end)

    fun addSchedule(schedule: ScheduleEntity) =
        Observable.fromCallable { scheduleDao.add(schedule) }
            .subscribeOn(Schedulers.io()).subscribe()

    fun updateSchedule(schedule: ScheduleEntity) =
        Observable.fromCallable { scheduleDao.update(schedule) }
            .subscribeOn(Schedulers.io()).subscribe()

    fun updateScheduleAsComplete(scheduleId: Long, isCompleted: Boolean) =
        Observable.fromCallable { scheduleDao.updateCompleteness(scheduleId, isCompleted) }
            .subscribeOn(Schedulers.io()).subscribe()

    fun updateScheduleOnTimeStatus(scheduleId: Long, isOnTime: Boolean) =
        Observable.fromCallable { scheduleDao.updateOnTimeStatus(scheduleId, isOnTime) }
            .subscribeOn(Schedulers.io()).subscribe()

    fun deleteSchedule(scheduleId: Long) =
        Observable.fromCallable { scheduleDao.delete(scheduleId) }
            .subscribeOn(Schedulers.io()).subscribe()


    companion object {
        // For Singleton instantiation
        @Volatile
        private var instance: Repository? = null

        fun getInstance(app: Application) =
            instance ?: synchronized(this) {
                instance ?: Repository(app).also { instance = it }
            }
    }

    // DEBUG ONLY
    fun prepopulate() {
        Observable.fromCallable { scheduleDao.deleteAll() }
            .subscribeOn(Schedulers.io()).subscribe {
                Observable.fromCallable { scheduleDao.add(*MockData.getSchedules().toTypedArray()) }
                    .subscribeOn(Schedulers.io()).subscribe()
            }

        Observable.fromCallable { schoolClassDao.deleteAll() }
            .subscribeOn(Schedulers.io()).subscribe {
                Observable.fromCallable {
                    schoolClassDao.add(
                        *MockData.getSchoolClasses().toTypedArray()
                    )
                }
                    .subscribeOn(Schedulers.io()).subscribe()
            }

        Observable.fromCallable { cycleDao.deleteAll() }
            .subscribeOn(Schedulers.io()).subscribe {
                Observable.fromCallable { cycleDao.add(*MockData.getCycles().toTypedArray()) }
                    .subscribeOn(Schedulers.io()).subscribe()
            }

        Observable.fromCallable { targetUtamaDao.deleteOldTarget() }
            .subscribeOn(Schedulers.io()).subscribe {
                Observable.fromCallable {
                    targetUtamaDao.add(
                        TargetUtamaEntity(
                            0,
                            "Masuk ke fasilkom ui yang terbaik",
                            "hehehhe senengnyaa kalo bisa masuk uwuwuwuwuwuwu",
                            Calendar.getInstance().apply { set(2020, 6, 5) })
                    )
                }.subscribeOn(Schedulers.io()).subscribe()
            }

        Observable.fromCallable { targetPendukungDao.deleteAll() }
            .subscribeOn(Schedulers.io()).subscribe {
                Observable.fromCallable {
                    targetPendukungDao.add(
                        *MockData.getSupportingTargets().toTypedArray()
                    )
                }.subscribeOn(Schedulers.io()).subscribe()
            }

        sharedPreference.setShouldNotShowKelolaPembelajaran()
        sharedPreference.setEvaluationDate(
            Calendar.getInstance().apply { add(Calendar.DATE, 3) }.timeInMillis
        )
        sharedPreference.setCycleTime(Pair(SkripsiConstant.CYCLE_FREQUENCY_DAILY, 3))
        sharedPreference.setUserName("Inas")
        sharedPreference.setUserGrade("11")
        sharedPreference.setUserStudy("Ilmu Komputer")
    }

}
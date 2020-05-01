package inas.anisha.skripsi_app.data

import android.app.Application
import androidx.lifecycle.LiveData
import inas.anisha.skripsi_app.constant.SkripsiConstant
import inas.anisha.skripsi_app.data.db.AppDatabase
import inas.anisha.skripsi_app.data.db.dao.*
import inas.anisha.skripsi_app.data.db.entity.*
import inas.anisha.skripsi_app.utils.CalendarUtil.Companion.getNextMidnight
import inas.anisha.skripsi_app.utils.CalendarUtil.Companion.getPreviousMidnight
import inas.anisha.skripsi_app.utils.CalendarUtil.Companion.toMinuteOfDay
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

    // region shared preference
    fun shouldShowKelolaPembelajaran() = sharedPreference.shouldShowKelolaPembelajaran()
    fun setShouldNotShowKelolaPembelajaran() = sharedPreference.setShouldNotShowKelolaPembelajaran()

    fun shouldShowSchoolScheduleDialog() = sharedPreference.shouldShowSchoolScheduleDialog()
    fun setShouldNotShowSchoolScheduleDialog() =
        sharedPreference.setShouldNotShowSchoolScheduleDialog()

    fun shouldShowEndOfCycleWarning() = sharedPreference.shouldShowEndOfCycleWarning()
    fun setShouldShowEndOfCycleWarning(shouldShow: Boolean) =
        sharedPreference.setShouldShowEndOfCycleWarning(shouldShow)

    fun shouldShowEvaluationReport() = sharedPreference.shouldShowEvaluationReport()
    fun setShouldShowEvaluationReport(shouldShow: Boolean) =
        sharedPreference.setShouldShowEvaluationReport(shouldShow)

    fun getCycleTime() = sharedPreference.getCycleTime()
    fun setCycleTime(cycleTime: Pair<Int, Int>) = sharedPreference.setCycleTime(cycleTime)

    fun getEvaluationDate(): Long = sharedPreference.getEvaluationDate()
    fun setEvaluationDate(evaluationDate: Long) = sharedPreference.setEvaluationDate(evaluationDate)

    fun getCycleStartDate(): Long = sharedPreference.getCycleStartDate()
    fun setCycleStartDate(startDate: Long) = sharedPreference.setCycleStartDate(startDate)

    fun getUserName(): String = sharedPreference.getUserName()
    fun setUserName(name: String) = sharedPreference.setUserName(name)

    fun getUserGrade(): String = sharedPreference.getUserGrade()
    fun setUserGrade(grade: String) = sharedPreference.setUserGrade(grade)

    fun getUserStudy(): String = sharedPreference.getUserStudy()
    fun setUserStudy(study: String) = sharedPreference.setUserStudy(study)
    // end region


    // region main target
    fun getMainTarget() = targetUtamaDao.getTarget()

    fun setMainTarget(target: TargetUtamaEntity) {
        Observable.fromCallable { targetUtamaDao.deleteOldTarget() }
            .subscribeOn(Schedulers.io())
            .subscribe { targetUtamaDao.add(target) }
    }
    // end region


    // region supporting target
    fun getSupportingTarget(targetId: Long): LiveData<TargetPendukungEntity> =
        targetPendukungDao.get(targetId)

    fun getSupportingTargets(): LiveData<List<TargetPendukungEntity>> = targetPendukungDao.getAll()

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

    fun replaceSupportingTarget(vararg target: TargetPendukungEntity) {
        Observable.fromCallable { targetPendukungDao.deleteAll() }
            .subscribeOn(Schedulers.io())
            .subscribe { targetPendukungDao.add(*target) }
    }
    // end region


    // region cycle
    fun getCurrentCycle(): Observable<CycleEntity> =
        Observable.fromCallable { cycleDao.getLatest() }.subscribeOn(Schedulers.io())

    fun updateCycle(cycle: CycleEntity) =
        Observable.fromCallable { cycleDao.updateCycle(cycle) }.subscribeOn(Schedulers.io())
            .subscribe()

    fun getCycles(): LiveData<List<CycleEntity>> = cycleDao.getAll()
    fun getCycleCount(): LiveData<Int> = cycleDao.getCount()

    fun addCycle(cycle: CycleEntity) =
        Observable.fromCallable { cycleDao.add(cycle) }
            .subscribeOn(Schedulers.io()).subscribe()

    fun updateCurrentCycleCompleteness(percentage: Int) =
        Observable.fromCallable { cycleDao.updateCurrentCycleCompleteness(percentage) }
            .subscribeOn(Schedulers.io()).subscribe()
    // end region


    // region schedule
    fun addSchoolClass(schoolClass: SchoolClassEntity) =
        Observable.fromCallable { schoolClassDao.add(schoolClass) }
            .subscribeOn(Schedulers.io()).subscribe()

    fun getSchoolClass(classId: Long): LiveData<SchoolClassEntity> =
        schoolClassDao.get(classId)

    fun getSchoolClasses(dayOfWeek: Int): LiveData<List<SchoolClassEntity>> =
        schoolClassDao.getAll(dayOfWeek)

    fun getSchoolClassesSorted(dayOfWeek: Int): LiveData<List<SchoolClassEntity>> =
        schoolClassDao.getAllSorted(dayOfWeek)

    fun updateSchoolClass(schoolClass: SchoolClassEntity) =
        Observable.fromCallable { schoolClassDao.update(schoolClass) }
            .subscribeOn(Schedulers.io()).subscribe()

    fun deleteSchoolClass(classId: Long) =
        Observable.fromCallable { schoolClassDao.delete(classId) }
            .subscribeOn(Schedulers.io()).subscribe()

    fun getSchoolCount(dayOfWeek: Int): LiveData<Int> = schoolClassDao.getCount(dayOfWeek)

    fun getOverlappingClass(
        start: Calendar,
        end: Calendar,
        classId: Long
    ): Observable<List<SchoolClassEntity>> {
        return Observable.fromCallable {
            (schoolClassDao.getOverlappingEntity(
                start.get(Calendar.DAY_OF_WEEK),
                start.toMinuteOfDay(),
                end.toMinuteOfDay(),
                classId
            ))
        }.subscribeOn(Schedulers.io())
    }

    fun getTasks(dateLimit: Calendar, isUpcomingTasks: Boolean): LiveData<List<ScheduleEntity>> {
        return if (isUpcomingTasks) {
            scheduleDao.getAllAfterDate(SkripsiConstant.SCHEDULE_TYPE_TASK, dateLimit)
        } else {
            scheduleDao.getAllBeforeDate(SkripsiConstant.SCHEDULE_TYPE_TASK, dateLimit)
        }
    }

    fun getCurrentCycleTasks(): LiveData<List<ScheduleEntity>> =
        scheduleDao.getAll(
            Calendar.getInstance().apply { timeInMillis = getCycleStartDate() }
                .getPreviousMidnight(),
            Calendar.getInstance().apply {
                timeInMillis = getEvaluationDate()
                add(Calendar.DAY_OF_MONTH, -1)
            }.getNextMidnight(),
            SkripsiConstant.SCHEDULE_TYPE_TASK
        )

    fun getSchedule(scheduleId: Long): LiveData<ScheduleEntity> =
        scheduleDao.get(scheduleId)

    fun getOverlappingScheduleAndSchoolEntity(
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

    fun getScheduleSorted(type: Int): LiveData<List<ScheduleEntity>> =
        scheduleDao.getAllSorted(type)

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
    // end schedule

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
        sharedPreference.setCycleStartDate(Calendar.getInstance().timeInMillis)
        sharedPreference.setEvaluationDate(
            Calendar.getInstance().apply { add(Calendar.DATE, 3) }.timeInMillis
        )
        sharedPreference.setCycleTime(Pair(SkripsiConstant.CYCLE_FREQUENCY_DAILY, 3))
        sharedPreference.setUserName("Inas")
        sharedPreference.setUserGrade("11")
        sharedPreference.setUserStudy("Ilmu Komputer")
    }

}
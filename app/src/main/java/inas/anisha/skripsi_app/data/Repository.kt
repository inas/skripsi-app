package inas.anisha.skripsi_app.data

import android.app.Application
import androidx.lifecycle.LiveData
import inas.anisha.skripsi_app.data.db.AppDatabase
import inas.anisha.skripsi_app.data.db.dao.CycleDao
import inas.anisha.skripsi_app.data.db.dao.TargetPendukungDao
import inas.anisha.skripsi_app.data.db.dao.TargetUtamaDao
import inas.anisha.skripsi_app.data.db.entity.CycleEntity
import inas.anisha.skripsi_app.data.db.entity.TargetPendukungEntity
import inas.anisha.skripsi_app.data.db.entity.TargetUtamaEntity
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers

class Repository(application: Application) {

    var sharedPreference: AppPreference
    var targetUtamaDao: TargetUtamaDao
    var targetPendukungDao: TargetPendukungDao
    var cycleDao: CycleDao

    init {
        sharedPreference = AppPreference.getInstance(application)

        val db = AppDatabase.getDatabase(application)
        targetUtamaDao = db.targetUtamaDao()
        targetPendukungDao = db.targetPendukungDao()
        cycleDao = db.cycleDao()
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

    fun deleteSupportingTargets(targetId: Long) {
        Observable.fromCallable { targetPendukungDao.delete(targetId) }
            .subscribeOn(Schedulers.io()).subscribe()
    }

    fun addSupportingTarget(vararg target: TargetPendukungEntity) {
        Observable.fromCallable { targetPendukungDao.add(*target) }
            .subscribeOn(Schedulers.io()).subscribe()
    }

    fun updateSupportingTarget(
        id: Long,
        name: String,
        note: String,
        time: String,
        isCompleted: Boolean
    ) {
        Observable.fromCallable {
            targetPendukungDao.update(id, name, note, time, isCompleted)
        }.subscribeOn(Schedulers.io()).subscribe()
    }

    fun getCurrentCycle(): LiveData<CycleEntity> = cycleDao.getLatest()
    fun getAllCycle(): LiveData<List<CycleEntity>> = cycleDao.getAll()
    fun addCycle(cycle: CycleEntity) =
        Observable.fromCallable { cycleDao.add(cycle) }.subscribeOn(Schedulers.io()).subscribe()

    fun getCycleTime() = sharedPreference.getCycleTime()
    fun setCycleTime(cycleTime: Pair<Int, Int>) {
        sharedPreference.setCycleTime(cycleTime)
    }

    fun getEvaluationDate() = sharedPreference.getEvaluationDate()
    fun setEvaluationDate(evaluationDate: Long) = sharedPreference.setEvaluationDate(evaluationDate)

    fun getUserName(): String = sharedPreference.getUserName()
    fun setUserName(name: String) = sharedPreference.setUserName(name)

    fun getUserGrade(): String = sharedPreference.getUserGrade()
    fun setUserGrade(grade: String) = sharedPreference.setUserGrade(grade)

    fun getUserStudy(): String = sharedPreference.getUserStudy()
    fun setUserStudy(study: String) = sharedPreference.setUserStudy(study)

    companion object {
        // For Singleton instantiation
        @Volatile
        private var instance: Repository? = null

        fun getInstance(app: Application) =
            instance ?: synchronized(this) {
                instance ?: Repository(app).also { instance = it }
            }
    }
}
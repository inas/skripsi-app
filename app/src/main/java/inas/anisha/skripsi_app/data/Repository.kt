package inas.anisha.skripsi_app.data

import android.app.Application
import inas.anisha.skripsi_app.data.db.AppDatabase
import inas.anisha.skripsi_app.data.db.dao.TargetPendukungDao
import inas.anisha.skripsi_app.data.db.dao.TargetUtamaDao
import inas.anisha.skripsi_app.data.db.entity.TargetPendukungEntity
import inas.anisha.skripsi_app.data.db.entity.TargetUtamaEntity
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers

class Repository(application: Application) {

    var sharedPreference: AppPreference
    var targetUtamaDao: TargetUtamaDao
    var targetPendukungDao: TargetPendukungDao

    init {
        sharedPreference = AppPreference.getInstance(application)

        val db = AppDatabase.getDatabase(application)
        targetUtamaDao = db.targetUtamaDao()
        targetPendukungDao = db.targetPendukungDao()
    }

    // region shared preferenceex
    fun shouldShowKelolaPembelajaran() = sharedPreference.shouldShowKelolaPembelajaran()
    fun setShouldNotShowKelolaPembelajaran(): Boolean =
        sharedPreference.setShouldNotShowKelolaPembelajaran()

    fun setMainTarget(target: TargetUtamaEntity) {
        Observable.fromCallable { targetUtamaDao.add(target) }
            .subscribeOn(Schedulers.io()).subscribe()
    }

    fun setSupportingTargets(target: MutableList<TargetPendukungEntity>) {
        Observable.fromCallable { targetPendukungDao.add(*target.toTypedArray()) }
            .subscribeOn(Schedulers.io()).subscribe()
    }

    fun getCycleTime() = sharedPreference.getCycleTime()
    fun setCycleTime(cycleTime: Pair<Int, Int>) {
        sharedPreference.setCycleTime(cycleTime)
    }

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
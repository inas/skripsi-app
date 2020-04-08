package inas.anisha.skripsi_app.data

import android.app.Application

class Repository(application: Application) {

    var sharedPreference: AppPreference

    init {
        sharedPreference = AppPreference.getInstance(application)
    }

    // region shared preferencee
    fun shouldShowOnBoarding() = sharedPreference.shouldShowOnboarding
    fun setShouldNotShowOnBoarding() = sharedPreference.setShouldNotShowOnboarding()

    fun shouldShowKelolaPembelajaran() = sharedPreference.shouldShowKelolaPembelajaran
    fun setShouldNotShowKelolaPembelajaran() = sharedPreference.setShouldNotShowKelolaPembelajaran()

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
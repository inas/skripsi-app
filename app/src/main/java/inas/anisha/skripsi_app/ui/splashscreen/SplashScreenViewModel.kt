package inas.anisha.skripsi_app.ui.splashscreen

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import inas.anisha.skripsi_app.data.Repository

class SplashScreenViewModel(application: Application) : AndroidViewModel(application) {
    var mRepository: Repository = Repository.getInstance(application)

    fun shouldShowKelolaPembelajaran() = mRepository.shouldShowKelolaPembelajaran()
}
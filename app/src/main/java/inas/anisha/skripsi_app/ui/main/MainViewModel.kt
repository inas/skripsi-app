package inas.anisha.skripsi_app.ui.main

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import inas.anisha.skripsi_app.data.Repository

class MainViewModel(application: Application) : AndroidViewModel(application) {
    var mRepository: Repository = Repository.getInstance(application)

    fun shouldShowKelolaPembelajaran() = mRepository.shouldShowKelolaPembelajaran()
    fun getTimeCycle() {
        val first = mRepository.getCycleTime().first
        val second = mRepository.getCycleTime().second
        val yes = shouldShowKelolaPembelajaran()
        first
        second
    }
}
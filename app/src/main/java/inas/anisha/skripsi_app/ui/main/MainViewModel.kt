package inas.anisha.skripsi_app.ui.main

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import inas.anisha.skripsi_app.data.Repository

class MainViewModel(application: Application) : AndroidViewModel(application) {
    var mRepository: Repository = Repository.getInstance(application)

    fun shouldShowOnboardingScreen() = mRepository.shouldShowOnBoarding()

}
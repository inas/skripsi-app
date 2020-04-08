package inas.anisha.skripsi_app.ui.onboarding

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import inas.anisha.skripsi_app.data.Repository

class OnboardingViewModel(application: Application) : AndroidViewModel(application) {
    var mRepository: Repository = Repository.getInstance(application)

    fun setShouldNotShowOnboarding() {
        mRepository.setShouldNotShowOnBoarding()
    }
}
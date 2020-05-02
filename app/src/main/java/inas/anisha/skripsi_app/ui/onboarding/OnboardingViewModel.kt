package inas.anisha.skripsi_app.ui.onboarding

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class OnboardingViewModel : ViewModel() {
    var pageNum: MutableLiveData<Int> = MutableLiveData(0)
}
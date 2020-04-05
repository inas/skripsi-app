package inas.anisha.skripsi_app.ui.onboarding

import android.app.Application
import androidx.lifecycle.AndroidViewModel

class OnboardingPagerViewModel(application: Application) : AndroidViewModel(application) {

    var title: String = ""
    var description: String = ""

    fun initViewModel(title: String, description: String) {
        this.title = title
        this.description = description
    }
}
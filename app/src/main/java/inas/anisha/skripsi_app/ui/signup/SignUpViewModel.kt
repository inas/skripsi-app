package inas.anisha.skripsi_app.ui.signup

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import inas.anisha.skripsi_app.data.Repository

class SignUpViewModel(application: Application) : AndroidViewModel(application) {
    val mRepository = Repository(application)

    fun saveData(name: String, grade: String, study: String) {
        mRepository.setUserName(name)
        mRepository.setUserGrade(grade)
        mRepository.setUserStudy(study)
    }
}
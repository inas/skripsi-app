package inas.anisha.srl_app.ui.signup

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import inas.anisha.srl_app.data.Repository

class SignUpViewModel(application: Application) : AndroidViewModel(application) {
    private val mRepository = Repository.getInstance(application)

    fun saveData(name: String, grade: String, study: String) {
        mRepository.setUserName(name)
        mRepository.setUserGrade(grade)
        mRepository.setUserStudy(study)
    }
}
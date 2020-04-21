package inas.anisha.skripsi_app.ui.main

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import inas.anisha.skripsi_app.data.Repository

class MainViewModel(application: Application) : AndroidViewModel(application) {
    var mRepository: Repository = Repository.getInstance(application)

    fun checkname() {
        val name = mRepository.getUserName()
        val grade = mRepository.getUserGrade()
        val yes = mRepository.getUserStudy()
    }

    fun prepopulate() = mRepository.prepopulate()

}